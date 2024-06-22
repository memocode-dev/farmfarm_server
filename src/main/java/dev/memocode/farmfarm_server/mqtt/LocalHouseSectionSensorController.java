package dev.memocode.farmfarm_server.mqtt;

import dev.memocode.farmfarm_server.domain.service.LocalHouseSectionSensorService;
import dev.memocode.farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorRequest;
import dev.memocode.farmfarm_server.mqtt.config.Mqtt5Body;
import dev.memocode.farmfarm_server.mqtt.config.MqttHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocalHouseSectionSensorController {
    private final LocalHouseSectionSensorService localHouseSectionService;

    @MqttHandler(method = UPSERT, uri = "/localHouseSectionSensors")
    public void upsertLocalHouseSectionSensors(@Mqtt5Body UpsertLocalHouseSectionSensorRequest request) {
        log.info("request: {}", request);
        localHouseSectionService.upsertLocalHouseSectionSensor(request);
    }
}
