
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

# Declaring variables: Help:
# A how to: http://stackoverflow.com/questions/11505522/mysql-declaring-variables
# Tells me variables need to be declared and used within the scope
# of a "BEGIN;" and "[COMMIT; / END;]"
# https://dev.mysql.com/doc/refman/5.0/en/declare-local-variable.html



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
