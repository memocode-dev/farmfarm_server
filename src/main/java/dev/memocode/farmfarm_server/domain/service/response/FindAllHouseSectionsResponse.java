package dev.memocode.farmfarm_server.domain.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "하우스 동 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FindAllHouseSectionsResponse__HouseSection> houseSections;
}
