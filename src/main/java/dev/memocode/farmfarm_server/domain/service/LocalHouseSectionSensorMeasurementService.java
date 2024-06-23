package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensorMeasurement;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionSensorMeasurementRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionSensorRepository;
import dev.memocode.farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorMeasurementRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static dev.memocode.farmfarm_server.domain.exception.HouseSectionSensorErrorCode.NOT_FOUND_HOUSE_SECTION_SENSOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalHouseSectionSensorMeasurementService {
    private final LocalHouseSectionSensorMeasurementRepository localHouseSectionSensorMeasurementRepository;

    private final LocalHouseSectionSensorRepository localHouseSectionSensorRepository;

    @Transactional
    public void upsertLocalHouseSectionSensorMeasurement(@Valid UpsertLocalHouseSectionSensorMeasurementRequest request) {
        Optional<LocalHouseSectionSensorMeasurement> _localHouseSectionSensorMeasurement =
                localHouseSectionSensorMeasurementRepository.findByLocalHouseSectionSensorIdAndMeasurementTypeAndMeasuredAt(
                        request.getHouseSectionSensorId(), request.getMeasurementType(), request.getMeasuredAt());

        if (_localHouseSectionSensorMeasurement.isEmpty()) {
            LocalHouseSectionSensor localHouseSectionSensor =
                    localHouseSectionSensorRepository.findByHouseSectionSensorId(request.getHouseSectionSensorId())
                            .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

            LocalHouseSectionSensorMeasurement localHouseSectionSensorMeasurement =
                    LocalHouseSectionSensorMeasurement.builder()
                            .localHouseSectionSensor(localHouseSectionSensor)
                            .measurementType(request.getMeasurementType())
                            .value(request.getValue())
                            .measuredAt(request.getMeasuredAt())
                            .build();

            localHouseSectionSensorMeasurementRepository.save(localHouseSectionSensorMeasurement);
        } else {
            log.info("already saved to database");
        }
    }
}
