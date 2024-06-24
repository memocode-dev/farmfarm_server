package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateHouseSectionSensorForm;
import dev.memocode.farmfarm_server.api.form.UpdateHouseSectionSensorForm;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.service.HouseSectionSensorService;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseSectionSensorRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseSectionSensorRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindHouseSectionSensorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import static dev.memocode.farmfarm_server.domain.entity.SyncStatus.HEALTHY;
import static dev.memocode.farmfarm_server.domain.exception.BaseErrorCode.SYNC_FAILED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/sections/{houseSectionId}/sensors")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "houses", description = "하우스")
public class HouseSectionSensorController {
    private final HouseSectionSensorService houseSectionSensorService;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostMapping
    @Operation(summary = "하우스동 센서 생성", description = "하우스동 센서를 생성할 수 있습니다.")
    public DeferredResult<ResponseEntity<String>> createHouseSectionSensor(
            @PathVariable UUID houseId, @PathVariable UUID houseSectionId,
            @RequestBody CreateHouseSectionSensorForm form) {
        CreateHouseSectionSensorRequest request = CreateHouseSectionSensorRequest.builder()
                .houseId(houseId)
                .houseSectionId(houseSectionId)
                .nameForAdmin(form.getNameForAdmin())
                .nameForUser(form.getNameForUser())
                .portName(form.getPortName())
                .sensorModel(form.getSensorModel())
                .build();

        UUID houseSectionSensorId = houseSectionSensorService.createHouseSectionSensor(request);

        houseSectionSensorService.syncHouseSectionSensor(houseId, houseSectionId, houseSectionSensorId);

        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                SyncStatus syncStatus = houseSectionSensorService.findHouseSectionSensor(houseSectionSensorId, false).getSyncStatus();
                if (syncStatus == HEALTHY) { // SyncStatus의 HEALTHY 상수 사용
                    deferredResult.setResult(ResponseEntity.created(URI.create(houseSectionSensorId.toString())).body(houseSectionSensorId.toString()));
                }
            } catch (Exception e) {
                deferredResult.setErrorResult(e);
            }
        }, Duration.ofMillis(1000));

        // 타임아웃 설정
        deferredResult.onTimeout(() -> {
            scheduledFuture.cancel(true);
            deferredResult.setErrorResult(new BusinessRuleViolationException(SYNC_FAILED));
        });

        // 요청 완료 또는 취소 시 처리
        deferredResult.onCompletion(() -> {
            scheduledFuture.cancel(true);
        });

        return deferredResult;
    }

    @PatchMapping("/{houseSectionSensorId}")
    @Operation(summary = "하우스동 센서 수정", description = "하우스동 센서를 수정할 수 있습니다.")
    public DeferredResult<ResponseEntity<Void>> updateHouseSectionSensor(
            @PathVariable UUID houseId,
            @PathVariable UUID houseSectionId,
            @PathVariable UUID houseSectionSensorId,
            @RequestBody UpdateHouseSectionSensorForm form) {
        UpdateHouseSectionSensorRequest request = UpdateHouseSectionSensorRequest.builder()
                .houseId(houseId)
                .houseSectionId(houseSectionId)
                .houseSectionSensorId(houseSectionSensorId)
                .nameForAdmin(form.getNameForAdmin())
                .nameForUser(form.getNameForUser())
                .portName(form.getPortName())
                .build();

        houseSectionSensorService.updateHouseSectionSensor(request);

        houseSectionSensorService.syncHouseSectionSensor(houseId, houseSectionId, houseSectionSensorId);

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                SyncStatus syncStatus = houseSectionSensorService.findHouseSectionSensor(houseSectionSensorId, false).getSyncStatus();
                if (syncStatus == HEALTHY) { // SyncStatus의 HEALTHY 상수 사용
                    deferredResult.setResult(ResponseEntity.noContent().build());
                }
            } catch (Exception e) {
                deferredResult.setErrorResult(e);
            }
        }, Duration.ofMillis(1000));

        // 타임아웃 설정
        deferredResult.onTimeout(() -> {
            scheduledFuture.cancel(true);
            deferredResult.setErrorResult(new BusinessRuleViolationException(SYNC_FAILED));
        });

        // 요청 완료 또는 취소 시 처리
        deferredResult.onCompletion(() -> {
            scheduledFuture.cancel(true);
        });

        return deferredResult;
    }

    @DeleteMapping("/{houseSectionSensorId}")
    @Operation(summary = "하우스동 센서 삭제", description = "하우스동 센서를 삭제할 수 있습니다.")
    public DeferredResult<ResponseEntity<Void>> deleteHouseSectionSensor(
            @PathVariable UUID houseId, @PathVariable UUID houseSectionId, @PathVariable UUID houseSectionSensorId) {
        houseSectionSensorService.deleteHouseSectionSensor(houseId, houseSectionId, houseSectionSensorId);

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                FindHouseSectionSensorResponse houseSectionSensor =
                        houseSectionSensorService.findHouseSectionSensor(houseSectionSensorId, true);
                if (houseSectionSensor.getDeleted()) {
                    deferredResult.setResult(ResponseEntity.noContent().build());
                }
            } catch (Exception e) {
                deferredResult.setErrorResult(e);
            }
        }, Duration.ofMillis(1000));

        // 타임아웃 설정
        deferredResult.onTimeout(() -> {
            scheduledFuture.cancel(true);
            deferredResult.setErrorResult(new BusinessRuleViolationException(SYNC_FAILED));
        });

        // 요청 완료 또는 취소 시 처리
        deferredResult.onCompletion(() -> {
            scheduledFuture.cancel(true);
        });

        return deferredResult;
    }

    @PutMapping("/{houseSectionSensorId}/sync")
    @Operation(summary = "하우스동 센서 동기화", description = "하우스동 센서를 로컬서버에 동기화 할 수 있습니다.")
    public ResponseEntity<Void> syncHouseSectionSensor(
            @PathVariable UUID houseId, @PathVariable UUID houseSectionId, @PathVariable UUID houseSectionSensorId) {
        houseSectionSensorService.syncHouseSectionSensor(houseId, houseSectionId, houseSectionSensorId);

        return ResponseEntity.noContent().build();
    }
}
