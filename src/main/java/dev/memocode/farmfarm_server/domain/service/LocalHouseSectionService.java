package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import dev.memocode.farmfarm_server.domain.entity.LocalHouse;
import dev.memocode.farmfarm_server.domain.entity.LocalHouseSection;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionRepository;
import dev.memocode.farmfarm_server.domain.service.request.UpsertLocalHouseSectionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.Optional;

import static dev.memocode.farmfarm_server.domain.exception.HouseSectionErrorCode.NOT_FOUND_HOUSE_SECTION;
import static dev.memocode.farmfarm_server.domain.exception.LocalHouseErrorCode.NOT_FOUND_LOCAL_HOUSE;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class LocalHouseSectionService {
    private final LocalHouseRepository localHouseRepository;
    private final LocalHouseSectionRepository localHouseSectionRepository;
    private final HouseSectionRepository houseSectionRepository;

    @Transactional
    public void upsertLocalHouseSection(@Valid UpsertLocalHouseSectionRequest request) {
        Optional<LocalHouseSection> _localHouseSection =
                localHouseSectionRepository.findByHouseSectionId(request.getHouseSectionId());

        if (_localHouseSection.isPresent()) {
            LocalHouseSection localHouseSection = _localHouseSection.get();
            updateLocalHouseSectionIfNecessary(localHouseSection, request);
        } else {
            createLocalHouseSection(request);
        }
    }

    private LocalHouseSection updateLocalHouseSectionIfNecessary(LocalHouseSection localHouseSection, UpsertLocalHouseSectionRequest request) {
        // 하우스동 버전이 db에 저장된 하우스동 버전보다 클 경우에만 변경
        if (localHouseSection.getHouseSectionVersion() <= request.getHouseSectionVersion()) {
            localHouseSection.changeSectionNumber(request.getSectionNumber());
            localHouseSection.changeHouseSectionVersion(request.getHouseSectionVersion());
            localHouseSection.changeCreatedAt(request.getCreatedAt());
            localHouseSection.changeUpdatedAt(request.getUpdatedAt());
            localHouseSection.changeDeleted(request.getDeleted());
            localHouseSection.changeDeletedAt(request.getDeletedAt());
            localHouseSection.changeLastUpdatedAt(Instant.now());
        } else {
            // 변경하지 않음, 필요시 로깅
            log.info("No changes made to LocalHouseSection with houseId: {}", request.getHouseId());
        }

        return localHouseSection;
    }

    private LocalHouseSection createLocalHouseSection(UpsertLocalHouseSectionRequest request) {
        HouseSection houseSection = houseSectionRepository.findById(request.getHouseSectionId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

        LocalHouse localHouse = localHouseRepository.findByHouseId(request.getHouseId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE));

        LocalHouseSection localHouseSection = LocalHouseSection.builder()
                .sectionNumber(request.getSectionNumber())
                .houseSection(houseSection)
                .localHouse(localHouse)
                .houseSectionVersion(request.getHouseSectionVersion())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .deleted(request.getDeleted())
                .deletedAt(request.getDeletedAt())
                .lastUpdatedAt(Instant.now())
                .build();

        return localHouseSectionRepository.save(localHouseSection);
    }
}
