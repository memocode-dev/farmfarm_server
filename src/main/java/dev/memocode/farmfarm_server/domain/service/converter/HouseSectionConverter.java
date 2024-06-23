package dev.memocode.farmfarm_server.domain.service.converter;

import dev.memocode.farmfarm_server.domain.entity.*;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionSensorMeasurementRepository;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse__HouseSection;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse__HouseSectionSensor;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse__HouseSectionSensorMeasurement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HouseSectionConverter {
    private final LocalHouseSectionSensorMeasurementRepository localHouseSectionSensorMeasurementRepository;

    public FindAllHouseSectionsResponse__HouseSection toFindAllHouseSectionsResponse__HouseSection(
            HouseSection houseSection) {
        List<HouseSectionSensor> houseSectionSensors = houseSection.getHouseSectionSensors();

        List<FindAllHouseSectionsResponse__HouseSectionSensor> sensors = houseSectionSensors.stream()
                .filter(houseSectionSensor -> !houseSectionSensor.getDeleted())
                .map(houseSectionSensor -> {
                    SensorModel sensorModel = houseSectionSensor.getSensorModel();
                    SensorModelInfo modelInfo = sensorModel.getModelInfo();

                    Map<MeasurementType, FindAllHouseSectionsResponse__HouseSectionSensorMeasurement> measurements =
                            new HashMap<>();

                    if (houseSectionSensor.getLocalHouseSectionSensor() != null) {
                        modelInfo.getMeasurementDetails()
                                .forEach(measurementDetail -> {
                                    Optional<LocalHouseSectionSensorMeasurement> _measurement =
                                            localHouseSectionSensorMeasurementRepository.
                                                    findTopByLocalHouseSectionSensorIdAndMeasurementTypeOrderByMeasuredAtDesc(
                                                            houseSectionSensor.getLocalHouseSectionSensor().getId(),
                                                            measurementDetail.getMeasurementType()
                                                    );

                                    if (_measurement.isEmpty()) {
                                        measurements.put(measurementDetail.getMeasurementType(),
                                                FindAllHouseSectionsResponse__HouseSectionSensorMeasurement.builder()
                                                        .measurementType(measurementDetail.getMeasurementType())
                                                        .value(0f)
                                                        .measuredAt(Instant.now())
                                                        .syncStatus(SyncStatus.NOT_CREATED)
                                                        .build());
                                    } else {
                                        LocalHouseSectionSensorMeasurement measurement = _measurement.get();
                                        measurements.put(measurement.getMeasurementType(),
                                                FindAllHouseSectionsResponse__HouseSectionSensorMeasurement.builder()
                                                        .measurementType(measurement.getMeasurementType())
                                                        .value(measurement.getValue())
                                                        .measuredAt(measurement.getMeasuredAt())
                                                        .syncStatus(SyncStatus.HEALTHY)
                                                        .build());
                                    }
                                });
                    }



                    return FindAllHouseSectionsResponse__HouseSectionSensor.builder()
                            .id(houseSectionSensor.getId())
                            .nameForAdmin(houseSectionSensor.getNameForAdmin())
                            .nameForUser(houseSectionSensor.getNameForUser())
                            .sensorModelInfo(houseSectionSensor.getSensorModel().getModelInfo())
                            .portName(houseSectionSensor.getPortName())
                            .createdAt(houseSectionSensor.getCreatedAt())
                            .updatedAt(houseSectionSensor.getUpdatedAt())
                            .syncStatus(houseSectionSensor.getSyncStatus())
                            .measurements(measurements)
                            .build();
                })
                .toList();

        return FindAllHouseSectionsResponse__HouseSection.builder()
                .id(houseSection.getId())
                .sectionNumber(houseSection.getSectionNumber())
                .createdAt(houseSection.getCreatedAt())
                .updatedAt(houseSection.getUpdatedAt())
                .syncStatus(houseSection.getSyncStatus())
                .sensors(sensors)
                .build();
    }

    public List<FindAllHouseSectionsResponse__HouseSection> toFindAllHouseSectionsResponse__HouseSection(
            List<HouseSection> houseSections) {
        return houseSections.stream()
                .map(this::toFindAllHouseSectionsResponse__HouseSection)
                .toList();
    }
}
