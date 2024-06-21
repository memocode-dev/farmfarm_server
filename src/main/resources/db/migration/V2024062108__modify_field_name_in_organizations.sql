ALTER TABLE organizations
    ADD CONSTRAINT organization_name_unique UNIQUE (name);