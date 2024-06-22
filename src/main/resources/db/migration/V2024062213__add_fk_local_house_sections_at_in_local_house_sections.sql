ALTER TABLE local_house_sections
    ADD CONSTRAINT fk_local_house_sections__house_sections
        FOREIGN KEY (house_section_id)
            REFERENCES house_sections (id);