package dev.memocode.farmfarm_server.api;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import dev.memocode.farmfarm_server.domain.entity.SensorModelInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/sensorModels")
@Tag(name = "sensors", description = "센서")
public class SensorModelController {

    @GetMapping
    @Operation(summary = "센서 모델 전체 조회", description = "센서 모델을 전체 조회할 수 있습니다.")
    public ResponseEntity<List<SensorModelInfo>> findAllSensorModels() {
        List<SensorModelInfo> sensorModelInfos = Arrays.stream(SensorModel.values())
                .map(SensorModel::getModelInfo)
                .toList();

        return ResponseEntity.ok(sensorModelInfos);
    }
}
