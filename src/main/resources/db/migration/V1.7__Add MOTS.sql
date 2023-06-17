CREATE SEQUENCE IF NOT EXISTS man_of_the_series_id_seq;

CREATE TABLE IF NOT EXISTS man_of_the_series
(
    id bigint NOT NULL DEFAULT nextval('man_of_the_series_id_seq'::regclass),
    series_id bigint not null,
    player_id bigint not null,
    CONSTRAINT pk_man_of_the_series PRIMARY KEY (id),
    CONSTRAINT uk_mots_series_player UNIQUE (series_id, player_id),
    CONSTRAINT fk_mots_series FOREIGN KEY (series_id) REFERENCES series (id)  on DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_mots_player FOREIGN KEY (player_id) REFERENCES players (id)  on DELETE RESTRICT ON UPDATE RESTRICT
);