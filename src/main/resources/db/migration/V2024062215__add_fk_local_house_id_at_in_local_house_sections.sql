ALTER TABLE local_house_sections
    ADD COLUMN local_house_id UUID;

ALTER TABLE local_house_sections
    ADD CONSTRAINT fk_local_house_sections__local_houses FOREIGN KEY (local_house_id) REFERENCES local_houses (id);