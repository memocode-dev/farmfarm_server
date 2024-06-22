package dev.memocode.farmfarm_server.domain.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHouseSectionsResponse {
    private List<FindAllHouseSectionsResponse__HouseSection> houseSections;
}
