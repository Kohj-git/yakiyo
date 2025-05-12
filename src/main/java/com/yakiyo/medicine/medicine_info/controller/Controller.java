package com.yakiyo.medicine.medicine_info.controller;

import com.yakiyo.medicine.medicine_info.dto.req.InfoCreateReqDto;
import com.yakiyo.medicine.medicine_info.dto.req.InfoUpdateReqDto;
import com.yakiyo.medicine.medicine_info.dto.res.MedicineListResDto;
import com.yakiyo.medicine.medicine_info.dto.res.MedicineDetailResDto;
import com.yakiyo.medicine.medicine_info.dto.res.NextMedicineResDto;
import com.yakiyo.medicine.medicine_info.dto.res.InfoResDto;
import com.yakiyo.medicine.medicine_info.dto.res.TakeMedicineResDto;
import com.yakiyo.medicine.medicine_info.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.yakiyo.medicine.record.dto.res.CalendarResDto;
import com.yakiyo.medicine.record.service.CalendarService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicine")
@Tag(name = "약 정보", description = "약 정보 관리를 위한 API")
public class Controller {

    private final InfoService infoService;
    private final CalendarService calendarService;

    @Operation(
        summary = "다음 복용 약 조회",
        description = "오늘 복용해야 할 약들 중 가장 가까운 시간의 약을 조회합니다."
    )
    @GetMapping("/{googleId}/next")
    public ResponseEntity<NextMedicineResDto> getNextMedicine(@PathVariable String googleId) {
        return ResponseEntity.ok(infoService.getNextMedicine(googleId));
    }

    @PostMapping("/{googleId}/{medicineId}/take")
    public ResponseEntity<TakeMedicineResDto> takeMedicine(
            @PathVariable String googleId,
            @PathVariable Long medicineId) {
        return ResponseEntity.ok(infoService.takeMedicine(googleId, medicineId));
    }

    @Operation(
        summary = "약 목록 조회",
        description = "사용자의 모든 약 목록을 조회합니다. 약 이름과 복용 시간대를 반환합니다."
    )
    @GetMapping("/{googleId}/medicines")
    public ResponseEntity<List<MedicineListResDto>> getMedicineList(@PathVariable String googleId) {
        return ResponseEntity.ok(infoService.getMedicineList(googleId));
    }



    @Operation(
        summary = "약 상세 정보 조회",
        description = "특정 약의 상세 정보를 조회합니다. 약 이름, 복용 기간, 복용 요일, 복용 시간등을 포함합니다."
    )
    @GetMapping("/{googleId}/{medicineId}")
    public ResponseEntity<MedicineDetailResDto> getMedicineDetail(
            @PathVariable String googleId,
            @PathVariable Long medicineId) {
        return ResponseEntity.ok(infoService.getMedicineDetail(googleId, medicineId));
    }

    @Operation(
        summary = "약 정보 수정",
        description = "약 이름, 복용 요일, 복용 시간을 수정합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = """
            {
                "medicineName": "타이레놀",
                "daysOfWeek": ["MON", "WED", "FRI"],
                "times": [
                    {
                        "period": "MORNING",
                        "specificTime": "09:00"
                    },
                    {
                        "period": "AFTERNOON",
                        "specificTime": "13:00"
                    },
                    {
                        "period": "EVENING",
                        "specificTime": "19:00"
                    }
                ]
            }
            """)))
    )
    @PatchMapping("/{googleId}/{medicineId}")
    public ResponseEntity<Void> updateMedicine(
            @PathVariable String googleId,
            @PathVariable Long medicineId,
            @RequestBody InfoUpdateReqDto request) {
        infoService.updateMedicineInfo(googleId, medicineId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "평생 복용 약 등록",
        description = "구글 ID를 통해 평생 복용해야 하는 약을 등록합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = """
            {
                "medicineName": "고혈압약",
                "isLifetime": true,
                "startDate": "2025-05-06",
                "daysOfWeek": ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"],
                "times": [
                    {
                        "period": "MORNING",
                        "specificTime": "08:00"
                    },
                    {
                        "period": "EVENING",
                        "specificTime": "20:00"
                    }
                ]
            }
            """)))
    )
    @PostMapping("/lifetime/{googleId}")
    public ResponseEntity<String> createLifetimeMedicine(
            @PathVariable String googleId,
            @RequestBody InfoCreateReqDto request) {
        infoService.createLifetimeMedicineInfo(googleId, request);
        return ResponseEntity.ok("약 정보가 성공적으로 등록되었습니다");
    }

    @Operation(
        summary = "기간제 약 등록",
        description = "구글 ID를 통해 일정 기간 동안 복용해야 하는 약을 등록합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = """
            {
                "medicineName": "타이레놀",
                "isLifetime": false,
                "startDate": "2025-05-06",
                "endDate": "2025-12-31",
                "daysOfWeek": ["MON", "WED", "FRI"],
                "times": [
                    {
                        "period": "MORNING",
                        "specificTime": "09:00"
                    },
                    {
                        "period": "EVENING",
                        "specificTime": "19:00"
                    }
                ]
            }
            """)))
    )
    @PostMapping("/period/{googleId}")
    public ResponseEntity<String> createPeriodMedicine(
            @PathVariable String googleId,
            @RequestBody InfoCreateReqDto request) {
        infoService.createPeriodMedicineInfo(googleId, request);
        return ResponseEntity.ok("약 정보가 성공적으로 등록되었습니다");
    }

    @Operation(
        summary = "월별 복용 현황 조회",
        description = "특정 월의 날짜별 복용 현황을 조회합니다. 상태는 RED(복용 미실패), GREEN(복용 성공), BLACK(복용할 약 없음), GRAY(미래 복용 예정)로 구분됩니다."
    )
    @GetMapping("/{googleId}/calendar")
    public ResponseEntity<CalendarResDto> getMonthlyCalendar(
            @PathVariable String googleId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(calendarService.getMonthlyCalendar(googleId, year, month));
    }
}
