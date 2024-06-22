package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateHouseSectionSensorForm;
import dev.memocode.farmfarm_server.domain.service.HouseSectionSensorService;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseSectionSensorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/sections/{houseSectionId}/sensors")
@Tag(name = "houses", description = "하우스")
public class HouseSectionSensorController {
    private final HouseSectionSensorService houseSectionSensorService;

    @PostMapping
    @Operation(summary = "하우스동 센서 생성", description = "하우스동 센서를 생성할 수 있습니다.")
    public ResponseEntity<String> createHouseSection(
            @PathVariable UUID houseId, @PathVariable UUID houseSectionId,
            @RequestBody CreateHouseSectionSensorForm form) {
        CreateHouseSectionSensorRequest request = CreateHouseSectionSensorRequest.builder()
                .houseId(houseId)
                .houseSectionId(houseSectionId)
                .nameForAdmin(form.getNameForAdmin())
                .nameForUser(form.getNameForUser())
                .sensorModel(form.getSensorModel())
                .build();

        UUID houseSectionSensorId = houseSectionSensorService.createHouseSectionSensor(request);

        houseSectionSensorService.syncHouseSectionSensor(houseId, houseSectionId, houseSectionSensorId);

        return ResponseEntity.created(URI.create(houseSectionSensorId.toString())).body(houseSectionSensorId.toString());
    }

    @PutMapping("/{houseSectionSensorId}/sync")
    @Operation(summary = "하우스동 센서 동기화", description = "하우스동을 로컬서버에 동기화 할 수 있습니다.")
    public ResponseEntity<Void> syncHouse(
            @PathVariable UUID houseId, @PathVariable UUID houseSectionId, @PathVariable UUID houseSectionSensorId) {
        houseSectionSensorService.syncHouseSectionSensor(houseId, houseSectionId, houseSectionSensorId);

        return ResponseEntity.noContent().build();
    }
}
