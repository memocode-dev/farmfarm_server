package dev.memocode.farmfarm_server.mqtt;

import dev.memocode.farmfarm_server.domain.service.LocalHouseSectionSensorMeasurementService;
import dev.memocode.farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorMeasurementRequest;
import dev.memocode.farmfarm_server.mqtt.config.Mqtt5Body;
import dev.memocode.farmfarm_server.mqtt.config.MqttHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocalHouseSectionSensorMeasurementController {
    private final LocalHouseSectionSensorMeasurementService localHouseSectionSensorMeasurementService;

    @MqttHandler(method = UPSERT, uri = "/localHouseSectionSensorMeasurements")
    public void upsertLocalHouseSectionSensorMeasurements(@Mqtt5Body UpsertLocalHouseSectionSensorMeasurementRequest request) {
        log.info("request: {}", request);
        localHouseSectionSensorMeasurementService.upsertLocalHouseSectionSensorMeasurement(request);
    }
}
