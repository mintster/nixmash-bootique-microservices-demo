--
-- Table structure for table roles
--
DROP TABLE
IF EXISTS roles;

CREATE TABLE roles (
  role_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  permission VARCHAR (80) DEFAULT NULL,
  role_name VARCHAR (45) DEFAULT NULL
);

--
-- Table structure for table users
--
DROP TABLE
IF EXISTS users;

CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR (50) NOT NULL,
  email VARCHAR (150) NOT NULL,
  first_name VARCHAR (40) NOT NULL,
  last_name VARCHAR (40) NOT NULL,
  enabled TINYINT (1) DEFAULT '1' NOT NULL,
  account_expired TINYINT (1) DEFAULT '0' NOT NULL,
  account_locked TINYINT (1) DEFAULT '0' NOT NULL,
  credentials_expired TINYINT (1) DEFAULT '0' NOT NULL,
  has_avatar TINYINT (1) DEFAULT '0' NOT NULL,
  user_key VARCHAR (25) DEFAULT '0000000000000000' NOT NULL,
  provider_id VARCHAR (25) DEFAULT 'SITE' NOT NULL,
  PASSWORD VARCHAR (255) NOT NULL,
  version INT DEFAULT '0' NOT NULL
);

--
-- Table structure for table user_roles
--
DROP TABLE
IF EXISTS user_roles;

CREATE TABLE user_roles (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT DEFAULT NULL,
  role_id BIGINT DEFAULT NULL
);

DROP TABLE
IF EXISTS jangles_users;

CREATE TABLE jangles_users (
  user_id BIGINT (20) NOT NULL AUTO_INCREMENT,
  username VARCHAR (25) DEFAULT NULL,
  PASSWORD VARCHAR (50) DEFAULT NULL,
  display_name VARCHAR (50) DEFAULT NULL,
  date_created TIMESTAMP NULL DEFAULT NULL,
  is_active TINYINT (1) DEFAULT NULL,
  PRIMARY KEY (user_id)
);

TRUNCATE TABLE jangles_users;

INSERT INTO jangles_users (user_id, username, password, display_name, date_created, is_active) VALUES (1, 'Bopper', 'password', 'Bopper Johnson', '2017-05-29 09:56:25', 1);
INSERT INTO jangles_users (user_id, username, password, display_name, date_created, is_active) VALUES (2, 'Bipper', 'password', 'Bipper Blaster', '2017-05-29 09:56:45', 1);
INSERT INTO jangles_users (user_id, username, password, display_name, date_created, is_active) VALUES (3, 'Smoker', 'password', 'Smoker Sql', '2017-05-29 09:57:06', 1);


