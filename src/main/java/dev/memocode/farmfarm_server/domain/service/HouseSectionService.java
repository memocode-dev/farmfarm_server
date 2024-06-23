package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.House;
import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseRepository;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionRepository;
import dev.memocode.farmfarm_server.domain.service.converter.HouseSectionConverter;
import dev.memocode.farmfarm_server.domain.service.request.CreateHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.request.SyncHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.request.UpdateHouseSectionRequest;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse;
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

import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_FOUND_HOUSE;
import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_HEALTHY_HOUSE;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionErrorCode.INVALID_HOUSE_SECTION_RELATION;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionErrorCode.NOT_FOUND_HOUSE_SECTION;
import static dev.memocode.farmfarm_server.mqtt.dto.Mqtt5Method.UPSERT;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseSectionService {
    private final HouseRepository houseRepository;

    private final HouseSectionRepository houseSectionRepository;

    private final HouseSectionConverter houseSectionConverter;

    private final MqttSender mqttSender;

    @Transactional
    public UUID createHouseSection(@Valid CreateHouseSectionRequest request) {
        House house = houseRepository.findByIdAndDeleted(request.getHouseId(), false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        if (house.getSyncStatus() != SyncStatus.HEALTHY) {
            throw new BusinessRuleViolationException(NOT_HEALTHY_HOUSE);
        }

        // 하우스동 객체 생성
        HouseSection houseSection = HouseSection.builder()
                .sectionNumber(request.getSectionNumber())
                .house(house)
                .build();

        // 하우스동 저장
        houseSectionRepository.save(houseSection);

        return houseSection.getId();
    }

    @Transactional
    public void updateHouseSection(@Valid UpdateHouseSectionRequest request) {
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

        houseSection.changeSectionNumber(request.getSectionNumber());
    }

    @Transactional
    public void deleteHouseSection(
            @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be null") UUID houseId,
            @NotNull(message = "HOUSE_SECTION_ID_NOT_NULL:house section id cannot be null") UUID houseSectionId) {
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

        SyncHouseSectionRequest request = SyncHouseSectionRequest.builder()
                .houseId(house.getId())
                .houseSectionId(houseSection.getId())
                .houseSectionVersion(houseSection.getVersion())
                .sectionNumber(houseSection.getSectionNumber())
                .createdAt(houseSection.getCreatedAt())
                .updatedAt(houseSection.getUpdatedAt())
                .deletedAt(Instant.now())
                .deleted(true)
                .build();

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouseSections")
                .data(request)
                .build();

        mqttSender.send("request/%s".formatted(house.getId().toString()), message);
    }

    public FindAllHouseSectionsResponse findAllHouseSections(
            @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be null") UUID houseId) {
        houseRepository.findByIdAndDeleted(houseId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        List<HouseSection> houseSections = houseSectionRepository.findAllByHouseIdAndDeleted(houseId, false);

        return FindAllHouseSectionsResponse.builder()
                .houseSections(houseSectionConverter.toFindAllHouseSectionsResponse__HouseSection(houseSections))
                .build();
    }

    public void syncHouseSection(
            @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be null") UUID houseId,
            @NotNull(message = "HOUSE_SECTION_ID_NOT_NULL:house section id cannot be null") UUID houseSectionId) {
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

        SyncHouseSectionRequest request = SyncHouseSectionRequest.builder()
                .houseId(house.getId())
                .houseSectionId(houseSection.getId())
                .houseSectionVersion(houseSection.getVersion())
                .sectionNumber(houseSection.getSectionNumber())
                .createdAt(houseSection.getCreatedAt())
                .updatedAt(houseSection.getUpdatedAt())
                .deletedAt(houseSection.getDeletedAt())
                .deleted(houseSection.getDeleted())
                .build();

        Mqtt5Message message = Mqtt5Message.builder()
                .method(UPSERT)
                .uri("/localHouseSections")
                .data(request)
                .build();

        mqttSender.send("request/%s".formatted(house.getId().toString()), message);
    }
}
