package dev.memocode.farmfarm_server.domain.service.converter;

import dev.memocode.farmfarm_server.domain.entity.House;
import dev.memocode.farmfarm_server.domain.service.response.FindAllHousesResponse__House;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HouseConverter {

    public FindAllHousesResponse__House toFindAllHousesResponse__House(House house) {
        return FindAllHousesResponse__House.builder()
                .id(house.getId())
                .name(house.getName())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .status(house.getStatus())
                .build();
    }

    public List<FindAllHousesResponse__House> toFindAllHousesResponse__House(List<House> houses) {
        return houses.stream()
                .map(this::toFindAllHousesResponse__House)
                .toList();
    }
}
