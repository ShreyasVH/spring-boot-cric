CREATE SEQUENCE IF NOT EXISTS tours_id_seq;

CREATE TABLE IF NOT EXISTS tours
(
    id bigint NOT NULL DEFAULT nextval('tours_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    start_time timestamp NOT NULL,
    CONSTRAINT pk_tours PRIMARY KEY (id),
    CONSTRAINT uk_t_name_start_time UNIQUE (name, start_time)
);