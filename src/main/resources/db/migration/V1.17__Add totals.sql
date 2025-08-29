CREATE TABLE totals (
  id                            serial NOT NULL,
  match_id                      int NOT NULL,
  team_id                       bigint  NOT NULL,
  runs                          smallint NOT NULL DEFAULT '0',
  wickets                       smallint DEFAULT '0',
  balls                         smallint NOT NULL DEFAULT '0',
  innings                       smallint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_t_match_team_innings UNIQUE (match_id, team_id, innings),
  CONSTRAINT fk_t_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_t_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE INDEX idx_totals_team_id ON totals (team_id);