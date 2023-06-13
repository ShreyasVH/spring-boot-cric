CREATE SEQUENCE IF NOT EXISTS players_id_seq;

CREATE TABLE IF NOT EXISTS players
(
    id bigint NOT NULL DEFAULT nextval('players_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    country_id bigint NOT NULL,
    date_of_birth date NOT NULL,
    image character varying(255),
    CONSTRAINT pk_players PRIMARY KEY (id),
    CONSTRAINT uk_p_name_country_dob UNIQUE (name, country_id, date_of_birth),
    CONSTRAINT fk_players_country_id FOREIGN KEY (country_id) REFERENCES countries (id)
);