CREATE SEQUENCE IF NOT EXISTS stadiums_id_seq;

CREATE TABLE IF NOT EXISTS stadiums
(
    id bigint NOT NULL DEFAULT nextval('stadiums_id_seq'::regclass),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    city character varying(100) COLLATE pg_catalog."default" NOT NULL,
    state character varying(100) COLLATE pg_catalog."default" NULL,
    country_id bigint not null,
    CONSTRAINT pk_stadiums PRIMARY KEY (id),
    CONSTRAINT uk_s_name_country UNIQUE (name, country_id),
    CONSTRAINT fk_stadiums_country_id FOREIGN KEY (country_id) REFERENCES countries (id)
);