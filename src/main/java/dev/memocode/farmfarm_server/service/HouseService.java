package dev.memocode.farmfarm_server.service;

import dev.memocode.farmfarm_server.domain.House;
import dev.memocode.farmfarm_server.repository.HouseRepository;
import dev.memocode.farmfarm_server.service.converter.HouseConverter;
import dev.memocode.farmfarm_server.service.request.CreateHouseRequest;
import dev.memocode.farmfarm_server.service.response.FindAllHousesResponse;
import dev.memocode.farmfarm_server.service.response.FindAllHousesResponse__House;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseConverter houseConverter;

    public UUID createHouse(CreateHouseRequest createHouseRequest) {
        House house = House.builder()
                .name(createHouseRequest.getName())
                .build();

        houseRepository.save(house);

        return house.getId();
    }

    public FindAllHousesResponse findAllHouses() {
        List<House> houses = houseRepository.findAll();

        return FindAllHousesResponse.builder()
                .houses(houseConverter.toFindAllHousesResponse__House(houses))
                .build();
    }
}
