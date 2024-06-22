package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateHouseSectionForm;
import dev.memocode.farmfarm_server.api.form.UpdateHouseSectionForm;
import dev.memocode.farmfarm_server.domain.service.HouseSectionService;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/sections")
@Tag(name = "houses", description = "하우스")
public class HouseSectionController {
    private final HouseSectionService houseSectionService;

    @PostMapping
    @Operation(summary = "하우스동 생성", description = "하우스동을 생성할 수 있습니다.")
    public ResponseEntity<String> createHouseSection(@PathVariable UUID houseId, @RequestBody CreateHouseSectionForm form) {
        CreateHouseSectionRequest request = CreateHouseSectionRequest.builder()
                .houseId(houseId)
                .sectionNumber(form.getSectionNumber())
                .build();

        UUID houseSectionId = houseSectionService.createHouseSection(request);

        houseSectionService.syncHouseSection(houseId, houseSectionId);

        return ResponseEntity.created(URI.create(houseSectionId.toString())).body(houseSectionId.toString());
    }

    @PatchMapping("/{houseSectionId}")
    @Operation(summary = "하우스동 수정", description = "하우스동을 수정할 수 있습니다.")
    public ResponseEntity<Void> updateHouseSection(
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

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{houseSectionId}")
    @Operation(summary = "하우스 삭제", description = "하우스를 삭제할 수 있습니다.")
    public ResponseEntity<Void> deleteHouseSection(@PathVariable UUID houseId, @PathVariable UUID houseSectionId) {
        houseSectionService.deleteHouseSection(houseId, houseSectionId);

        houseSectionService.syncHouseSection(houseId, houseSectionId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "하우스동 전체 조회", description = "하우스를 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllHouseSectionsResponse> findAllHouseSections(@PathVariable UUID houseId) {
        FindAllHouseSectionsResponse response = houseSectionService.findAllHouseSections(houseId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{houseSectionId}/sync")
    @Operation(summary = "하우스 동기화", description = "하우스를 로컬서버에 동기화할 수 있습니다.")
    public ResponseEntity<Void> syncHouse(@PathVariable UUID houseId, @PathVariable UUID houseSectionId) {
        houseSectionService.syncHouseSection(houseId, houseSectionId);

        return ResponseEntity.noContent().build();
    }
}
