package com.yakiyo.medicine.medicine_info.service;

import com.yakiyo.exception.UserNotFoundException;
import com.yakiyo.medicine.days.domain.DayOfWeek;
import com.yakiyo.medicine.days.domain.Days;
import com.yakiyo.medicine.days.repo.MedicineDayRepo;
import com.yakiyo.medicine.medicine_info.domain.Medicine_info;
import com.yakiyo.medicine.medicine_info.dto.req.InfoCreateReqDto;
import com.yakiyo.medicine.medicine_info.dto.req.InfoUpdateReqDto;
import com.yakiyo.medicine.medicine_info.dto.res.MedicineListResDto;
import com.yakiyo.medicine.medicine_info.dto.res.MedicineDetailResDto;
import com.yakiyo.medicine.medicine_info.dto.res.NextMedicineResDto;
import com.yakiyo.medicine.medicine_info.repo.InfoRepo;
import com.yakiyo.medicine.record.domain.MedicationRecord;
import com.yakiyo.medicine.record.repo.MedicationRecordRepo;
import com.yakiyo.medicine.time.domain.Time;
import com.yakiyo.medicine.time.repo.TimeRepo;
import com.yakiyo.user.domain.User;
import com.yakiyo.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.yakiyo.medicine.record.dto.res.TakeMedicineResDto;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepo infoRepo;
    private final UserRepo userRepo;
    private final MedicineDayRepo medicineDayRepo;
    private final TimeRepo timeRepo;
    private final MedicationRecordRepo medicationRecordRepo;

    //약 목록 조회
    @Transactional(readOnly = true)
    public List<MedicineListResDto> getMedicineList(String googleId) {
        // 1. 구글 ID로 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 사용자의 모든 약 정보 조회 및 DTO로 변환
        return infoRepo.findByUser(user).stream()
                .map(info -> MedicineListResDto.builder()
                        .id(info.getId())  // 약 ID
                        .medicineName(info.getMedicineName())  // 약 이름
                        .periods(info.getTimes().stream()  // 복용 시간대 (아침/점심/저녁)
                                .map(Time::getPeriod)
                                .toList())
                        .build())
                .toList();
    }



    //약 상세 조회
    @Transactional(readOnly = true)
    public MedicineDetailResDto getMedicineDetail(String googleId, Long medicineId) {
        // 1. 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 약 정보 조회
        Medicine_info info = infoRepo.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("약을 찾을 수 없습니다."));

        // 3. 사용자의 약인지 확인
        if (!info.getUser().getId().equals(googleId)) {
            throw new IllegalArgumentException("해당 사용자의 약이 아닙니다.");
        }

        // 4. DTO 변환
        return MedicineDetailResDto.builder()
                .id(info.getId())
                .medicineName(info.getMedicineName())
                .isLifetime(info.getIsLifetime())
                .startDate(info.getStartDate())
                .endDate(info.getEndDate())
                .daysOfWeek(info.getDays().stream()
                        .map(day -> day.getDay())
                        .toList())
                .times(info.getTimes().stream()
                        .map(time -> MedicineDetailResDto.TimeInfo.builder()
                                .period(time.getPeriod())
                                .specificTime(time.getSpecificTime())
                                .build())
                        .toList())
                .build();
    }



    /**
     * 약 정보를 수정합니다.
     * 
     * @param googleId 사용자의 구글 ID
     * @param medicineId 약 ID
     * @param request 수정할 내용
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     * @throws IllegalArgumentException 약을 찾을 수 없거나 해당 사용자의 약이 아닌 경우
     */
    @Transactional
    public void updateMedicineInfo(String googleId, Long medicineId, InfoUpdateReqDto request) {
        // 1. 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 약 정보 조회
        Medicine_info info = infoRepo.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("약을 찾을 수 없습니다."));

        // 3. 사용자의 약인지 확인
        if (!info.getUser().getId().equals(googleId)) {
            throw new IllegalArgumentException("해당 사용자의 약이 아닙니다.");
        }

        // 4. 약 이름 수정
        info.updateMedicineName(request.getMedicineName());

        // 5. 복용 요일 수정
        // 5-1. 기존 요일 삭제
        medicineDayRepo.deleteAll(info.getDays());
        info.getDays().clear();

        // 5-2. 새로운 요일 추가
        request.getDaysOfWeek().forEach(day -> {
            Days medicineDay = Days.builder()
                    .day(day)
                    .build();
            info.addDay(medicineDay);
            medicineDayRepo.save(medicineDay);
        });

        // 6. 복용 시간 수정
        // 6-1. 기존 시간 삭제
        timeRepo.deleteAll(info.getTimes());
        info.getTimes().clear();

        // 6-2. 새로운 시간 추가
        request.getTimes().forEach(timeDto -> {
            Time time = Time.builder()
                    .period(timeDto.getPeriod())
                    .specificTime(timeDto.getSpecificTime())
                    .build();
            info.addTime(time);
            timeRepo.save(time);
        });
    }

    /**
     * 오늘 복용해야 할 약들 중 가장 가까운 시간의 약을 조회합니다.
     * 
     * @param googleId 사용자의 구글 ID
     * @return 다음 복용 약 정보 (약 이름, 남은 시간, 복용 시간대 포함)
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public NextMedicineResDto getNextMedicine(String googleId) {
        // 1. 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 사용자의 모든 약 정보 조회
        List<Medicine_info> medicines = infoRepo.findByUser(user);

        // 3. 현재 요일에 복용해야 하는 약들의 시간 추출
        LocalTime now = LocalTime.now();
        DayOfWeek today;
        switch (java.time.LocalDate.now().getDayOfWeek()) {
            case MONDAY -> today = DayOfWeek.MON;
            case TUESDAY -> today = DayOfWeek.TUE;
            case WEDNESDAY -> today = DayOfWeek.WED;
            case THURSDAY -> today = DayOfWeek.THU;
            case FRIDAY -> today = DayOfWeek.FRI;
            case SATURDAY -> today = DayOfWeek.SAT;
            case SUNDAY -> today = DayOfWeek.SUN;
            default -> throw new IllegalStateException("Unexpected value: " + java.time.LocalDate.now().getDayOfWeek());
        }

        record MedicineTime(Medicine_info medicine, Time time, boolean taken) {}

        // 오늘 복용해야 하는 약들과 시간 추출
        List<MedicineTime> todayMedicineTimes = medicines.stream()
                .filter(medicine -> medicine.getDays().stream()
                        .anyMatch(day -> day.getDay() == today))
                .flatMap(medicine -> {
                    // 이 약의 오늘 복용 기록 조회
                    List<MedicationRecord> records = medicationRecordRepo.findByUserAndMedicineAndDate(
                            user, medicine, LocalDate.now());
                    
                    return medicine.getTimes().stream()
                            .map(time -> new MedicineTime(
                                    medicine,
                                    time,
                                    records.stream()
                                            .anyMatch(record -> 
                                                record.getTime().equals(time) && 
                                                record.isTaken())
                            ));
                })
                .sorted(Comparator.comparing(mt -> mt.time().getSpecificTime()))
                .toList();

        if (todayMedicineTimes.isEmpty()) {
            return null;  // 오늘 복용할 약이 없음
        }

        // 4. 현재 시간 이후의 아직 안 먹은 가장 가까운 약 찾기
        MedicineTime nextMedicine = todayMedicineTimes.stream()
                .filter(mt -> mt.time().getSpecificTime().isAfter(now))
                .filter(mt -> !mt.taken())  // 아직 안 먹은 약만 필터링
                .findFirst()
                .orElse(null);

        if (nextMedicine == null) {
            return null;  // 오늘 더 이상 복용할 약이 없거나 모두 복용했음
        }

        // 5. 선택된 약의 오늘 모든 복용 시간 정보 수집
        Medicine_info selectedMedicine = nextMedicine.medicine();
        List<NextMedicineResDto.TimeStatus> timeStatuses = todayMedicineTimes.stream()
                .filter(mt -> mt.medicine().equals(selectedMedicine))
                .map(mt -> NextMedicineResDto.TimeStatus.builder()
                        .period(mt.time().getPeriod())
                        .specificTime(mt.time().getSpecificTime())
                        .hasTakenMedicine(mt.taken())
                        .build())
                .toList();

        // 6. 다음 복용까지 남은 시간 계산 (분 단위)
        long minutesUntilNext = ChronoUnit.MINUTES.between(now, nextMedicine.time().getSpecificTime());

        return NextMedicineResDto.builder()
                .minutesUntilNext(minutesUntilNext)
                .medicineName(selectedMedicine.getMedicineName())
                .todayTimes(timeStatuses)
                .build();
    }

    /**
     * 약 복용을 체크합니다.
     * 체크되지 않은 가장 이른 시간대의 복용을 체크합니다.
     * 
     * @param googleId 사용자의 구글 ID
     * @param medicineId 약 ID
     * @return 체크한 시간대와 체크한 시간
     */
    @Transactional
    public TakeMedicineResDto takeMedicine(String googleId, Long medicineId) {
        // 1. 사용자와 약 정보 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        
        Medicine_info medicine = infoRepo.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("약을 찾을 수 없습니다."));

        if (!medicine.getUser().getId().equals(googleId)) {
            throw new IllegalArgumentException("해당 사용자의 약이 아닙니다.");
        }

        // 2. 오늘 이 약의 복용 기록 조회
        List<MedicationRecord> todayRecords = medicationRecordRepo.findByUserAndMedicineAndDate(
                user, medicine, LocalDate.now());

        // 3. 아직 체크하지 않은 가장 이른 시간대 찾기
        Optional<Time> nextUntakenTimeOpt = medicine.getTimes().stream()
                .filter(time -> todayRecords.stream()
                        .noneMatch(record -> 
                            record.getTime().equals(time) && 
                            record.isTaken()))
                .min(Comparator.comparing(Time::getSpecificTime));

        // 모든 시간대가 체크되었다면
        if (nextUntakenTimeOpt.isEmpty()) {
            return TakeMedicineResDto.builder()
                    .allTaken(true)
                    .build();
        }

        Time nextUntakenTime = nextUntakenTimeOpt.get();

        // 4. 복용 기록 생성 또는 업데이트
        MedicationRecord record = todayRecords.stream()
                .filter(r -> r.getTime().equals(nextUntakenTime))
                .findFirst()
                .orElseGet(() -> {
                    MedicationRecord newRecord = MedicationRecord.builder()
                            .user(user)
                            .medicine(medicine)
                            .time(nextUntakenTime)
                            .date(LocalDate.now())
                            .taken(true)
                            .build();
                    return medicationRecordRepo.save(newRecord);
                });

        if (!record.isTaken()) {
            record.updateTaken(true);
        }

        // 5. 응답 생성
        return TakeMedicineResDto.builder()
                .allTaken(false)
                .takenTime(nextUntakenTime.getPeriod())
                .takenAt(LocalDateTime.now())
                .build();
    }

    // 평생 복용약 등록
    @Transactional
    public void createLifetimeMedicineInfo(String googleId, InfoCreateReqDto request) {
        // 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));

        // 기본 Info 생성 (평생 복용약)
        Medicine_info info = Medicine_info.builder()
                .user(user)
                .medicineName(request.getMedicineName())
                .isLifetime(true)
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now())
                .build();
        
        // 저장
        info = infoRepo.save(info);
        
        // 요일과 시간 정보 처리
        saveDaysAndTimes(info, request);
    }
    
    // 기간제 복용약 등록
    @Transactional
    public void createPeriodMedicineInfo(String googleId, InfoCreateReqDto request) {
        // 종료일 필수 검증
        if (request.getEndDate() == null) {
            throw new IllegalArgumentException("기간제 복용약은 종료일이 반드시 필요합니다");
        }
        
        // 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));

        // 기본 Info 생성 (기간제 복용약)
        Medicine_info info = Medicine_info.builder()
                .user(user)
                .medicineName(request.getMedicineName())
                .isLifetime(false)
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now())
                .endDate(request.getEndDate())
                .build();
        
        // 저장
        info = infoRepo.save(info);
        
        // 요일과 시간 정보 처리
        saveDaysAndTimes(info, request);
    }
    
    // 요일과 시간 정보 저장 (Helper 메서드)
    private void saveDaysAndTimes(Medicine_info info, InfoCreateReqDto request) {
        // 요일 정보 처리
        if (request.getDaysOfWeek() != null && !request.getDaysOfWeek().isEmpty()) {
            for (com.yakiyo.medicine.days.domain.DayOfWeek dayOfWeek : request.getDaysOfWeek()) {
                Days day = Days.builder()
                        .day(dayOfWeek)
                        .build();
                day = medicineDayRepo.save(day);
                info.addDay(day);  // Info 엔티티에 연관관계 추가
            }
        }
        
        // 시간 정보 처리
        if (request.getTimes() != null && !request.getTimes().isEmpty()) {
            for (InfoCreateReqDto.TimeMedicineDto timeDto : request.getTimes()) {
                Time time = Time.builder()
                        .period(timeDto.getPeriod())
                        .specificTime(timeDto.getSpecificTime())
                        .build();
                time = timeRepo.save(time);
                info.addTime(time);  // Info 엔티티에 연관관계 추가
            }
        }
    }
}
