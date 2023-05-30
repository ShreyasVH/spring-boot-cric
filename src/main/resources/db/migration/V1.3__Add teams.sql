CREATE SEQUENCE IF NOT EXISTS team_types_id_seq;

CREATE TABLE IF NOT EXISTS team_types
(
    id smallint NOT NULL DEFAULT nextval('team_types_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_team_types PRIMARY KEY (id),
    CONSTRAINT uk_tt_name UNIQUE (name)
);

INSERT INTO team_types (name) VALUES
    ('International'),
    ('Domestic'),
    ('Franchise');

CREATE SEQUENCE IF NOT EXISTS teams_id_seq;

CREATE TABLE IF NOT EXISTS teams
(
    id bigint NOT NULL DEFAULT nextval('teams_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    country_id bigint not null,
    type_id smallint not null,
    CONSTRAINT pk_teams PRIMARY KEY (id),
    CONSTRAINT uk_t_name_country_type UNIQUE (name, country_id, type_id),
    CONSTRAINT fk_teams_country_id FOREIGN KEY (country_id) REFERENCES countries (id)  on DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_teams_type_id FOREIGN KEY (type_id) REFERENCES team_types (id)  on DELETE RESTRICT ON UPDATE RESTRICT
);