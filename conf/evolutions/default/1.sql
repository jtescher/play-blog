# --- !Ups
CREATE TABLE posts (
    id long,
    title varchar,
    body varchar);

# --- !Downs
DROP TABLE IF EXISTS posts;
