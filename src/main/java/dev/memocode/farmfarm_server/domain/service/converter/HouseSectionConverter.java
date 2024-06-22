package dev.memocode.farmfarm_server.domain.service.converter;

import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import dev.memocode.farmfarm_server.domain.entity.HouseSectionSensor;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse__HouseSection;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse__HouseSectionSensor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HouseSectionConverter {

    public FindAllHouseSectionsResponse__HouseSection toFindAllHouseSectionsResponse__HouseSection(HouseSection houseSection) {
        List<HouseSectionSensor> houseSectionSensors = houseSection.getHouseSectionSensors();

        List<FindAllHouseSectionsResponse__HouseSectionSensor> sensors = houseSectionSensors.stream()
                .map(houseSectionSensor -> FindAllHouseSectionsResponse__HouseSectionSensor.builder()
                        .id(houseSectionSensor.getId())
                        .nameForAdmin(houseSectionSensor.getNameForAdmin())
                        .nameForUser(houseSectionSensor.getNameForUser())
                        .sensorModelInfo(houseSectionSensor.getSensorModel().getModelInfo())
                        .createdAt(houseSectionSensor.getCreatedAt())
                        .updatedAt(houseSectionSensor.getUpdatedAt())
                        .build())
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

    public List<FindAllHouseSectionsResponse__HouseSection> toFindAllHouseSectionsResponse__HouseSection(List<HouseSection> houseSections) {
        return houseSections.stream()
                .map(this::toFindAllHouseSectionsResponse__HouseSection)
                .toList();
    }
}
