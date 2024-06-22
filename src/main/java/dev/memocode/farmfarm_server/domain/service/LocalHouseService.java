package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.House;
import dev.memocode.farmfarm_server.domain.entity.LocalHouse;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseRepository;
import dev.memocode.farmfarm_server.domain.service.request.UpsertLocalHouseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

import static dev.memocode.farmfarm_server.domain.exception.HouseErrorCode.NOT_FOUND_HOUSE;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class LocalHouseService {
    private final HouseRepository houseRepository;
    private final LocalHouseRepository localHouseRepository;

    @Transactional
    public void upsertLocalHouse(@Valid UpsertLocalHouseRequest request) {
        localHouseRepository.findByHouseId(request.getHouseId())
                .map(existingLocalHouse -> updateLocalHouseIfNecessary(existingLocalHouse, request))
                .orElseGet(() -> createLocalHouse(request));
    }

    private LocalHouse updateLocalHouseIfNecessary(LocalHouse localHouse, UpsertLocalHouseRequest request) {
        // 하우스 버전이 db에 저장된 하우스 버전보다 클 경우에만 변경
        if (localHouse.getHouseVersion() <= request.getHouseVersion()) {
            localHouse.changeName(request.getName());
            localHouse.changeHouseVersion(request.getHouseVersion());
            localHouse.changeCreatedAt(request.getCreatedAt());
            localHouse.changeUpdatedAt(request.getUpdatedAt());
            localHouse.changeDeleted(request.getDeleted());
            localHouse.changeDeletedAt(request.getDeletedAt());
            localHouse.changeLastUpdatedAt(Instant.now());
        } else {
            // 변경하지 않음, 필요시 로깅
            log.info("No changes made to LocalHouse with houseId: {}", request.getHouseId());
        }

        return localHouse;
    }

    private LocalHouse createLocalHouse(UpsertLocalHouseRequest request) {
        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE));

        LocalHouse localHouse = LocalHouse.builder()
                .name(request.getName())
                .house(house)
                .houseVersion(request.getHouseVersion())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .deleted(request.getDeleted())
                .deletedAt(request.getDeletedAt())
                .lastUpdatedAt(Instant.now())
                .build();

        return localHouseRepository.save(localHouse);
    }
}
