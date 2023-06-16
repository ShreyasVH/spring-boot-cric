CREATE SEQUENCE IF NOT EXISTS series_types_id_seq;

CREATE TABLE IF NOT EXISTS series_types
(
    id smallint NOT NULL DEFAULT nextval('series_types_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_series_types PRIMARY KEY (id),
    CONSTRAINT uk_st_name UNIQUE (name)
);

INSERT INTO series_types (name) VALUES
    ('Bilateral'),
    ('Tri series'),
    ('Tournament');

CREATE SEQUENCE IF NOT EXISTS game_types_id_seq;
CREATE TABLE IF NOT EXISTS game_types
(
    id smallint NOT NULL DEFAULT nextval('game_types_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_game_types PRIMARY KEY (id),
    CONSTRAINT uk_gt_name UNIQUE (name)
);

INSERT INTO game_types (name) VALUES
    ('ODI'),
    ('Test'),
    ('T20');

CREATE SEQUENCE IF NOT EXISTS series_id_seq;
CREATE TABLE IF NOT EXISTS series
(
    id bigint NOT NULL DEFAULT nextval('series_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    home_country_id bigint not null,
    tour_id bigint not null,
    type_id smallint not null,
    game_type_id smallint not null,
    start_time timestamp NOT NULL,
    CONSTRAINT pk_series PRIMARY KEY (id),
    CONSTRAINT uk_s_name_tour_game_type UNIQUE (name, tour_id, game_type_id),
    CONSTRAINT fk_series_home_country FOREIGN KEY (home_country_id) REFERENCES countries (id)  on DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_series_tour FOREIGN KEY (tour_id) REFERENCES tours (id)  on DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_series_type FOREIGN KEY (type_id) REFERENCES series_types (id)  on DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_series_game_type FOREIGN KEY (game_type_id) REFERENCES game_types (id)  on DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE SEQUENCE IF NOT EXISTS series_teams_map_id_seq;
CREATE TABLE IF NOT EXISTS series_teams_map
(
    id bigint NOT NULL DEFAULT nextval('series_teams_map_id_seq'::regclass),
    series_id bigint not null,
    team_id bigint not null,
    CONSTRAINT pk_series_teams_map PRIMARY KEY (id),
    CONSTRAINT uk_stm_series_team UNIQUE (series_id, team_id),
    CONSTRAINT fk_series_teams_map_series FOREIGN KEY (series_id) REFERENCES series (id)  on DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_series_teams_map_team FOREIGN KEY (team_id) REFERENCES teams (id)  on DELETE RESTRICT ON UPDATE RESTRICT
);