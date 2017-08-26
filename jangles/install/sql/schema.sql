--
-- Table structure for table roles
--
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  role_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  permission VARCHAR (80) DEFAULT NULL,
  role_name VARCHAR (45) DEFAULT NULL
);

--
-- Table structure for table users
--
DROP TABLE IF EXISTS users;
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
DROP TABLE IF EXISTS user_roles;
CREATE TABLE user_roles (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT DEFAULT NULL,
  role_id BIGINT DEFAULT NULL
);

--
-- Table structure for table jangles_users
--
DROP TABLE IF EXISTS jangles_users;
CREATE TABLE jangles_users (
  user_id BIGINT (20) NOT NULL AUTO_INCREMENT,
  username VARCHAR (25) DEFAULT NULL,
  PASSWORD VARCHAR (50) DEFAULT NULL,
  display_name VARCHAR (50) DEFAULT NULL,
  date_created TIMESTAMP NULL DEFAULT NULL,
  is_active TINYINT (1) DEFAULT NULL,
  PRIMARY KEY (user_id)
)

--
-- Table structure for table user_data
--
DROP TABLE IF EXISTS user_data;
CREATE TABLE user_data (
  user_id BIGINT NOT NULL PRIMARY KEY,
  login_attempts INT DEFAULT '0' NOT NULL,
  lastlogin_datetime TIMESTAMP NULL,
  created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  approved_datetime TIMESTAMP NULL,
  invited_datetime TIMESTAMP NULL,
  accepted_datetime TIMESTAMP NULL,
  invited_by_id BIGINT DEFAULT '0' NOT NULL,
  ip VARCHAR (25) NULL,
  CONSTRAINT user_data_user_id_uindex UNIQUE (user_id),
  CONSTRAINT user_data_users_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

--
-- Table structure for view v_users
--
DROP VIEW IF EXISTS v_users;
CREATE VIEW v_users AS SELECT
       users.user_id AS user_id,
       users.username AS username,
       users.email AS email,
       users.first_name AS first_name,
       users.last_name AS last_name,
       users.enabled AS enabled,
       users.account_expired AS account_expired,
       users.account_locked AS account_locked,
       users.credentials_expired AS credentials_expired,
       users.has_avatar AS has_avatar,
       users.user_key AS user_key,
       users.provider_id AS provider_id,
       users.PASSWORD AS PASSWORD,
       users.version AS version,
       user_data.login_attempts AS login_attempts,
       user_data.lastlogin_datetime AS lastlogin_datetime,
       user_data.created_datetime AS created_datetime,
       user_data.approved_datetime AS approved_datetime,
       user_data.invited_datetime AS invited_datetime,
       user_data.accepted_datetime AS accepted_datetime,
       user_data.invited_by_id AS invited_by_id,
       user_data.ip AS ip
     FROM
       (
           users
           JOIN user_data ON (
           (
             user_data.user_id = users.user_id
           )
           )
       );

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS posts;
CREATE TABLE posts
(
  post_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL,
  post_title VARCHAR(200) NOT NULL,
  post_name VARCHAR(200) NOT NULL,
  post_link VARCHAR(255),
  post_date TIMESTAMP DEFAULT '0000-00-00 00:00:00' NOT NULL,
  post_modified TIMESTAMP DEFAULT '0000-00-00 00:00:00' NOT NULL,
  post_type VARCHAR(20) DEFAULT 'LINK' NOT NULL,
  display_type VARCHAR(20) DEFAULT 'LINK' NOT NULL,
  is_published TINYINT(1) DEFAULT '0' NOT NULL,
  post_content TEXT NOT NULL,
  post_source VARCHAR(50) DEFAULT 'NA' NOT NULL,
  post_image VARCHAR(200),
  click_count INT(11) DEFAULT '0' NOT NULL,
  likes_count INT(11) DEFAULT '0' NOT NULL,
  value_rating INT(11) DEFAULT '0' NOT NULL,
  version INT(11) DEFAULT '0' NOT NULL,
  CONSTRAINT posts_users_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE UNIQUE INDEX posts_post_id_uindex ON posts (post_id);
CREATE INDEX posts_users_user_id_fk ON posts (user_id);
