DROP DATABASE IF EXISTS login_study;
DROP USER IF EXISTS 'login_study_dev'@'%';
DROP USER IF EXISTS 'login_study_dev'@'localhost';
# 만약 삭제하지 않으면 % 와 충돌
CREATE DATABASE login_study CHAR SET utf8;
CREATE USER 'login_study_dev'@'%' IDENTIFIED BY '12345678';
GRANT SELECT,INSERT,UPDATE,DELETE ON login_study.* TO  'login_study_dev'@'%';
flush PRIVILEGES;
USE login_study;
CREATE TABLE users(
    id VARCHAR(255) PRIMARY KEY,
    pw VARCHAR(255),
    picture VARCHAR(255),
    oauth ENUM('GOOGLE','KAKAO','NAVER','GITHUB'),
    role ENUM('GUEST','USER','MANAGER','ADMIN'),
    name VARCHAR(255) NOT NULL
);

INSERT INTO users(id,pw,role,name)
VALUES ('user1','$2a$10$wlg3paJ3C6Yw7cy73AHg/OhuZzAzifmJyYhgzkCpq8ZyT4zbcJ3tu','USER','사용자'),
       ('guest1','$2a$10$wlg3paJ3C6Yw7cy73AHg/OhuZzAzifmJyYhgzkCpq8ZyT4zbcJ3tu','GUEST','게스트'),
       ('manager1','$2a$10$wlg3paJ3C6Yw7cy73AHg/OhuZzAzifmJyYhgzkCpq8ZyT4zbcJ3tu','MANAGER','매니저'),
       ('admin1','$2a$10$wlg3paJ3C6Yw7cy73AHg/OhuZzAzifmJyYhgzkCpq8ZyT4zbcJ3tu','ADMIN','관리자');