DROP TABLE IF EXISTS jangles_users;
CREATE TABLE `jangles_users` (
  `user_id`      BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username`     VARCHAR(25)         DEFAULT NULL,
  `password`     VARCHAR(50)         DEFAULT NULL,
  `display_name` VARCHAR(50)         DEFAULT NULL,
  `date_created` TIMESTAMP  NULL     DEFAULT NULL,
  `is_active`    TINYINT(1)          DEFAULT NULL,
  PRIMARY KEY (`user_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = latin1;

DROP PROCEDURE IF EXISTS p_janglesusers_insert;
DELIMITER $$

CREATE PROCEDURE p_janglesusers_insert(
  IN  p_username     VARCHAR(25),
  IN  p_password     VARCHAR(50),
  IN  p_display_name VARCHAR(50),
  IN  p_is_active    TINYINT(1),
  OUT out_user_id    BIGINT)

  BEGIN
    INSERT INTO jangles_users (
      username,
      PASSWORD,
      display_name,
      date_created,
      is_active
    )
    VALUES
      (
        p_username,
        p_password,
        p_display_name,
        now(),
        p_is_active
      );
    SET out_user_id = LAST_INSERT_ID();
  END
$$

DELIMITER ;



