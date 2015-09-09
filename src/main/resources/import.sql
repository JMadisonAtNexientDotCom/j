
DROP TABLE IF EXISTS riddle_table;
DROP TABLE IF EXISTS rhyme_table;
-- DROP TABLE IF EXISTS riddle_rhyme_truth_table;
-- DROP TABLE IF EXISTS riddle_rhyme_false_table;


BEGIN;

-- Max length == 800, as in: 10 lines max when each line is 80 chars. --
-- Max length == 80 (one line) for our answers (rhymes).              --
CREATE TABLE riddle_table (id serial PRIMARY KEY, string varchar(800) );
CREATE TABLE rhyme_table (id serial PRIMARY KEY, string varchar(80) );

 
INSERT INTO riddle_table (string) VALUES ('I build up castles but tear down mountains make some men blind but others to see. What am I?');
INSERT INTO rhyme_table (string) VALUES ('Sand my man.');

INSERT INTO riddle_table (string) VALUES ('I have lots to say but can never speak. Knowledge is the thing I eat. Inside me adventures you will find quests and treasures of every kind for all those that wish to visit me your hands are the ultimate key. What am I?');
INSERT INTO rhyme_table (string) VALUES ('A book, Ms. Cook.');

INSERT INTO riddle_table (string) VALUES ('The beginning of eternity, the end of time and space, the beginnings of every end, the end of every place. What am I?');
INSERT INTO rhyme_table (string) VALUES ('The letter "e" I see.');

INSERT INTO riddle_table (string) VALUES ('White flesh above and brown gills below. Never moving an inch, and in darkness I grow.');
INSERT INTO rhyme_table (string) VALUES ('A mushroom maybe?');

INSERT INTO riddle_table (string) VALUES ('I touch the Earth, I touch the sky, But if I touch you, you will surely die.');
INSERT INTO rhyme_table (string) VALUES ('Lightning is frightening.');

INSERT INTO riddle_table (string) VALUES ('I run away every day and return every night. I am the bane of the flower and a friend to furry little robbers.');
INSERT INTO rhyme_table (string) VALUES ('Raccoons after noon.');

INSERT INTO riddle_table (string) VALUES ('I live in the oceans, the rivers and seas, when I am cold I float, when I am hot, I am free.');
INSERT INTO rhyme_table (string) VALUES ('Water is wet.');

INSERT INTO riddle_table (string) VALUES ('You use it between your head and toes, the more it works the thinner it grows. What is it?');
INSERT INTO rhyme_table (string) VALUES ('Soap on a rope.');


COMMIT;
