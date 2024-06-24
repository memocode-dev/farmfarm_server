package dev.memocode.farmfarm_server.api.form;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseSectionSensorForm {
    @Schema(description = "관리자용 센서 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nameForAdmin;

    @Schema(description = "사용자용 센서 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nameForUser;

    @Schema(description = "센서 모델", requiredMode = Schema.RequiredMode.REQUIRED)
    private SensorModel sensorModel;

    @Schema(description = "포트 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String portName;
}
