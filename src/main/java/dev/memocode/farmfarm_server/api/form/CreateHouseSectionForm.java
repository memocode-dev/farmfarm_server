package dev.memocode.farmfarm_server.api.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseSectionForm {
    @Schema(description = "하우스동 번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sectionNumber;
}
