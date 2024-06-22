package dev.memocode.farmfarm_server.api.form;

import dev.memocode.farmfarm_server.domain.entity.SensorModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHouseSectionSensorForm {
    private String nameForAdmin;
    private String nameForUser;
    private SensorModel sensorModel;
}
