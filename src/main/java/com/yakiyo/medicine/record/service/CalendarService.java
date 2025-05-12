package com.yakiyo.medicine.record.service;

import com.yakiyo.exception.UserNotFoundException;
import com.yakiyo.medicine.medicine_info.domain.Medicine_info;
import com.yakiyo.medicine.medicine_info.repo.InfoRepo;
import com.yakiyo.medicine.record.domain.MedicationRecord;
import com.yakiyo.medicine.record.dto.res.CalendarResDto;
import com.yakiyo.medicine.record.repo.MedicationRecordRepo;
import com.yakiyo.medicine.time.domain.Time;
import com.yakiyo.user.domain.User;
import com.yakiyo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final UserRepo userRepo;
    private final InfoRepo infoRepo;
    private final MedicationRecordRepo recordRepo;

    @Transactional(readOnly = true)
    public CalendarResDto getMonthlyCalendar(String googleId, int year, int month) {
        // 1. 사용자 조회
        User user = userRepo.findById(googleId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));

        // 2. 해당 월의 시작일과 마지막 날짜 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
        LocalDate today = LocalDate.now();

        // 3. 사용자의 모든 약 정보 조회
        List<Medicine_info> medicines = infoRepo.findByUser(user);

        // 4. 해당 월의 모든 복용 기록 조회
        List<MedicationRecord> records = recordRepo.findByUserAndDateBetween(user, startDate, endDate);
        
        // 날짜별 복용 기록을 맵으로 변환
        Map<LocalDate, List<MedicationRecord>> recordsByDate = records.stream()
                .collect(Collectors.groupingBy(MedicationRecord::getDate));

        // 5. 각 날짜별 상태 계산
        List<CalendarResDto.DayStatus> days = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            CalendarResDto.Status status = calculateStatus(date, medicines, recordsByDate.get(date), today);
            days.add(CalendarResDto.DayStatus.builder()
                    .date(date.getDayOfMonth())
                    .status(status)
                    .build());
        }

        // 6. 응답 DTO 생성
        return CalendarResDto.builder()
                .year(year)
                .month(month)
                .days(days)
                .build();
    }

    private CalendarResDto.Status calculateStatus(
            LocalDate date,
            List<Medicine_info> medicines,
            List<MedicationRecord> records,
            LocalDate today) {

        // 해당 날짜에 복용할 약이 있는지 확인
        boolean hasMedicines = medicines.stream()
                .anyMatch(medicine -> shouldTakeMedicine(medicine, date));

        if (!hasMedicines) {
            return CalendarResDto.Status.BLACK;  // 복용할 약이 없는 날
        }

        if (date.isAfter(today)) {
            return CalendarResDto.Status.GRAY;   // 미래의 복용 예정일
        }

        // 과거나 오늘인 경우, 모든 약을 복용했는지 확인
        if (records == null || records.isEmpty()) {
            return CalendarResDto.Status.RED;    // 복용 기록이 없음
        }

        // 모든 약의 모든 시간대를 복용했는지 확인
        boolean allTaken = true;
        for (Medicine_info medicine : medicines) {
            if (shouldTakeMedicine(medicine, date)) {
                for (Time time : medicine.getTimes()) {
                    boolean taken = records.stream()
                            .anyMatch(record -> 
                                record.getMedicine().equals(medicine) &&
                                record.getTime().equals(time) &&
                                record.isTaken());
                    if (!taken) {
                        allTaken = false;
                        break;
                    }
                }
            }
            if (!allTaken) break;
        }

        return allTaken ? CalendarResDto.Status.GREEN : CalendarResDto.Status.RED;
    }

    private boolean shouldTakeMedicine(Medicine_info medicine, LocalDate date) {
        // 복용 기간 체크
        if (!medicine.getIsLifetime()) {
            if (date.isBefore(medicine.getStartDate()) || date.isAfter(medicine.getEndDate())) {
                return false;
            }
        } else if (date.isBefore(medicine.getStartDate())) {
            return false;
        }

        // 복용 요일 체크
        return medicine.getDays().stream()
                .anyMatch(day -> day.getDay().name().substring(0, 3)
                        .equals(date.getDayOfWeek().name().substring(0, 3)));
    }
}
