# --- !Ups
CREATE TABLE posts (
    id long NOT NULL AUTO_INCREMENT,
    title varchar NOT NULL,
    body varchar NOT NULL);

# --- !Downs
DROP TABLE IF EXISTS posts;
