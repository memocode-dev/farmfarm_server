package dev.memocode.farmfarm_server.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseRequest {
    @NotBlank(message = "HOUSE_NAME_NOT_BLANK:Name cannot be blank")
    private String name;
}
