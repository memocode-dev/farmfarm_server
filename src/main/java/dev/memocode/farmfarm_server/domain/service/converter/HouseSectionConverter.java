package dev.memocode.farmfarm_server.domain.service.converter;

import dev.memocode.farmfarm_server.domain.entity.HouseSection;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHouseSectionsResponse__HouseSection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HouseSectionConverter {

    public FindAllHouseSectionsResponse__HouseSection toFindAllHouseSectionsResponse__HouseSection(HouseSection houseSection) {
        return FindAllHouseSectionsResponse__HouseSection.builder()
                .id(houseSection.getId())
                .sectionNumber(houseSection.getSectionNumber())
                .createdAt(houseSection.getCreatedAt())
                .updatedAt(houseSection.getUpdatedAt())
                .syncStatus(houseSection.getSyncStatus())
                .build();
    }

    public List<FindAllHouseSectionsResponse__HouseSection> toFindAllHouseSectionsResponse__HouseSection(List<HouseSection> houseSections) {
        return houseSections.stream()
                .map(this::toFindAllHouseSectionsResponse__HouseSection)
                .toList();
    }
}
