CREATE INDEX index_bs_match_player ON batting_scores (match_player_id);

CREATE INDEX index_bs_dismissal_mode ON batting_scores (dismissal_mode_id);

CREATE INDEX index_m_series ON matches (series_id);

CREATE INDEX index_t_type ON teams (type_id);

CREATE INDEX index_s_game_type ON series (game_type_id);

CREATE INDEX index_bf_match_player ON bowling_figures (match_player_id);

CREATE INDEX index_fd_match_player ON fielder_dismissals (match_player_id);

CREATE INDEX index_fd_score ON fielder_dismissals (score_id);

CREATE INDEX match_player_map_player_id_match_id_team_id_index ON match_player_map (player_id, match_id, team_id);