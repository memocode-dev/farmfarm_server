package dev.memocode.farmfarm_server.api.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseForm {
    @Schema(description = "하우스 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
