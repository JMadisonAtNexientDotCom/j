

BEGIN;

DROP TABLE IF EXISTS riddle_table;
DROP TABLE IF EXISTS rhyme_table;
DROP TABLE IF EXISTS riddle_rhyme_truth_table;
DROP TABLE IF EXISTS riddle_rhyme_wrong_table;

-- Max length == 800, as in: 10 lines max when each line is 80 chars. --
-- Max length == 80 (one line) for our answers (rhymes).              --
CREATE TABLE riddle_table (id serial PRIMARY KEY, string varchar(800) );
CREATE TABLE rhyme_table (id serial PRIMARY KEY, string varchar(80) );
CREATE TABLE riddle_rhyme_truth_table (riddle_id INT UNSIGNED NOT NULL, 
                                       rhyme_id  INT UNSIGNED NOT NULL);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('I build up castles but tear down mountains make some men blind but others to see. What am I?');
INSERT INTO rhyme_table (string) VALUES ('Sand my man.');
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (1,1);

COMMIT;