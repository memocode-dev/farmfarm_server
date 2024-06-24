package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateHouseSectionForm;
import dev.memocode.farmfarm_server.api.form.UpdateHouseSectionForm;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.service.HouseSectionService;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse;
import dev.memocode.farmfarm_server.domain.service.response.FindHouseSection;
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
import static dev.memocode.farmfarm_server.domain.exception.BaseErrorCode.SYNC_FAILED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/sections")
@Tag(name = "houses", description = "하우스")
public class HouseSectionController {
    private final HouseSectionService houseSectionService;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostMapping
    @Operation(summary = "하우스동 생성", description = "하우스동을 생성할 수 있습니다.")
    public DeferredResult<ResponseEntity<String>> createHouseSection(@PathVariable UUID houseId, @RequestBody CreateHouseSectionForm form) {
        CreateHouseSectionRequest request = CreateHouseSectionRequest.builder()
                .houseId(houseId)
                .sectionNumber(form.getSectionNumber())
                .build();

        UUID houseSectionId = houseSectionService.createHouseSection(request);

        houseSectionService.syncHouseSection(houseId, houseSectionId);

        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(5000L);
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                SyncStatus syncStatus = houseSectionService.findHouseSection(houseSectionId, false).getSyncStatus();
                if (syncStatus == HEALTHY) {
                    deferredResult.setResult(ResponseEntity.created(URI.create(houseSectionId.toString())).body(houseSectionId.toString()));
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

        // 요청 취소 시 처리
        deferredResult.onCompletion(() -> {
            scheduledFuture.cancel(true);
        });

        return deferredResult;
    }

    @PatchMapping("/{houseSectionId}")
    @Operation(summary = "하우스동 수정", description = "하우스동을 수정할 수 있습니다.")
    public DeferredResult<ResponseEntity<Void>> updateHouseSection(
            @PathVariable UUID houseId,
            @PathVariable UUID houseSectionId,
            @RequestBody UpdateHouseSectionForm form) {
        UpdateHouseSectionRequest request = UpdateHouseSectionRequest.builder()
                .houseId(houseId)
                .houseSectionId(houseSectionId)
                .sectionNumber(form.getSectionNumber())
                .build();

        houseSectionService.updateHouseSection(request);

        houseSectionService.syncHouseSection(houseId, houseSectionId);

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                SyncStatus syncStatus = houseSectionService.findHouseSection(houseSectionId, false).getSyncStatus();
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

    @DeleteMapping("/{houseSectionId}")
    @Operation(summary = "하우스동 삭제", description = "하우스동을 삭제할 수 있습니다.")
    public DeferredResult<ResponseEntity<Void>> deleteHouseSection(@PathVariable UUID houseId, @PathVariable UUID houseSectionId) {
        houseSectionService.deleteHouseSection(houseId, houseSectionId);

        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>(5000L); // 5초 타임아웃 설정
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            try {
                FindHouseSection houseSection = houseSectionService.findHouseSection(houseSectionId, true);
                if (houseSection.getDeleted()) {
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

    @GetMapping
    @Operation(summary = "하우스동 전체 조회", description = "하우스동을 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllHouseSectionsResponse> findAllHouseSections(@PathVariable UUID houseId) {
        FindAllHouseSectionsResponse response = houseSectionService.findAllHouseSections(houseId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{houseSectionId}/sync")
    @Operation(summary = "하우스동 동기화", description = "하우스동을 로컬서버에 동기화할 수 있습니다.")
    public ResponseEntity<Void> syncHouseSection(@PathVariable UUID houseId, @PathVariable UUID houseSectionId) {
        houseSectionService.syncHouseSection(houseId, houseSectionId);

        return ResponseEntity.noContent().build();
    }
}
