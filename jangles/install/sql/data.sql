
INSERT INTO jangles_users VALUES (1, 'Jim', 'password', 'Jim Johnson', now(),1);
INSERT INTO jangles_users VALUES (2, 'Bill', 'password', 'Bill Blaster', now(),1);
INSERT INTO jangles_users VALUES (3, 'Sally', 'password', 'Sally Sql', now(),1);

INSERT INTO users (user_id, username, email, first_name, last_name, password)
VALUES (1, 'bob', 'bob@aol.com', 'Bob', 'Planter', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');
INSERT INTO users (user_id, username, email, first_name, last_name, password)
VALUES (2, 'ken', 'ken@aol.com', 'Ken', 'Stark', 'a4e63bcacf6c172ad84f9f4523c8f1acaf33676fa76d3258c67b7e7bbf16d777');

INSERT INTO roles (role_id, permission, role_name) VALUES (1, 'nixmash:all', 'admin');
INSERT INTO roles (role_id, permission, role_name) VALUES (2, 'nixmash:view', 'user');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

INSERT INTO user_data (user_id) SELECT user_id from users;
