ALTER TABLE stadiums DROP CONSTRAINT uk_s_name_country;
ALTER TABLE stadiums ADD CONSTRAINT uk_s_name_country_city UNIQUE (name, country_id, city);
