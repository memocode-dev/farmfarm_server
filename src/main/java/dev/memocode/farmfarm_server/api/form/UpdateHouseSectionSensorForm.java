package dev.memocode.farmfarm_server.api.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHouseSectionSensorForm {
    @Schema(description = "관리자용 센서 이름")
    private String nameForAdmin;

    @Schema(description = "사용자용 센서 이름")
    private String nameForUser;

    @Schema(description = "포트 이름")
    private String portName;
}
