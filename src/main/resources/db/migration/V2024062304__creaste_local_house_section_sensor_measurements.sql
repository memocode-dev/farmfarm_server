CREATE TABLE local_house_section_sensor_measurements
(
    id                            UUID PRIMARY KEY,
    local_house_section_sensor_id UUID,
    measurement_type              TEXT                     NOT NULL,
    value                         DOUBLE PRECISION         NOT NULL,
    measured_at                   TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_local_house_section_sensor FOREIGN KEY (local_house_section_sensor_id) REFERENCES local_house_section_sensors (id)
);