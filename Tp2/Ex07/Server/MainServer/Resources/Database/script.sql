CREATE TABLE USER (
  user_id INTEGER PRIMARY KEY,
  username VARCHAR(20) UNIQUE,
  pass VARCHAR(20)
);
CREATE TABLE PERMISSION (
  permission_id INTEGER PRIMARY KEY,
  permission_name VARCHAR(20) unique
);
CREATE TABLE USER_ACCESS_PERMISSION (
  uap_id INTEGER PRIMARY KEY,
  user_id references USER,
  permission_id references PERMISSION,
  unique(user_id, permission_id)
);
CREATE TABLE USER_RESOURCE_PERMISSION (
  urp_id INTEGER PRIMARY KEY,
  user_id references USER,
  permission_id references PERMISSION,
  file_name VARCHAR(40) unique,
  unique(user_id, permission_id, file_name)
);
--permissions are:
INSERT INTO PERMISSION VALUES (1, "get");
INSERT INTO PERMISSION VALUES (2, "post");
INSERT INTO PERMISSION VALUES (3, "del");
INSERT INTO PERMISSION VALUES (4, "dir");

-- user admin is:
INSERT INTO USER VALUES (1, "admin", "admin");

-- user admin has this permissions:
INSERT INTO USER_ACCESS_PERMISSION VALUES (1, 1, 1);
INSERT INTO USER_ACCESS_PERMISSION VALUES (2, 1, 2);
INSERT INTO USER_ACCESS_PERMISSION VALUES (3, 1, 3);
INSERT INTO USER_ACCESS_PERMISSION VALUES (4, 1, 4);


