DROP DATABASE IF EXISTS login_study;
CREATE DATABASE login_study CHAR SET utf8;
CREATE USER 'login_study_dev'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT,INSERT,UPDATE,DELETE ON login_study.* TO  'login_study_dev'@'localhost';
USE login_study;
CREATE TABLE users(
    id VARCHAR(255) PRIMARY KEY,
    pw VARCHAR(255) NOT NULL,
    role ENUM('GUEST','USER','MANAGER','ADMIN'),
    name VARCHAR(255) NOT NULL
);
INSERT INTO users(id,pw,role,name)
VALUES ('user1','1234','USER','사용자'),
       ('guest1','1234','GUEST','게스트'),
       ('manager1','1234','MANAGER','매니저'),
       ('admin1','1234','ADMIN','관리자');