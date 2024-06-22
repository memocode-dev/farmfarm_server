CREATE TABLE house_sections
(
    id             UUID PRIMARY KEY,
    section_number INT                      NOT NULL,
    version        BIGINT                   NOT NULL,
    house_id       UUID                     NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted        BOOLEAN                  NOT NULL,
    deleted_at     TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_house_sections_house FOREIGN KEY (house_id) REFERENCES houses (id)
);