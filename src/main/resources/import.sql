
DROP TABLE IF EXISTS riddle_table;
DROP TABLE IF EXISTS rhyme_table;
-- DROP TABLE IF EXISTS riddle_rhyme_truth_table;
-- DROP TABLE IF EXISTS riddle_rhyme_false_table;


BEGIN;

CREATE TABLE riddle_table (id serial PRIMARY KEY, string varchar(80) );
CREATE TABLE rhyme_table (id serial PRIMARY KEY, string varchar(30) );


INSERT INTO riddle_table (string) VALUES ('I build up castles but tear down mountains make some men blind but others to see. What am I?');
INSERT INTO rhyme_table (string) VALUES ('Sand my man.');


COMMIT;
