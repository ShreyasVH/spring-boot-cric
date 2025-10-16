ALTER TABLE series_teams_map DROP CONSTRAINT fk_series_teams_map_series;
ALTER TABLE man_of_the_series DROP CONSTRAINT fk_mots_series;
ALTER TABLE matches DROP CONSTRAINT fk_m_series;

ALTER TABLE series ALTER COLUMN id TYPE INTEGER USING id::integer;

ALTER TABLE series_teams_map ALTER COLUMN series_id TYPE INTEGER USING series_id::integer;
ALTER TABLE man_of_the_series ALTER COLUMN series_id TYPE INTEGER USING series_id::integer;
ALTER TABLE matches ALTER COLUMN series_id TYPE INTEGER USING series_id::integer;

ALTER TABLE series_teams_map ADD CONSTRAINT fk_series_teams_map_series FOREIGN KEY (series_id) REFERENCES series(id);
ALTER TABLE man_of_the_series ADD CONSTRAINT fk_mots_series FOREIGN KEY (series_id) REFERENCES series(id);
ALTER TABLE matches ADD CONSTRAINT fk_m_series FOREIGN KEY (series_id) REFERENCES series(id);

CREATE TABLE IF NOT EXISTS tags
(
    id smallserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id),
    CONSTRAINT uk_ta_name UNIQUE (name)
);

INSERT INTO tags (name) VALUES
('WORLD_CUP'),
('IPL'),
('CHAMPIONS_TROPHY'),
('BBL'),
('ILT20'),
('CHAMPIONS_LEAGUE'),
('ASIA_CUP'),
('WTC');

CREATE TABLE IF NOT EXISTS tags_map
(
    id bigserial NOT NULL,
    entity_type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    entity_id integer NOT NULL,
    tag_id smallint NOT NULL,
    CONSTRAINT pk_tags_map PRIMARY KEY (id),
    CONSTRAINT uk_tm_type_id_tag UNIQUE (entity_type, entity_id, tag_id),
    CONSTRAINT fk_tm_tag FOREIGN KEY (tag_id) REFERENCES tags (id)  on DELETE RESTRICT ON UPDATE RESTRICT
);