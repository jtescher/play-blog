# --- !Ups
CREATE TABLE comments (
    id LONG NOT NULL AUTO_INCREMENT,
    body VARCHAR NOT NULL DEFAULT '',
    post_id LONG NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE IF EXISTS comments;
