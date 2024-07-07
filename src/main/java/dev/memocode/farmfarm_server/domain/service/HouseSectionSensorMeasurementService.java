package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.HouseSectionSensor;
import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.farmfarm_server.domain.entity.MeasurementType;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseRepository;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionRepository;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionSensorRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionSensorMeasurementRepository;
import dev.memocode.farmfarm_server.domain.service.dto.MeasurementIntervalDTO;
import dev.memocode.farmfarm_server.domain.service.request.FindAllHouseSectionSensorMeasurementsRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionSensorMeasurementsResponse;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionSensorMeasurementsResponse__HouseSectionSensorMeasurement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static dev.memocode.farmfarm_server.domain.entity.SyncStatus.NOT_CREATED;
import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_FOUND_HOUSE;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionErrorCode.NOT_FOUND_HOUSE_SECTION;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionSensorErrorCode.NOT_FOUND_HOUSE_SECTION_SENSOR;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionSensorErrorCode.SENSOR_NOT_REGISTERED_LOCALLY;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseSectionSensorMeasurementService {
    private final HouseRepository houseRepository;
    private final HouseSectionRepository houseSectionRepository;
    private final HouseSectionSensorRepository houseSectionSensorRepository;
    private final LocalHouseSectionSensorMeasurementRepository localHouseSectionSensorMeasurementRepository;

    public FindAllHouseSectionSensorMeasurementsResponse findAllHouseSectionSensorMeasurements(FindAllHouseSectionSensorMeasurementsRequest request) {

        houseRepository.findByIdAndDeleted(request.getHouseId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        houseSectionRepository.findByIdAndDeleted(request.getHouseSectionId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

        HouseSectionSensor houseSectionSensor = houseSectionSensorRepository.findByIdAndDeleted(request.getHouseSectionSensorId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

        if (houseSectionSensor.getSyncStatus() == NOT_CREATED) {
            throw new BusinessRuleViolationException(SENSOR_NOT_REGISTERED_LOCALLY);
        }

        LocalHouseSectionSensor localHouseSectionSensor = houseSectionSensor.getLocalHouseSectionSensor();

        List<MeasurementIntervalDTO> measurementIntervalDTOS = this.findAverageAllSensorDataByLocalHouseSectionSensorAndMeasuredAtBetween(
                localHouseSectionSensor, request.getStartMeasuredAt(), request.getEndMeasuredAt());

        return FindAllHouseSectionSensorMeasurementsResponse.builder()
                .measurements(toDTOs(measurementIntervalDTOS))
                .startMeasuredAt(request.getStartMeasuredAt())
                .endMeasuredAt(request.getEndMeasuredAt())
                .build();
    }

    private List<MeasurementIntervalDTO> findAverageAllSensorDataByLocalHouseSectionSensorAndMeasuredAtBetween(LocalHouseSectionSensor sensor, Instant start, Instant end) {
        List<Object[]> rawResults = localHouseSectionSensorMeasurementRepository
                .findAverageAllSensorDataByLocalHouseSectionSensorAndMeasuredAtBetween(sensor.getId(), start, end);
        return rawResults.stream()
                .map(result -> new MeasurementIntervalDTO(
                        (Instant) result[0],
                        MeasurementType.valueOf((String) result[1]),
                        ((Number) result[2]).floatValue()))
                .collect(Collectors.toList());
    }

    private FindAllHouseSectionSensorMeasurementsResponse__HouseSectionSensorMeasurement toDTO(
            MeasurementIntervalDTO measurementIntervalDTO) {
        return FindAllHouseSectionSensorMeasurementsResponse__HouseSectionSensorMeasurement.builder()
                .measurementType(measurementIntervalDTO.getMeasurementType())
                .value(measurementIntervalDTO.getValue())
                .measuredAt(measurementIntervalDTO.getMeasuredAt())
                .build();
    }

    private List<FindAllHouseSectionSensorMeasurementsResponse__HouseSectionSensorMeasurement> toDTOs(
            List<MeasurementIntervalDTO> measurementIntervalDTOs) {
        return measurementIntervalDTOs.stream()
                .map(this::toDTO)
                .toList();
    }
}
