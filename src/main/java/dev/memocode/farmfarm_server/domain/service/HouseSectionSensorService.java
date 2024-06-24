package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.House;
import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import dev.memocode.farmfarm_server.domain.entity.HouseSectionSensor;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseRepository;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionRepository;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionSensorRepository;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseSectionSensorRequest;
import dev.memocode.farmfarm_server.domain.service.request.SyncHouseSectionSensorRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseSectionSensorRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindHouseSectionSensorResponse;
import dev.memocode.farmfarm_server.mqtt.config.MqttSender;
import dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.UUID;

import static dev.memocode.farmfarm_server.domain.entity.SyncStatus.HEALTHY;
import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_FOUND_HOUSE;
import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_HEALTHY_HOUSE;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionErrorCode.*;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionSensorErrorCode.INVALID_HOUSE_SECTION_SENSOR_RELATION;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionSensorErrorCode.NOT_FOUND_HOUSE_SECTION_SENSOR;
import static dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseSectionSensorService {
    private final HouseRepository houseRepository;

    private final HouseSectionRepository houseSectionRepository;

    private final HouseSectionSensorRepository houseSectionSensorRepository;

    private final MqttSender mqttSender;

    @Transactional
    public UUID createHouseSectionSensor(@Valid CreateHouseSectionSensorRequest request) {
        House house = houseRepository.findByIdAndDeleted(request.getHouseId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        HouseSection houseSection = houseSectionRepository.findByIdAndDeleted(request.getHouseSectionId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

        if (houseSection.getSyncStatus() != HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE_SECTION);
        }

        HouseSectionSensor houseSectionSensor = HouseSectionSensor.builder()
                .nameForAdmin(request.getNameForAdmin())
                .nameForUser(request.getNameForUser())
                .sensorModel(request.getSensorModel())
                .houseSection(houseSection)
                .portName(request.getPortName())
                .build();

        houseSectionSensorRepository.save(houseSectionSensor);

        return houseSectionSensor.getId();
    }

    public void syncHouseSectionSensor(
            @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be null") UUID houseId,
            @NotNull(message = "HOUSE_SECTION_ID_NOT_NULL:house section id cannot be null") UUID houseSectionId,
            @NotNull(message = "HOUSE_SECTION_SENSOR_ID_NOT_NULL:house id cannot be null") UUID houseSectionSensorId
    ) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        HouseSection houseSection = houseSectionRepository.findById(houseSectionId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

        if (!houseSection.getHouse().equals(house)) {
            throw new BusinessRuleViolationException(INVALID_HOUSE_SECTION_RELATION);
        }

        HouseSectionSensor houseSectionSensor = houseSectionSensorRepository.findById(houseSectionSensorId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

        if (!houseSectionSensor.getHouseSection().equals(houseSection)) {
            throw new BusinessRuleViolationException(INVALID_HOUSE_SECTION_SENSOR_RELATION);
        }

        SyncHouseSectionSensorRequest request = SyncHouseSectionSensorRequest.builder()
                .houseId(house.getId())
                .houseSectionId(houseSection.getId())
                .houseSectionSensorId(houseSectionSensor.getId())
                .houseSectionSensorVersion(houseSectionSensor.getVersion())
                .nameForAdmin(houseSectionSensor.getNameForAdmin())
                .nameForUser(houseSectionSensor.getNameForUser())
                .sensorModel(houseSectionSensor.getSensorModel())
                .portName(houseSectionSensor.getPortName())
                .createdAt(houseSectionSensor.getCreatedAt())
                .updatedAt(houseSectionSensor.getUpdatedAt())
                .deletedAt(houseSectionSensor.getDeletedAt())
                .deleted(houseSectionSensor.getDeleted())
                .build();

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouseSectionSensors")
                .data(request)
                .build();

        mqttSender.send("request/%s".formatted(house.getId().toString()), message);
    }

    @Transactional
    public void deleteHouseSectionSensor(UUID houseId, UUID houseSectionId, UUID houseSectionSensorId) {
        House house = houseRepository.findByIdAndDeleted(houseId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        HouseSection houseSection = houseSectionRepository.findByIdAndDeleted(houseSectionId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

        if (!houseSection.getHouse().equals(house)) {
            throw new BusinessRuleViolationException(INVALID_HOUSE_SECTION_RELATION);
        }

        if (houseSection.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE_SECTION);
        }

        HouseSectionSensor houseSectionSensor =
                houseSectionSensorRepository.findByIdAndDeleted(houseSectionSensorId, false)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

        if (!houseSectionSensor.getHouseSection().equals(houseSection)) {
            throw new BusinessRuleViolationException(INVALID_HOUSE_SECTION_SENSOR_RELATION);
        }

        SyncHouseSectionSensorRequest request = SyncHouseSectionSensorRequest.builder()
                .houseId(house.getId())
                .houseSectionId(houseSection.getId())
                .houseSectionSensorId(houseSectionSensor.getId())
                .houseSectionSensorVersion(houseSectionSensor.getVersion())
                .nameForAdmin(houseSectionSensor.getNameForAdmin())
                .nameForUser(houseSectionSensor.getNameForUser())
                .sensorModel(houseSectionSensor.getSensorModel())
                .createdAt(houseSectionSensor.getCreatedAt())
                .updatedAt(houseSectionSensor.getUpdatedAt())
                .deletedAt(Instant.now())
                .deleted(true)
                .build();

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouseSectionSensors")
                .data(request)
                .build();

        mqttSender.send("request/%s".formatted(house.getId().toString()), message);
    }

    @Transactional
    public void updateHouseSectionSensor(@Valid UpdateHouseSectionSensorRequest request) {
        House house = houseRepository.findByIdAndDeleted(request.getHouseId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        HouseSection houseSection = houseSectionRepository.findByIdAndDeleted(request.getHouseSectionId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

        if (!houseSection.getHouse().equals(house)) {
            throw new BusinessRuleViolationException(INVALID_HOUSE_SECTION_RELATION);
        }

        if (houseSection.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE_SECTION);
        }

        HouseSectionSensor houseSectionSensor =
                houseSectionSensorRepository.findByIdAndDeleted(request.getHouseSectionSensorId(), false)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

        if (!houseSectionSensor.getHouseSection().equals(houseSection)) {
            throw new BusinessRuleViolationException(INVALID_HOUSE_SECTION_SENSOR_RELATION);
        }

        if (request.getPortName() != null) {
            houseSectionSensor.changePortName(request.getPortName());
        }

        if (request.getNameForAdmin() != null) {
            houseSectionSensor.changeNameForAdmin(request.getNameForAdmin());
        }

        if (request.getNameForUser() != null) {
            houseSectionSensor.changeNameForUser(request.getNameForUser());
        }
    }

    public FindHouseSectionSensorResponse findHouseSectionSensor(UUID houseSectionSensorId, boolean withDeleted) {
        HouseSectionSensor houseSectionSensor = withDeleted ?
                houseSectionSensorRepository.findById(houseSectionSensorId)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR)) :
                houseSectionSensorRepository.findByIdAndDeleted(houseSectionSensorId, false)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

        return FindHouseSectionSensorResponse.builder()
                .id(houseSectionSensor.getId())
                .nameForAdmin(houseSectionSensor.getNameForAdmin())
                .nameForUser(houseSectionSensor.getNameForUser())
                .portName(houseSectionSensor.getPortName())
                .syncStatus(houseSectionSensor.getSyncStatus())
                .createdAt(houseSectionSensor.getCreatedAt())
                .updatedAt(houseSectionSensor.getUpdatedAt())
                .deletedAt(houseSectionSensor.getDeletedAt())
                .deleted(houseSectionSensor.getDeleted())
                .build();
    }
}
