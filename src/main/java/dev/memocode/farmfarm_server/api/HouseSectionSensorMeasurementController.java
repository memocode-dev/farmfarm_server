package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.domain.service.HouseSectionSensorMeasurementService;
import dev.memocode.farmfarm_server.domain.service.request.FindAllHouseSectionSensorMeasurementsRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionSensorMeasurementsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static dev.memocode.farmfarm_server.utils.InstantUtils.getStartOfNextDay;
import static dev.memocode.farmfarm_server.utils.InstantUtils.getStartOfToday;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/sections/{houseSectionId}/sensors/{houseSectionSensorId}/measurements")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "houses", description = "하우스")
public class HouseSectionSensorMeasurementController {
    private final HouseSectionSensorMeasurementService houseSectionSensorMeasurementService;

    @GetMapping
    @Operation(summary = "하우스동 센서 측정데이터 전체 조회", description = "하우스동 센서를 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllHouseSectionSensorMeasurementsResponse> findAllHouseSectionSensorMeasurements(
            @PathVariable UUID houseId, @PathVariable UUID houseSectionId, @PathVariable UUID houseSectionSensorId,
            @RequestParam(required = false) Instant startMeasuredAt, @RequestParam(required = false) Instant endMeasuredAt) {

        FindAllHouseSectionSensorMeasurementsRequest request = FindAllHouseSectionSensorMeasurementsRequest.builder()
                .houseId(houseId)
                .houseSectionId(houseSectionId)
                .houseSectionSensorId(houseSectionSensorId)
                .startMeasuredAt(startMeasuredAt != null ? startMeasuredAt : getStartOfToday(ZoneOffset.of("+09:00")))
                .endMeasuredAt(endMeasuredAt != null ? endMeasuredAt : getStartOfNextDay(ZoneOffset.of("+09:00")))
                .build();

        FindAllHouseSectionSensorMeasurementsResponse response = houseSectionSensorMeasurementService.findAllHouseSectionSensorMeasurements(request);

        return ResponseEntity.ok(response);
    }
}
