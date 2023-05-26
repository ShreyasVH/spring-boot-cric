CREATE SEQUENCE IF NOT EXISTS countries_id_seq;

CREATE TABLE IF NOT EXISTS countries
(
    id bigint NOT NULL DEFAULT nextval('countries_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_countries PRIMARY KEY (id)
);