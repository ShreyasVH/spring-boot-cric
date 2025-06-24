DROP INDEX uk_s_name_country;

CREATE UNIQUE INDEX uk_s_name_country_city ON stadiums (name, country_id, city);

