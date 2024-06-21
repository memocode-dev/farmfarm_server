package dev.memocode.farmfarm_server.service;

import dev.memocode.farmfarm_server.domain.House;
import dev.memocode.farmfarm_server.domain.exception.BusinessRuleViolationException;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.repository.HouseRepository;
import dev.memocode.farmfarm_server.service.converter.HouseConverter;
import dev.memocode.farmfarm_server.service.request.CreateHouseRequest;
import dev.memocode.farmfarm_server.service.request.UpdateHouseRequest;
import dev.memocode.farmfarm_server.service.response.FindAllHousesResponse;
import dev.memocode.farmfarm_server.service.response.FindHouseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.ALREADY_EXISTS_HOUSE_NAME;
import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_FOUND_HOUSE;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;

    private final HouseConverter houseConverter;

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

        // 하우스 소프트 삭제
        house.softDelete();
    }

    public FindAllHousesResponse findAllHouses() {
        List<House> houses = houseRepository.findAllByDeleted(false);

        return FindAllHousesResponse.builder()
                .houses(houseConverter.toFindAllHousesResponse__House(houses))
                .build();
    }

    public FindHouseResponse findHouse(@NotNull(message = "HOUSE_ID_NOT_NULL:house cannot be null") UUID houseId) {
        House house = houseRepository.findByIdAndDeleted(houseId, false)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        return FindHouseResponse.builder()
                .id(house.getId())
                .name(house.getName())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .build();
    }
}
