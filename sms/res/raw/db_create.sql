CREATE TABLE poll ( pollId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  question text,  startdate date,  enddate date null);
CREATE TABLE answer (
    answerId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    pollId INTEGER,
    option text
);

CREATE TABLE respondant (
    respondantId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    pollId INTEGER,
    answerId INTEGER,
    phoneno text
);

CREATE UNIQUE INDEX id_poll ON poll(pollId);
CREATE UNIQUE INDEX id_answer ON answer(answerId);
CREATE UNIQUE INDEX id_respondant ON respondant(respondantId);
