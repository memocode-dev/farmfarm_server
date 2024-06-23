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
public class UpdateHouseSectionSensorRequest {
    @NotNull
    private UUID houseId;

    @NotNull
    private UUID houseSectionId;

    @NotNull
    private UUID houseSectionSensorId;

    private String nameForAdmin;

    private String nameForUser;

    private String portName;
}
