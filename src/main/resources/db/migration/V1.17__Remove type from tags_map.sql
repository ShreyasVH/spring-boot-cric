ALTER TABLE tags_map DROP CONSTRAINT uk_tm_type_id_tag;

ALTER TABLE tags_map ADD CONSTRAINT uk_tm_id_tag UNIQUE (entity_id, tag_id);

ALTER TABLE tags_map DROP COLUMN entity_type;