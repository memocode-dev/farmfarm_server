package dev.memocode.farmfarm_server.domain.service;

import dev.memocode.farmfarm_server.domain.entity.HouseSectionSensor;
import dev.memocode.farmfarm_server.domain.entity.LocalHouseSection;
import dev.memocode.farmfarm_server.domain.entity.LocalHouseSectionSensor;
import dev.memocode.farmfarm_server.domain.exception.NotFoundException;
import dev.memocode.farmfarm_server.domain.repository.HouseSectionSensorRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionRepository;
import dev.memocode.farmfarm_server.domain.repository.LocalHouseSectionSensorRepository;
import dev.memocode.farmfarm_server.domain.service.request.UpsertLocalHouseSectionSensorRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static dev.memocode.farmfarm_server.domain.exception.HouseSectionErrorCode.NOT_FOUND_HOUSE_SECTION;
import static dev.memocode.farmfarm_server.domain.exception.HouseSectionSensorErrorCode.NOT_FOUND_HOUSE_SECTION_SENSOR;
import static dev.memocode.farmfarm_server.domain.exception.LocalHouseSectionErrorCode.NOT_FOUND_LOCAL_HOUSE_SECTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalHouseSectionSensorService {
    private final HouseSectionSensorRepository houseSectionSensorRepository;
    private final LocalHouseSectionSensorRepository localHouseSectionSensorRepository;
    private final LocalHouseSectionRepository localHouseSectionRepository;

    @Transactional
    public void upsertLocalHouseSectionSensor(@Valid UpsertLocalHouseSectionSensorRequest request) {
        Optional<LocalHouseSectionSensor> _localHouseSectionSensor =
                localHouseSectionSensorRepository.findByHouseSectionSensorId(request.getHouseSectionSensorId());

        if (_localHouseSectionSensor.isPresent()) {
            LocalHouseSectionSensor localHouseSectionSensor = _localHouseSectionSensor.get();
            updateLocalHouseSectionSensorIfNecessary(localHouseSectionSensor, request);
        } else {
            createLocalHouseSectionSensor(request);
        }
    }

    private LocalHouseSectionSensor updateLocalHouseSectionSensorIfNecessary(
            LocalHouseSectionSensor localHouseSectionSensor, UpsertLocalHouseSectionSensorRequest request) {
        // 하우스동 버전이 db에 저장된 하우스동 버전보다 클 경우에만 변경
        if (localHouseSectionSensor.getHouseSectionSensorVersion() <= request.getHouseSectionSensorVersion()) {
            LocalHouseSection localHouseSection = localHouseSectionRepository.findByHouseSectionId(request.getHouseSectionId())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION));

            HouseSectionSensor houseSectionSensor = houseSectionSensorRepository.findById(request.getHouseSectionSensorId())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

            localHouseSectionSensor.changeNameForAdmin(request.getNameForAdmin());
            localHouseSectionSensor.changeNameForUser(request.getNameForUser());
            localHouseSectionSensor.changeLocalHouseSection(localHouseSection);
            localHouseSectionSensor.changeHouseSectionSensor(houseSectionSensor);
            localHouseSectionSensor.changeHouseSectionSensorVersion(request.getHouseSectionSensorVersion());
            localHouseSectionSensor.changeCreatedAt(request.getCreatedAt());
            localHouseSectionSensor.changeUpdatedAt(request.getUpdatedAt());
            localHouseSectionSensor.changeDeleted(request.getDeleted());
            localHouseSectionSensor.changeDeletedAt(request.getDeletedAt());
            localHouseSectionSensor.changeLastUpdatedAt(Instant.now());
        } else {
            // 변경하지 않음, 필요시 로깅
            log.info("No changes made to LocalHouseSection with houseId: {}", request.getHouseId());
        }

        return localHouseSectionSensor;
    }

    private LocalHouseSectionSensor createLocalHouseSectionSensor(UpsertLocalHouseSectionSensorRequest request) {
        HouseSectionSensor houseSectionSensor = houseSectionSensorRepository.findById(request.getHouseSectionSensorId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_HOUSE_SECTION_SENSOR));

        LocalHouseSection localHouseSection = localHouseSectionRepository.findByHouseSectionId(request.getHouseSectionId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LOCAL_HOUSE_SECTION));

        LocalHouseSectionSensor localHouseSectionSensor = LocalHouseSectionSensor.builder()
                .nameForAdmin(request.getNameForAdmin())
                .nameForUser(request.getNameForUser())
                .sensorModel(request.getSensorModel())
                .houseSectionSensor(houseSectionSensor)
                .localHouseSection(localHouseSection)
                .houseSectionSensorVersion(request.getHouseSectionSensorVersion())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .deleted(request.getDeleted())
                .deletedAt(request.getDeletedAt())
                .lastUpdatedAt(Instant.now())
                .build();

        return localHouseSectionSensorRepository.save(localHouseSectionSensor);
    }
}
