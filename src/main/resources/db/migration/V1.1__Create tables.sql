CREATE SEQUENCE public.tours_id_seq;

CREATE TABLE public.tours
(
    id bigint NOT NULL DEFAULT nextval('tours_id_seq'::regclass),
    name character varying(100) NOT NULL,
    start_time bigint NOT NULL,
    CONSTRAINT tours_pkey PRIMARY KEY (id),
    CONSTRAINT uk_t_name UNIQUE (name, start_time)
);

CREATE SEQUENCE public.countries_id_seq;

CREATE TABLE public.countries
(
    id bigint NOT NULL DEFAULT nextval('countries_id_seq'::regclass),
    name character varying(100) NOT NULL,
    CONSTRAINT countries_pkey PRIMARY KEY (id),
    CONSTRAINT uk_c_name UNIQUE (name)
);

CREATE SEQUENCE public.stadiums_id_seq;

CREATE TABLE public.stadiums
(
    id bigint NOT NULL DEFAULT nextval('stadiums_id_seq'::regclass),
    name character varying(200) NOT NULL,
    city character varying(100) DEFAULT NULL,
    state character varying(100) DEFAULT NULL,
    country_id bigint NOT NULL,
    CONSTRAINT stadiums_pkey PRIMARY KEY (id),
    CONSTRAINT uk_s_name_country UNIQUE (name, country_id),
    CONSTRAINT fk_stadiums_country_id FOREIGN KEY(country_id) references countries (id)
);

CREATE SEQUENCE public.team_types_id_seq;

CREATE TABLE public.team_types
(
    id int NOT NULL DEFAULT nextval('team_types_id_seq'::regclass),
    name character varying(50) NOT NULL,
    CONSTRAINT team_types_pkey PRIMARY KEY (id),
    CONSTRAINT uk_tt_name UNIQUE (name)
);

INSERT INTO public.team_types (name) VALUES ('INTERNATIONAL');
INSERT INTO public.team_types (name) VALUES ('DOMESTIC');
INSERT INTO public.team_types (name) VALUES ('FRANCHISE');

CREATE SEQUENCE public.teams_id_seq;

CREATE TABLE public.teams
(
    id bigint NOT NULL DEFAULT nextval('teams_id_seq'::regclass),
    name character varying(100) NOT NULL,
    country_id bigint NOT NULL,
    team_type_id int NOT NULL,
    CONSTRAINT teams_pkey PRIMARY KEY (id),
    CONSTRAINT uk_t_name_country_type UNIQUE (name, country_id, team_type_id),
    CONSTRAINT fk_teams_country_id FOREIGN KEY(country_id) references countries (id)
--     CONSTRAINT fk_teams_team_type_id FOREIGN KEY(team_type_id) references team_types (id)
);

CREATE SEQUENCE public.series_types_id_seq;

CREATE TABLE public.series_types
(
    id int NOT NULL DEFAULT nextval('series_types_id_seq'::regclass),
    name character varying(20) NOT NULL,
    CONSTRAINT series_types_pkey PRIMARY KEY (id),
    CONSTRAINT uk_st_name UNIQUE (name)
);

INSERT INTO public.series_types (name) VALUES ('BI_LATERAL');
INSERT INTO public.series_types (name) VALUES ('TRI_SERIES');
INSERT INTO public.series_types (name) VALUES ('TOURNAMENT');

CREATE SEQUENCE public.game_types_id_seq;

CREATE TABLE public.game_types
(
    id int NOT NULL DEFAULT nextval('game_types_id_seq'::regclass),
    name character varying(20) NOT NULL,
    CONSTRAINT game_types_pkey PRIMARY KEY (id),
    CONSTRAINT uk_gt_name UNIQUE (name)
);

INSERT INTO public.game_types (name) VALUES ('ODI');
INSERT INTO public.game_types (name) VALUES ('TEST');
INSERT INTO public.game_types (name) VALUES ('T20');

CREATE SEQUENCE public.series_id_seq;

CREATE TABLE public.series
(
    id bigint NOT NULL DEFAULT nextval('series_id_seq'::regclass),
    name character varying(100) NOT NULL,
    home_country_id bigint NOT NULL,
    tour_id bigint NOT NULL,
    type int NOT NULL,
    game_type int NOT NULL,
    start_time bigint NOT NULL,
    CONSTRAINT series_pkey PRIMARY KEY (id),
    CONSTRAINT uk_s_name_tour_game_type UNIQUE (name, tour_id, game_type),
    CONSTRAINT fk_series_tour_id FOREIGN KEY(tour_id) references tours (id),
    CONSTRAINT fk_series_home_country_id FOREIGN KEY(home_country_id) references countries (id)
);

