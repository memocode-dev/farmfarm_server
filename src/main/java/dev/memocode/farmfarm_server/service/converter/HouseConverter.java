package dev.memocode.farmfarm_server.service.converter;

import dev.memocode.farmfarm_server.domain.House;
import dev.memocode.farmfarm_server.service.response.FindAllHousesResponse__House;
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
                .build();
    }

    public List<FindAllHousesResponse__House> toFindAllHousesResponse__House(List<House> houses) {
        return houses.stream()
                .map(this::toFindAllHousesResponse__House)
                .toList();
    }
}
