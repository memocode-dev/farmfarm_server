package dev.memocode.farmfarm_server.api.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHouseSectionSensorForm {
    private String nameForAdmin;
    private String nameForUser;
    private String portName;
}
