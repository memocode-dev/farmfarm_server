ALTER TABLE local_house_section_sensor_measurements
    ADD CONSTRAINT unique_local_house_section_sensor_measurements__measurement
        UNIQUE (local_house_section_sensor_id, measurement_type, measured_at);