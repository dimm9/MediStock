CREATE TYPE employee_role AS ENUM (
    'ADMINISTRATOR',
    'WAREHOUSE_WORKER',
    'DOCTOR',
    'NURSE'
);

CREATE TYPE stock_category AS ENUM (
    'BASIC_MEDICAL_EQUIPMENT',
    'SPECIALIZED_MEDICAL_EQUIPMENT',
    'TECHNICAL_EQUIPMENT',
    'MEDICINES',
    'LABORATORY_EQUIPMENT'
);

CREATE TABLE hospital (
                          id BIGSERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          address TEXT NOT NULL,
                          funds NUMERIC(14,2) NOT NULL CHECK (funds >= 0)
);

CREATE TABLE stock (
                       id BIGSERIAL PRIMARY KEY,
                       hospital_id BIGINT NOT NULL REFERENCES hospital(id) ON DELETE CASCADE,
                       name TEXT NOT NULL,
                       category stock_category NOT NULL
);

CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         stock_id BIGINT NOT NULL REFERENCES stock(id) ON DELETE CASCADE,
                         name TEXT NOT NULL,
                         type TEXT NOT NULL,
                         cost NUMERIC(12,2) NOT NULL CHECK (cost >= 0),
                         quantity INTEGER NOT NULL CHECK (quantity >= 0),
                         is_available BOOLEAN NOT NULL DEFAULT TRUE,
                         media_url TEXT
);

CREATE TABLE employee (
                          id BIGSERIAL PRIMARY KEY,
                          hospital_id BIGINT NOT NULL REFERENCES hospital(id) ON DELETE CASCADE,
                          name TEXT NOT NULL,
                          role employee_role NOT NULL,
                          salary NUMERIC(12,2) CHECK (salary >= 0),
                          login TEXT UNIQUE NOT NULL,
                          password_hash TEXT NOT NULL,
                          active BOOLEAN NOT NULL DEFAULT TRUE
);

ALTER TYPE employee_role ADD VALUE IF NOT EXISTS 'UNREGISTERED';

ALTER TABLE employee ALTER COLUMN hospital_id DROP NOT NULL;
ALTER TABLE employee ALTER COLUMN name DROP NOT NULL;
