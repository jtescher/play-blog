# --- !Ups
CREATE TABLE posts (
    id LONG NOT NULL AUTO_INCREMENT,
    title VARCHAR NOT NULL default '',
    body VARCHAR NOT NULL default '',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE IF EXISTS posts;
