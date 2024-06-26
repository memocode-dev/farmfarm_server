CREATE TABLE local_house_section_sensors
(
    id                           UUID PRIMARY KEY,
    name_for_admin               TEXT                     NOT NULL,
    name_for_user                TEXT                     NOT NULL,
    sensor_model                 TEXT                     NOT NULL,
    version                      BIGINT                   NOT NULL,
    house_section_sensor_version BIGINT                   NOT NULL,
    house_section_sensor_id      UUID                     NOT NULL,
    local_house_section_id       UUID                     NOT NULL,
    created_at                   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at                   TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted                      BOOLEAN                  NOT NULL,
    deleted_at                   TIMESTAMP WITH TIME ZONE,
    last_updated_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_local_house_section_sensors__local_house_sections FOREIGN KEY (local_house_section_id) REFERENCES local_house_sections (id),
    CONSTRAINT fk_local_house_section_sensors__house_section_sensors FOREIGN KEY (house_section_sensor_id) REFERENCES house_section_sensors (id)
);