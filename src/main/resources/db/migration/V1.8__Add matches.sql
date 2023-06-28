CREATE TABLE IF NOT EXISTS result_types
(
    id smallserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_result_types PRIMARY KEY (id),
    CONSTRAINT uk_rt_name UNIQUE (name)
);

INSERT INTO result_types (name) VALUES
('Normal'),
('Tie'),
('Draw'),
('Super Over'),
('Washed Out'),
('Bowl Out'),
('Forfeit');

CREATE TABLE win_margin_types
(
    id   smallserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_win_margin_types PRIMARY KEY (id),
    CONSTRAINT uk_wmt_name UNIQUE (name)
);

INSERT INTO win_margin_types (name) VALUES
('Run'),
('Wicket');

CREATE TABLE matches (
     id                            serial NOT NULL,
     series_id                     bigint  NOT NULL,
     team_1_id                     bigint  NOT NULL,
     team_2_id                     bigint  NOT NULL,
     toss_winner_id                bigint  DEFAULT NULL,
     bat_first_id                  bigint  DEFAULT NULL,
     result_type_id                smallint  NOT NULL,
     winner_id                     bigint  DEFAULT NULL,
     win_margin                    smallint  DEFAULT NULL,
     win_margin_type_id            smallint  DEFAULT NULL,
     stadium_id                    bigint  NOT NULL,
     start_time                    timestamp NOT NULL,
     is_official                   boolean NOT NULL default true,
     PRIMARY KEY (id),
     CONSTRAINT uk_m_stadium_start UNIQUE (stadium_id, start_time),
     CONSTRAINT fk_m_series FOREIGN KEY (series_id) REFERENCES series (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_team_1 FOREIGN KEY (team_1_id) REFERENCES teams (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_team_2 FOREIGN KEY (team_2_id) REFERENCES teams (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_toss_winner FOREIGN KEY (toss_winner_id) REFERENCES teams (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_bat_first FOREIGN KEY (bat_first_id) REFERENCES teams (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_result_type FOREIGN KEY (result_type_id) REFERENCES result_types (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_winner FOREIGN KEY (winner_id) REFERENCES teams (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_win_margin_type FOREIGN KEY (win_margin_type_id) REFERENCES win_margin_types (id) on DELETE RESTRICT ON UPDATE RESTRICT,
     CONSTRAINT fk_m_stadium FOREIGN KEY (stadium_id) REFERENCES stadiums (id) on DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE match_player_map (
      id                            serial NOT NULL,
      match_id                      int NOT NULL,
      player_id                     bigint  NOT NULL,
      team_id                       bigint  NOT NULL,
      PRIMARY KEY (id),
      CONSTRAINT uk_mpm_match_player_team UNIQUE (match_id, player_id, team_id),
      CONSTRAINT fk_mpm_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
      CONSTRAINT fk_mpm_player FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
      CONSTRAINT fk_mpm_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE dismissal_modes (
     id                            smallserial NOT NULL,
     name                          character varying(30),
     PRIMARY KEY (id),
     CONSTRAINT uk_dm_name UNIQUE (name)
);

INSERT INTO dismissal_modes (name) VALUES
('Bowled'),
('Caught'),
('LBW'),
('Run Out'),
('Stumped'),
('Hit Twice'),
('Hit Wicket'),
('Obstructing the Field'),
('Timed Out'),
('Retired Hurt'),
('Handled the Ball');

CREATE TABLE batting_scores (
    id                            serial NOT NULL,
    match_player_id               int NOT NULL,
    runs                          smallint NOT NULL DEFAULT '0',
    balls                         smallint NOT NULL DEFAULT '0',
    fours                         smallint NOT NULL DEFAULT '0',
    sixes                         smallint NOT NULL DEFAULT '0',
    dismissal_mode_id             smallint DEFAULT NULL,
    bowler_id                     int DEFAULT NULL,
    innings                       smallint NOT NULL,
    number                        smallint DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_bs_match_player_innings UNIQUE (match_player_id, innings),
    CONSTRAINT fk_bs_match_player FOREIGN KEY (match_player_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_bs_dismissal_mode FOREIGN KEY (dismissal_mode_id) REFERENCES dismissal_modes (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_bs_bowler FOREIGN KEY (bowler_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE bowling_figures (
     id                            serial NOT NULL,
     match_player_id               int NOT NULL,
     balls                         smallint DEFAULT '0',
     maidens                       smallint DEFAULT '0',
     runs                          smallint DEFAULT '0',
     wickets                       smallint DEFAULT '0',
     innings                    smallint NOT NULL,
     PRIMARY KEY (id),
     CONSTRAINT uk_bf_match_player_innings UNIQUE (match_player_id, innings),
     CONSTRAINT fk_bf_match_player FOREIGN KEY (match_player_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

create table fielder_dismissals (
    id                            smallserial NOT NULL,
    score_id                      smallint NOT NULL,
    match_player_id               smallint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_fd_score_player_team UNIQUE (score_id, match_player_id),
    CONSTRAINT fk_fd_score FOREIGN KEY (score_id) REFERENCES batting_scores (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_fd_match_player FOREIGN KEY (match_player_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE extras_types (
    id                            smallserial NOT NULL,
    name                          character varying(100),
    PRIMARY KEY (id),
    CONSTRAINT uk_et_name UNIQUE (name)
);

INSERT INTO extras_types (name) VALUES
('Bye'),
('Leg Bye'),
('Wide'),
('No Ball'),
('Penalty');

CREATE TABLE extras (
    id                            smallserial NOT NULL,
    match_id                      smallint NOT NULL,
    type_id                       smallint NOT NULL,
    runs                          smallint NOT NULL,
    batting_team_id               bigint NOT NULL,
    bowling_team_id               bigint NOT NULL,
    innings                    smallint not null,
    PRIMARY KEY (id),
    CONSTRAINT uk_e_match_type_batting_innings UNIQUE (match_id, type_id, batting_team_id, innings),
    CONSTRAINT fk_e_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_e_type FOREIGN KEY (type_id) REFERENCES extras_types (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_e_batting_team FOREIGN KEY (batting_team_id) REFERENCES teams (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_e_bowling_team FOREIGN KEY (bowling_team_id) REFERENCES teams (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE captains (
    id smallserial NOT NULL,
    match_player_id smallint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_c_match_player UNIQUE (match_player_id),
    CONSTRAINT fk_c_match_player FOREIGN KEY (match_player_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE wicket_keepers (
    id smallserial NOT NULL,
    match_player_id smallint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_wk_match_player UNIQUE (match_player_id),
    CONSTRAINT fk_wk_match_player FOREIGN KEY (match_player_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE man_of_the_match (
    id smallserial NOT NULL,
    match_player_id smallint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_motm_match_player UNIQUE (match_player_id),
    CONSTRAINT fk_motm_match_player FOREIGN KEY (match_player_id) REFERENCES match_player_map (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);