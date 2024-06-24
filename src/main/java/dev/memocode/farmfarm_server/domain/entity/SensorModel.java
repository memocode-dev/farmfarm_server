package dev.memocode.farmfarm_server.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static dev.memocode.farmfarm_server.domain.entity.MeasurementType.HUMIDITY;
import static dev.memocode.farmfarm_server.domain.entity.MeasurementType.TEMPERATURE;
import static dev.memocode.farmfarm_server.domain.entity.MeasurementUnit.CELSIUS;
import static dev.memocode.farmfarm_server.domain.entity.MeasurementUnit.PERCENT;

@Getter
@AllArgsConstructor
public enum SensorModel {
    XY_MD02(new SensorModelInfo("XY_MD02", "온습도 센서",
            List.of(new MeasurementDetails(TEMPERATURE, CELSIUS), new MeasurementDetails(HUMIDITY, PERCENT)))),
    DS18B20(new SensorModelInfo("DS18B20", "온도 센서",
            List.of(new MeasurementDetails(TEMPERATURE, CELSIUS)))),
    ;

    private final SensorModelInfo modelInfo;
}
