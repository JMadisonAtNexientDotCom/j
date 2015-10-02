

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
DROP TABLE IF EXISTS token_table;
DROP TABLE IF EXISTS admin_table;
DROP TABLE IF EXISTS ninja_table
DROP TABLE IF EXISTS riddle_table;
DROP TABLE IF EXISTS rhyme_table;
DROP TABLE IF EXISTS riddle_rhyme_truth_table;
DROP TABLE IF EXISTS riddle_rhyme_wrong_table;
DROP TABLE IF EXISTS session_table;
DROP TABLE IF EXISTS owner_table; -- JOINS: { token_table(id), ninja_table(id), admin_table(id) }
--DROP TABLE IF EXISTS trans_table; -- helps us debug concurrency by seeing linear transactions.


-- Max length == 800, as in: 10 lines max when each line is 80 chars. --
-- Max length == 80 (one line) for our answers (rhymes).              --
-- -----------------------------------| [B][B][B][B][B] BASE ENTITY FIELDS [B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B][B]-------|
CREATE TABLE token_table              (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, token_hash varchar(80));
CREATE TABLE ninja_table              (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, name varchar(80), phone INT UNSIGNED, email varchar(80), portfolio_url varchar(80) );
CREATE TABLE riddle_table             (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, id serial PRIMARY KEY, text varchar(800) );
CREATE TABLE rhyme_table              (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, id serial PRIMARY KEY, text varchar(80) );
CREATE TABLE riddle_rhyme_truth_table (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, riddle_id INT UNSIGNED NOT NULL, rhyme_id INT UNSIGNED NOT NULL);
CREATE TABLE riddle_rhyme_wrong_table (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, riddle_id INT UNSIGNED NOT NULL, rhyme_id  INT UNSIGNED NOT NULL);
CREATE TABLE admin_table              (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, user_name varchar(80), pass_hash varchar (80) );
CREATE TABLE session_table            (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, token_id INT UNSIGNED NOT NULL, opened_on BIGINT UNSIGNED NOT NULL, duration INT UNSIGNED NOT NULL, is_active BOOLEAN NOT NULL);
CREATE TABLE owner_table              (id serial PRIMARY KEY, dele BOOLEAN, comment varchar(80), global_save_id INT UNSIGNED, record_local_save_count INT UNSIGNED, token_id INT UNSIGNED NOT NULL, ninja_id INT UNSIGNED NOT NULL, admin_id INT UNSIGNED NOT NULL);

-- trans_table is a KERNEL entity, not a base entity. So it lacks some of the --
-- fields that are included on all of the others.  -----------------------------
CREATE TABLE trans_table              (id serial PRIMARY KEY, convo_open_id INT UNSIGNED, convo_close_id INT UNSIGNED, log_id INT UNSIGNED, foreign_table_name varchar(80), foreign_record_id INT UNSIGNED(80), foreign_record_comment varchar(80) );

-- bug fix: Tells me trans table does not exist... Is it my formatting?       --
-- Or do I need to create at least one entry to force it into existance?      --
-- ANSWER: It was a concurrency issue. After removing "DROP TABLE IF EXISTS"  --
-- I no longer get error that "trans_table" does not exist.                   --
-- THIS MEANS IMPORT.SQL is firing multiple times and interweaving.           --

-- Make an entry into the admin_table                                         --
-- ALL USER NAMES SHOULD BE STORED AS LOWERCASE BECAUSE WE WANT USER NAMES    --
-- TO BE CASE-INSENSITIVE!!!                                                  --
INSERT INTO admin_table (user_name, pass_hash,dele) VALUES ('sensei', 'password_hashed', false);

-- BUG: It seems that using FOREIGN KEY in import.sql FAILS... As much as I want to make
-- A "proper" create-table file. I cannot.
-- CREATE TABLE session_table (id serial PRIMARY KEY, FOREIGN KEY (token_id) REFERENCES token_table(id), opened_on INT, duration INT, is_active BOOLEAN, comment varchar(80) ); 
-- CREATE TABLE owner_table (FOREIGN KEY (token_id) REFERENCES token_table(id), FOREIGN KEY (ninja_id) REFERENCES ninja_table(id), FOREIGN KEY (admin_id) REFERENCES admin_table(id) , comment varchar(80) );
-- TODO: Is there a way to hack session_table into existance?                 --
-- CREATE TABLE does not seem like enough.                                    --
-- HACKING SOME TABLES INTO EXISTANCE THAT I WANTED TO START EMPTY            --
--INSERT INTO session_table(id, token_id, opened_on, duration, is_active, comment) VALUES (0,0,0,0,FALSE,"importSQLHACK");
--INSERT INTO owner_table(token_id, ninja_id, admin_id) VALUES (0,0,0);

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