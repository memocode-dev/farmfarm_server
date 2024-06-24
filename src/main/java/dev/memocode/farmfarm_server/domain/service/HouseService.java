package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.House;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseRepository;
import dev.memocode.farmfarm_server.domain.service.converter.HouseConverter;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseRequest;
import dev.memocode.farmfarm_server.domain.service.request.SyncHouseRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHousesResponse;
import dev.memocode.farmfarm_server.domain.service.response.FindHouseResponse;
import dev.memocode.farmfarm_server.mqtt.config.MqttSender;
import dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.*;
import static dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;

    private final HouseConverter houseConverter;

    private final MqttSender mqttSender;

    @Transactional
    public UUID createHouse(@Valid CreateHouseRequest request) {
        // 중복 이름 검사
        if (houseRepository.existsByName(request.getName())) {
            throw new BusinessRuleViolationException(ALREADY_EXISTS_HOUSE_NAME);
        }

        // 하우스 객체 생성
        House house = House.builder()
                .name(request.getName())
                .build();

        // 하우스 저장
        houseRepository.save(house);

        return house.getId();
    }

    @Transactional
    public void updateHouse(@Valid UpdateHouseRequest request) {
        // 하우스 조회
        House house = houseRepository.findByIdAndDeleted(request.getHouseId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        // 현재 하우스 이름이 아닌 경우
        if (!house.getName().equals(request.getName())) {
            // 중복 이름 검사
            if (houseRepository.existsByName(request.getName())) {
                throw new BusinessRuleViolationException(ALREADY_EXISTS_HOUSE_NAME);
            }
        }

        // 하우스 이름 변경
        house.changeName(request.getName());
    }

    @Transactional
    public void deleteHouse(@NotNull(message = "HOUSE_ID_NOT_NULL:house cannot be null") UUID houseId) {
        // 하우스 조회
        House house = houseRepository.findByIdAndDeleted(houseId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        SyncHouseRequest request = SyncHouseRequest.builder()
                .houseId(house.getId())
                .name(house.getName())
                .houseVersion(house.getVersion())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .deletedAt(Instant.now())
                .deleted(true)
                .build();

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouses")
                .data(request)
                .build();

        mqttSender.sendRequest(house.getId(), message);
    }

    public FindAllHousesResponse findAllHouses() {
        List<House> houses = houseRepository.findAllByDeleted(false);

        return FindAllHousesResponse.builder()
                .houses(houseConverter.toFindAllHousesResponse__House(houses))
                .build();
    }

    public FindHouseResponse findHouse(@NotNull(message = "HOUSE_ID_NOT_NULL:house cannot be null") UUID houseId, boolean withDeleted) {
        House house = withDeleted ?
                houseRepository.findById(houseId)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE)) :
                houseRepository.findByIdAndDeleted(houseId, false)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        return FindHouseResponse.builder()
                .id(house.getId())
                .name(house.getName())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .deletedAt(house.getDeletedAt())
                .deleted(house.getDeleted())
                .syncStatus(house.getSyncStatus())
                .build();
    }

    public void syncHouse(@NotNull(message = "HOUSE_ID_NOT_NULL:house cannot be null") UUID houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        SyncHouseRequest request = SyncHouseRequest.builder()
                .houseId(house.getId())
                .name(house.getName())
                .houseVersion(house.getVersion())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .deletedAt(house.getDeletedAt())
                .deleted(house.getDeleted())
                .build();

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouses")
                .data(request)
                .build();

        mqttSender.sendRequest(house.getId(), message);
    }
}
