package dev.memocode.farmfarm_server.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SensorModelInfo {
    private String name;
    private String description;
    private List<MeasurementDetails> measurementDetails;

}
