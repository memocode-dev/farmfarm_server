package dev.memocode.farmfarm_server.domain.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseSectionRequest {
    @NotNull(message = "HOUSE_ID_NOT_NULL:house id cannot be null")
    private UUID houseId;

    @NotNull(message = "HOUSE_SECTION_NUMBER_NOT_NULL:house section number cannot be null")
    private Integer sectionNumber;
}
