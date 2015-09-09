
SET @last_riddle_id = 0;
SET @last_rhyme_id = 0;

# TABLE SUMMARY: --------------------------------------------------------------
# riddle_table : Contains riddles (questions) that need answers.
# rhyme_table  : Contains rhymes that are the answers to riddles.
# riddle_rhyme_truth_table : Pairs VALID riddle_id + rhyme_id combinations.
#                            My take on Drew's "question answer" table.
#                            The difference being that if the pair is NOT
#                            found in the table, we assume FALSE. If found
#                            in table, we know to be true.
#
# riddle_rhyme_wrong_table : Used to explicitly mark a riddle+rhyme pair as
#                            a WRONG ANSWER. Used for error checking mostly.
#                            --
#                            Extention of Drew's "question answer" table.
#                            To mark all possible combinations of questions +
#                            answers means a LOT of manual work. And a LOT
#                            of what I believe to be unnecessary storage.
#                            With my riddle_rhyme_truth_table, 100 riddles
#                            gives you initially 100 entries, assuming
#                            there is only ONE correct answer for each riddle.
#                            --
#                            Using the format for question_answer table...
#                            For 100 questions that have only one unique answer
#                            I would need: 100x100 entries in the 
#                            question_answer table.
#                            --
#                            Rather than do this. We occassionally explicitly
#                            mark a riddle+rhyme pair as WRONG by entering it
#                            in this table. If the riddle+rhyme is also found
#                            in the truth table, it signifies that two admins
#                            DISAGREE on this question and this question is
#                            now INVALID until the descrepancy is fixed.
#                            | IN TRUTH? | WRONG? | MEANS:
#                            |    YES    |   NO   | Correct Response given.
#                            |    NO     |   YES  | Incorrect Response given.
#                            |    NO     |   NO   | Presumed False by default.
#                            |    YES    |   YES  | ERROR: Flaged true+false
# -------------------------  TABLE SUMMARY END ---------------------------------
DROP TABLE IF EXISTS riddle_table;
DROP TABLE IF EXISTS rhyme_table;
DROP TABLE IF EXISTS riddle_rhyme_truth_table;
DROP TABLE IF EXISTS riddle_rhyme_wrong_table;


BEGIN;

-- Max length == 800, as in: 10 lines max when each line is 80 chars. --
-- Max length == 80 (one line) for our answers (rhymes).              --
CREATE TABLE riddle_table (id serial PRIMARY KEY, string varchar(800) );
CREATE TABLE rhyme_table (id serial PRIMARY KEY, string varchar(80) );
CREATE TABLE riddle_rhyme_truth_table (riddle_id INT UNSIGNED NOT NULL, 
                                       rhyme_id  INT UNSIGNED NOT NULL);
CREATE TABLE riddle_rhyme_wrong_table (riddle_id INT UNSIGNED NOT NULL, 
                                       rhyme_id  INT UNSIGNED NOT NULL);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('I build up castles but tear down mountains make some men blind but others to see. What am I?');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('Sand my man.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('I have lots to say but can never speak. Knowledge is the thing I eat. Inside me adventures you will find quests and treasures of every kind for all those that wish to visit me your hands are the ultimate key. What am I?');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('A book, Ms. Cook.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('The beginning of eternity, the end of time and space, the beginnings of every end, the end of every place. What am I?');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('The letter "e" I see.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('White flesh above and brown gills below. Never moving an inch, and in darkness I grow.');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('A mushroom maybe?');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('I touch the Earth, I touch the sky, But if I touch you, you will surely die.');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('Lightning is frightening.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('I run away every day and return every night. I am the bane of the flower and a friend to furry little robbers.');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('Raccoons after noon.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('I live in the oceans, the rivers and seas, when I am cold I float, when I am hot, I am free.');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('Water is wet.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);

-- RIDDLE + RHYME ENTRY --
INSERT INTO riddle_table (string) VALUES ('You use it between your head and toes, the more it works the thinner it grows. What is it?');
SET @last_riddle_id = LAST_INSERT_ID();
INSERT INTO rhyme_table (string) VALUES ('Soap on a rope.');
SET @last_rhyme_id  = LAST_INSERT_ID();
INSERT INTO riddle_rhyme_truth_table(riddle_id, rhyme_id) VALUES (@last_riddle_id,@last_rhyme_id);


COMMIT;
