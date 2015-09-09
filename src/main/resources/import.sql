

BEGIN;

# Declaring variables: Help:
# A how to: http://stackoverflow.com/questions/11505522/mysql-declaring-variables
# Tells me variables need to be declared and used within the scope
# of a "BEGIN; and END;"
# https://dev.mysql.com/doc/refman/5.0/en/declare-local-variable.html
SET @last_riddle_id = 0;
SET @last_rhyme_id = 0;

-- Table Summary: (Lost previous summary somewhere in commit history)
-- riddle_table: Questions that need answering.
-- rhyme_table : Answers to riddles.
-- riddle_rhyme_truth_table: pairs riddle+rhyme. 
--                           If pair is in here, it is true.
-- riddle_rhyme_wrong_table: pairs riddle+rhyme. 
--                           If pair is in here, it is wrong.
--                           This table mostly for data integrity check.
--
-- Design justification:
-- QUESTION: WHY USE riddle_rhyme_truth_table and riddle_rhyme_wrong_table ??
-- ANSWER: Having a question_answer table where there is a pair and a 3rd column
--         telling us if that question is wrong or right means that if there
--         are 100 questions each with only one correct answer, we will need
--         100x100 entries. 10,000 entries! That is a LOT OF SPACE.
--         And a LOT OF WORK for test makers to explicitly define all of this.
--         Instead: Only the correct answer needs to be defined.
--         The riddle_rhyme_wrong_table is mostly for finding errors.
--         If pair exists in both the "wrong" table and the "truth" table,
--         The question is now invalid because developers could not agree on it.
--
--         in wrong_table:  | in truth_table: | MEANS:
--               YES        |      YES        | Error. Answer both true&false
--               YES        |      NO         | Answer is FALSE. EXPLICIT
--               NO         |      NO         | Answer is FALSE. (implied)
--               NO         |      YES        | Answer is TRUE.  EXPLICIT
DROP TABLE IF EXISTS riddle_table;
DROP TABLE IF EXISTS rhyme_table;
DROP TABLE IF EXISTS riddle_rhyme_truth_table;
DROP TABLE IF EXISTS riddle_rhyme_wrong_table;

-- Max length == 800, as in: 10 lines max when each line is 80 chars. --
-- Max length == 80 (one line) for our answers (rhymes).              --
CREATE TABLE riddle_table (id serial PRIMARY KEY, text varchar(800) );
CREATE TABLE rhyme_table (id serial PRIMARY KEY, text varchar(80) );
CREATE TABLE riddle_rhyme_truth_table (riddle_id INT UNSIGNED NOT NULL, rhyme_id INT UNSIGNED NOT NULL);
CREATE TABLE riddle_rhyme_wrong_table (riddle_id INT UNSIGNED NOT NULL, rhyme_id  INT UNSIGNED NOT NULL);


-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('I build up castles but tear down mountains make some men blind but others to see. What am I?');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('Sand my man.');
SET @last_rhyme_id := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id, @last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('I have lots to say but can never speak. Knowledge is the thing I eat. Inside me adventures you will find quests and treasures of every kind for all those that wish to visit me your hands are the ultimate key. What am I?');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('A book, Ms. Cook.');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('The beginning of eternity, the end of time and space, the beginnings of every end, the end of every place. What am I?');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('The letter "e" I see.');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('White flesh above and brown gills below. Never moving an inch, and in darkness I grow.');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('A mushroom maybe?');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('I touch the Earth, I touch the sky, But if I touch you, you will surely die.');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('Lightning is frightening.');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('I run away every day and return every night. I am the bane of the flower and a friend to furry little robbers.');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('Raccoons after noon.');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('I live in the oceans, the rivers and seas, when I am cold I float, when I am hot, I am free.');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('Water is wet.');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (text) VALUES ('You use it between your head and toes, the more it works the thinner it grows. What is it?');
SET @last_riddle_id := LAST_INSERT_ID();
INSERT INTO rhyme_table (text) VALUES ('Soap on a rope.');
SET @last_rhyme_id  := LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);


COMMIT;