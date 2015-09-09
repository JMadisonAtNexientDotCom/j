
DROP TABLE IF EXISTS short_adjective;
DROP TABLE IF EXISTS long_adjective;
DROP TABLE IF EXISTS noun;

BEGIN;

CREATE TABLE short_adjective (id serial PRIMARY KEY, string varchar(30) );
CREATE TABLE long_adjective (id serial PRIMARY KEY, string varchar(30) );
CREATE TABLE noun (id serial PRIMARY KEY, string varchar(30) );

INSERT INTO short_adjective (string) VALUES ('artless');
INSERT INTO long_adjective (string) VALUES ('base-court');
INSERT INTO noun (string) VALUES ('apple-john');

COMMIT;
