INSERT INTO poll ( question, startdate, enddate) VALUES('How many bones?','2009-08-02', null);

INSERT INTO answer (pollId, option) VALUES(4,'300');
INSERT INTO answer (pollId, option) VALUES(4,'800');
INSERT INTO answer (pollId, option) VALUES(4,'1000');

INSERT INTO respondant (pollId, answerId, phoneno) VALUES(4,0,'3333');
INSERT INTO respondant (pollId, answerId, phoneno) VALUES(4,0,'4444');
INSERT INTO respondant (pollId, answerId, phoneno) VALUES(4,4,'5555');
INSERT INTO respondant (pollId, answerId, phoneno) VALUES(4,6,'6666');
