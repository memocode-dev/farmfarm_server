-- Step 1: organization_id 컬럼 추가
ALTER TABLE houses
    ADD COLUMN organization_id UUID;

-- Step 2: 외래 키 제약 조건 추가
ALTER TABLE houses
    ADD CONSTRAINT fk_organization_id
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id);