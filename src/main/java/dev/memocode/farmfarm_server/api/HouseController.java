package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.api.form.CreateHouseForm;
import dev.memocode.farmfarm_server.domain.House;
import dev.memocode.farmfarm_server.service.HouseService;
import dev.memocode.farmfarm_server.service.request.CreateHouseRequest;
import dev.memocode.farmfarm_server.service.response.FindAllHousesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
@Tag(name = "houses", description = "하우스")
public class HouseController {
    private final HouseService houseService;

    @PostMapping
    @Operation(summary = "하우스 생성", description = "하우스를 생성할 수 있습니다.")
    public ResponseEntity<String> createHouse(@RequestBody CreateHouseForm form) {
        CreateHouseRequest request = CreateHouseRequest.builder()
                .name(form.getName())
                .build();

        UUID houseId = houseService.createHouse(request);

        return ResponseEntity.created(URI.create(houseId.toString())).body(houseId.toString());
    }

    @GetMapping
    @Operation(summary = "하우스 전체 조회", description = "하우스를 전체 조회할 수 있습니다.")
    public ResponseEntity<FindAllHousesResponse> findAllHouses() {
        FindAllHousesResponse response = houseService.findAllHouses();

        return ResponseEntity.ok(response);
    }
}
