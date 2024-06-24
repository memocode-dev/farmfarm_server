package dev.memocode.farmfarm_server.api.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationForm {
    @Schema(description = "조직 이름")
    private String name;
}
