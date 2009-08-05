INSERT INTO poll ( question, startdate, enddate) VALUES('how many fingers','2009-07-31','2009-07-31');

INSERT INTO answer (pollId, option) VALUES(3,'2 fingers');
INSERT INTO answer (pollId, option) VALUES(3,'20 fingers');
INSERT INTO answer (pollId, option) VALUES(3,'40 fingers');

INSERT INTO respondant (pollId, answerId, phoneno) VALUES(3,1,'0000');
INSERT INTO respondant (pollId, answerId, phoneno) VALUES(3,2,'1111');
INSERT INTO respondant (pollId, answerId, phoneno) VALUES(3,3,'2222');
