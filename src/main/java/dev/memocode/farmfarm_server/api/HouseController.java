package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateHouseForm;
import dev.memocode.farmfarm_server.api.form.UpdateHouseForm;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import dev.memocode.farmfarm_server.domain.service.HouseService;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHousesResponse;
import dev.memocode.farmfarm_server.domain.service.response.FindHouseResponse;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
@Tag(name = "houses", description = "하우스")
public class HouseController {
    private final HouseService houseService;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostMapping
    @Operation(summary = "하우스 생성", description = "하우스를 생성할 수 있습니다.")
    public ResponseEntity<String> createHouse(@RequestBody CreateHouseForm form) {
        CreateHouseRequest request = CreateHouseRequest.builder()
                .name(form.getName())
                .build();

        UUID houseId = houseService.createHouse(request);

        return ResponseEntity.created(URI.create(houseId.toString())).body(houseId.toString());
    }

    @PatchMapping("/{houseId}")
    @Operation(summary = "하우스 수정", description = "하우스를 수정할 수 있습니다.")
    public DeferredResult<ResponseEntity<Void>> updateHouse(@PathVariable UUID houseId, @RequestBody UpdateHouseForm form) {
        UpdateHouseRequest request = UpdateHouseRequest.builder()
                .houseId(houseId)
                .name(form.getName())
                .build();

        houseService.updateHouse(request);
        houseService.syncHouse(houseId);

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                SyncStatus syncStatus = houseService.findHouse(houseId, false).getSyncStatus();
                if (syncStatus == HEALTHY) { // SyncStatus의 HEALTHY 상수 사용
                    deferredResult.setResult(ResponseEntity.noContent().build());
                }
            } catch (Exception e) {
                deferredResult.setErrorResult(ResponseEntity.status(500).body("Internal server error occurred while syncing house section"));
            }
        }, Duration.ofMillis(1000));

        // 타임아웃 설정
        deferredResult.onTimeout(() -> {
            scheduledFuture.cancel(true);
            deferredResult.setErrorResult(ResponseEntity.status(202).body("House edit is still in progress"));
        });

        // 요청 완료 또는 취소 시 처리
        deferredResult.onCompletion(() -> {
            scheduledFuture.cancel(true);
        });

        return deferredResult;
    }

    @DeleteMapping("/{houseId}")
    @Operation(summary = "하우스 삭제", description = "하우스를 삭제할 수 있습니다.")
    public DeferredResult<ResponseEntity<Void>> deleteHouse(@PathVariable UUID houseId) {
        houseService.deleteHouse(houseId);

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                FindHouseResponse house = houseService.findHouse(houseId, true);
                if (house.getDeleted()) { // SyncStatus의 HEALTHY 상수 사용
                    deferredResult.setResult(ResponseEntity.noContent().build());
                }
            } catch (Exception e) {
                deferredResult.setErrorResult(ResponseEntity.status(500).body("Internal server error occurred while syncing house section"));
            }
        }, Duration.ofMillis(1000));

        // 타임아웃 설정
        deferredResult.onTimeout(() -> {
            scheduledFuture.cancel(true);
            deferredResult.setErrorResult(ResponseEntity.status(202).body("House deletion is still in progress"));
        });

        // 요청 완료 또는 취소 시 처리
        deferredResult.onCompletion(() -> {
            scheduledFuture.cancel(true);
        });

        return deferredResult;
    }

    @GetMapping
    @Operation(summary = "하우스 전체 조회", description = "하우스를 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllHousesResponse> findAllHouses() {
        FindAllHousesResponse response = houseService.findAllHouses();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{houseId}")
    @Operation(summary = "하우스 단건 조회", description = "하우스를 단건 조회할 수 있습니다.")
    public ResponseEntity<FindHouseResponse> findHouse(@PathVariable UUID houseId) {
        FindHouseResponse response = houseService.findHouse(houseId, false);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{houseId}/sync")
    @Operation(summary = "하우스 동기화", description = "하우스를 로컬서버에 동기화할 수 있습니다.")
    public ResponseEntity<Void> syncHouse(@PathVariable UUID houseId) {
        houseService.syncHouse(houseId);

        return ResponseEntity.noContent().build();
    }
}
