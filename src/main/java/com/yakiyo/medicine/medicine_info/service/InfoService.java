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
import com.yakiyo.medicine.time.domain.Time;
import com.yakiyo.medicine.time.repo.TimeRepo;
import com.yakiyo.user.domain.User;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepo infoRepo;
    private final UserRepo userRepo;
    private final MedicineDayRepo medicineDayRepo;
    private final TimeRepo timeRepo;

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

        record MedicineTime(Medicine_info medicine, Time time) {}
        List<MedicineTime> todayMedicineTimes = medicines.stream()
                .filter(medicine -> medicine.getDays().stream()
                        .anyMatch(day -> day.getDay() == today))
                .flatMap(medicine -> medicine.getTimes().stream()
                        .map(time -> new MedicineTime(medicine, time)))
                .filter(mt -> mt.time().getSpecificTime().isAfter(now))
                .sorted(Comparator.comparing(mt -> mt.time().getSpecificTime()))
                .toList();

        // 4. 가장 가까운 시간의 약 정보 추출
        if (todayMedicineTimes.isEmpty()) {
            return null;  // 오늘 더 이상 복용할 약이 없음
        }

        MedicineTime nextMedicine = todayMedicineTimes.get(0);
        LocalTime nextTime = nextMedicine.time().getSpecificTime();

        // 5. 남은 시간 계산 (시간 단위)
        long hoursUntilNext = ChronoUnit.HOURS.between(now, nextTime);

        return NextMedicineResDto.builder()
                .hoursUntilNext(hoursUntilNext)
                .medicineName(nextMedicine.medicine().getMedicineName())
                .period(nextMedicine.time().getPeriod())
                .specificTime(nextTime)
                .hasTakenMedicine(false)  // TODO: 복용 여부 기록 구현 필요
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
