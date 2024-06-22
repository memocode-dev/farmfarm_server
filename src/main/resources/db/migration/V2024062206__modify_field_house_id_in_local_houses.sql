ALTER TABLE local_houses
    ADD CONSTRAINT fk_local_houses_house FOREIGN KEY (house_id) REFERENCES houses(id);