CREATE SEQUENCE public.series_teams_map_id_seq;

CREATE TABLE public.series_teams_map
(
    id bigint NOT NULL DEFAULT nextval('series_teams_map_id_seq'::regclass),
    series_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT series_teams_map_pkey PRIMARY KEY (id),
    CONSTRAINT uk UNIQUE (series_id, team_id),
    CONSTRAINT fk_series_teams_series_id FOREIGN KEY(series_id) references series (id),
    CONSTRAINT fk_series_teams_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.players_id_seq;

CREATE TABLE public.players
(
    id bigint NOT NULL DEFAULT nextval('players_id_seq'::regclass),
    name character varying(50) NOT NULL,
    country_id bigint NOT NULL,
    image character varying(255) NOT NULL,
    date_of_birth bigint NULL DEFAULT NULL,
    CONSTRAINT players_pkey PRIMARY KEY (id),
    CONSTRAINT uk_p_name_country_dob UNIQUE (country_id, name, date_of_birth),
    CONSTRAINT fk_players_country_id FOREIGN KEY(country_id) references countries (id)
);

CREATE SEQUENCE public.result_types_id_seq;

CREATE TABLE public.result_types
(
    id int NOT NULL DEFAULT nextval('result_types_id_seq'::regclass),
    name character varying(20) NOT NULL,
    CONSTRAINT result_types_pkey PRIMARY KEY (id),
    CONSTRAINT uk_rt_name UNIQUE (name)
);

INSERT INTO public.result_types (name) VALUES ('NORMAL');
INSERT INTO public.result_types (name) VALUES ('TIE');
INSERT INTO public.result_types (name) VALUES ('DRAW');
INSERT INTO public.result_types (name) VALUES ('SUPER_OVER');
INSERT INTO public.result_types (name) VALUES ('WASHED_OUT');
INSERT INTO public.result_types (name) VALUES ('BOWL_OUT');
INSERT INTO public.result_types (name) VALUES ('FORFEIT');

CREATE SEQUENCE public.win_margin_types_id_seq;

CREATE TABLE public.win_margin_types
(
    id int NOT NULL DEFAULT nextval('win_margin_types_id_seq'::regclass),
    name character varying(20) NOT NULL,
    CONSTRAINT win_margin_types_pkey PRIMARY KEY (id),
    CONSTRAINT uk_wmt_name UNIQUE (name)
);

INSERT INTO public.win_margin_types (name) VALUES ('RUN');
INSERT INTO public.win_margin_types (name) VALUES ('WICKET');

CREATE SEQUENCE public.matches_id_seq;

CREATE TABLE public.matches
(
    id bigint NOT NULL DEFAULT nextval('matches_id_seq'::regclass),
    series bigint NOT NULL,
    team_1 bigint NOT NULL,
    team_2 bigint NOT NULL,
    toss_winner bigint DEFAULT NULL,
    bat_first bigint DEFAULT NULL,
    result int NOT NULL,
    winner bigint DEFAULT NULL,
    win_margin int DEFAULT NULL,
    win_margin_type int DEFAULT NULL,
    stadium bigint NOT NULL,
    start_time bigint NOT NULL,
    tag character varying(20),
    is_official boolean NOT NULL DEFAULT false,
    CONSTRAINT matches_pkey PRIMARY KEY (id),
    CONSTRAINT uk_m_stadium_start UNIQUE (stadium, start_time),
    CONSTRAINT fk_matches_series_id FOREIGN KEY(series) references series (id),
    CONSTRAINT fk_matches_team_1 FOREIGN KEY(team_1) references teams (id),
    CONSTRAINT fk_matches_team_2 FOREIGN KEY(team_2) references teams (id),
    CONSTRAINT fk_matches_toss_winner FOREIGN KEY(toss_winner) references teams (id),
    CONSTRAINT fk_matches_bat_first FOREIGN KEY(bat_first) references teams (id),
    CONSTRAINT fk_matches_winner FOREIGN KEY(winner) references teams (id),
    CONSTRAINT fk_matches_stadium FOREIGN KEY(stadium) references stadiums (id)
--     CONSTRAINT fk_matches_result_type FOREIGN KEY(result_type_id) references result_types (id),
--     CONSTRAINT fk_matches_win_margin_type FOREIGN KEY(win_margin_type_id) references win_margin_types (id)
);

CREATE SEQUENCE public.match_player_map_id_seq;

CREATE TABLE public.match_player_map
(
    id bigint NOT NULL DEFAULT nextval('match_player_map_id_seq'::regclass),
    match_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT match_player_map_pkey PRIMARY KEY (id),
    CONSTRAINT uk_mpm_match_player_team UNIQUE (match_id, player_id, team_id),
    CONSTRAINT fk_match_player_map_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_match_player_map_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_match_player_map_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.dismissal_modes_id_seq;

CREATE TABLE public.dismissal_modes
(
    id int NOT NULL DEFAULT nextval('dismissal_modes_id_seq'::regclass),
    name character varying(30) NOT NULL,
    CONSTRAINT dismissal_modes_pkey PRIMARY KEY (id),
    CONSTRAINT uk_dm_name UNIQUE (name)
);

INSERT INTO public.dismissal_modes (name) VALUES ('Bowled');
INSERT INTO public.dismissal_modes (name) VALUES ('Caught');
INSERT INTO public.dismissal_modes (name) VALUES ('LBW');
INSERT INTO public.dismissal_modes (name) VALUES ('Run Out');
INSERT INTO public.dismissal_modes (name) VALUES ('Stumped');
INSERT INTO public.dismissal_modes (name) VALUES ('Hit Twice');
INSERT INTO public.dismissal_modes (name) VALUES ('Hit Wicket');
INSERT INTO public.dismissal_modes (name) VALUES ('Obstructing the Field');
INSERT INTO public.dismissal_modes (name) VALUES ('Timed Out');
INSERT INTO public.dismissal_modes (name) VALUES ('Retired Hurt');
INSERT INTO public.dismissal_modes (name) VALUES ('Handled the Ball');

CREATE SEQUENCE public.bowler_dismissals_id_seq;

CREATE TABLE public.bowler_dismissals
(
    id bigint NOT NULL DEFAULT nextval('bowler_dismissals_id_seq'::regclass),
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT bowler_dismissals_pkey PRIMARY KEY (id),
    CONSTRAINT fk_bowler_dismissals_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_bowler_dismissals_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.batting_scores_id_seq;

CREATE TABLE public.batting_scores
(
    id bigint NOT NULL DEFAULT nextval('batting_scores_id_seq'::regclass),
    match_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    runs int NOT NULL DEFAULT 0,
    balls int NOT NULL DEFAULT 0,
    fours int NOT NULL DEFAULT 0,
    sixes int NOT NULL DEFAULT 0,
    mode_of_dismissal_id int DEFAULT NULL,
    bowler_id bigint DEFAULT NULL,
    innings_id int NOT NULL,
    team_innings_id int NOT NULL,
    CONSTRAINT batting_scores_pkey PRIMARY KEY (id),
    CONSTRAINT uk_bs_match_player_team_innings UNIQUE (match_id, player_id, team_id, innings_id),
    CONSTRAINT fk_batting_scores_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_batting_scores_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_batting_scores_team_id FOREIGN KEY(team_id) references teams (id),
    CONSTRAINT fk_batting_scores_dismissal_mode FOREIGN KEY(mode_of_dismissal_id) references dismissal_modes (id),
    CONSTRAINT fk_batting_scores_bowler_id FOREIGN KEY(bowler_id) references bowler_dismissals (id)
);

CREATE SEQUENCE public.bowling_figures_id_seq;

CREATE TABLE public.bowling_figures
(
    id bigint NOT NULL DEFAULT nextval('bowling_figures_id_seq'::regclass),
    match_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    balls int DEFAULT 0,
    maidens int DEFAULT 0,
    runs int DEFAULT 0,
    wickets int DEFAULT 0,
    innings_id int NOT NULL,
    team_innings_id int NOT NULL,
    CONSTRAINT bowling_figures_pkey PRIMARY KEY (id),
    CONSTRAINT uk_bf_match_player_team_innings UNIQUE (match_id, player_id, team_id, innings_id),
    CONSTRAINT fk_bowling_figures_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_bowling_figures_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_bowling_figures_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.fielder_dismissals_id_seq;

CREATE TABLE public.fielder_dismissals
(
    id bigint NOT NULL DEFAULT nextval('fielder_dismissals_id_seq'::regclass),
    score_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT fielder_dismissals_pkey PRIMARY KEY (id),
    CONSTRAINT uk_fd_score_player_team UNIQUE (score_id, player_id, team_id),
    CONSTRAINT fk_fielder_dismissals_score_id FOREIGN KEY(score_id) references batting_scores (id),
    CONSTRAINT fk_fielder_dismissals_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_fielder_dismissals_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.extras_types_id_seq;

CREATE TABLE public.extras_types
(
    id int NOT NULL DEFAULT nextval('extras_types_id_seq'::regclass),
    name character varying(20) NOT NULL,
    CONSTRAINT extras_types_pkey PRIMARY KEY (id),
    CONSTRAINT uk_et_name UNIQUE (name)
);

INSERT INTO public.extras_types (name) VALUES ('BYE');
INSERT INTO public.extras_types (name) VALUES ('LEG_BYE');
INSERT INTO public.extras_types (name) VALUES ('WIDE');
INSERT INTO public.extras_types (name) VALUES ('NO_BALL');
INSERT INTO public.extras_types (name) VALUES ('PENALTY');

CREATE SEQUENCE public.extras_id_seq;

CREATE TABLE public.extras
(
    id bigint NOT NULL DEFAULT nextval('extras_id_seq'::regclass),
    match_id bigint NOT NULL,
    type int NOT NULL,
    runs int NOT NULL,
    batting_team bigint NOT NULL,
    bowling_team bigint NOT NULL,
    innings_id int NOT NULL,
    team_innings_id int NOT NULL,
    CONSTRAINT extras_pkey PRIMARY KEY (id),
    CONSTRAINT uk_e_match_type_batting_innings UNIQUE (match_id, type, batting_team, innings_id),
    CONSTRAINT fk_extras_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_extras_batting_team FOREIGN KEY(batting_team) references teams (id),
    CONSTRAINT fk_extras_bowling_team FOREIGN KEY(bowling_team) references teams (id)
);

CREATE SEQUENCE public.man_of_the_match_id_seq;

CREATE TABLE public.man_of_the_match
(
    id bigint NOT NULL DEFAULT nextval('man_of_the_match_id_seq'::regclass),
    match_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT man_of_the_match_pkey PRIMARY KEY (id),
    CONSTRAINT uk_motm_match_player_team UNIQUE (match_id, player_id, team_id),
    CONSTRAINT fk_man_of_the_match_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_man_of_the_match_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_man_of_the_match_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.man_of_the_series_id_seq;

CREATE TABLE public.man_of_the_series
(
    id bigint NOT NULL DEFAULT nextval('man_of_the_series_id_seq'::regclass),
    series_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT man_of_the_series_pkey PRIMARY KEY (id),
    CONSTRAINT uk_mots_series_player_team UNIQUE (series_id, player_id, team_id),
    CONSTRAINT fk_man_of_the_series_series_id FOREIGN KEY(series_id) references series (id),
    CONSTRAINT fk_man_of_the_series_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_man_of_the_series_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.captains_id_seq;

CREATE TABLE public.captains
(
    id bigint NOT NULL DEFAULT nextval('captains_id_seq'::regclass),
    match_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT captains_pkey PRIMARY KEY (id),
    CONSTRAINT uk_captains UNIQUE (match_id, player_id, team_id),
    CONSTRAINT fk_captains_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_captains_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_captains_team_id FOREIGN KEY(team_id) references teams (id)
);

CREATE SEQUENCE public.wicket_keepers_id_seq;

CREATE TABLE public.wicket_keepers
(
    id bigint NOT NULL DEFAULT nextval('wicket_keepers_id_seq'::regclass),
    match_id bigint NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint NOT NULL,
    CONSTRAINT wicket_keepers_pkey PRIMARY KEY (id),
    CONSTRAINT uk_wicketkeepers UNIQUE (match_id, player_id, team_id),
    CONSTRAINT fk_wicket_keepers_match_id FOREIGN KEY(match_id) references matches (id),
    CONSTRAINT fk_wicket_keepers_player_id FOREIGN KEY(player_id) references players (id),
    CONSTRAINT fk_wicket_keepers_team_id FOREIGN KEY(team_id) references teams (id)
);