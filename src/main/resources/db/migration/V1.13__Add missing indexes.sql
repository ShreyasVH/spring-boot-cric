create index index_bs_match_player on batting_scores (match_player_id);
create index index_bs_dismissal_mode on batting_scores (dismissal_mode_id);
create index index_m_series on matches (series_id);
create index index_t_type on teams (type_id);
create index index_s_game_type on series (game_type_id);
create index index_bf_match_player on bowling_figures (match_player_id);
create index index_fd_match_player on fielder_dismissals (match_player_id);
create index index_fd_score on fielder_dismissals (score_id);