package dev.memocode.farmfarm_server.domain.service.request;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import jakarta.validation.constraints.NotBlank;
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
public class CreateHouseSectionSensorRequest {
    @NotNull
    private UUID houseId;

    @NotNull
    private UUID houseSectionId;

    @NotBlank
    private String nameForAdmin;

    @NotBlank
    private String nameForUser;

    @NotNull
    private SensorModel sensorModel;

    @NotBlank
    private String portName;
}
