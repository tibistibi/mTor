CREATE DATABASE sonar CHARACTER SET utf8;

CREATE USER 'sonar' IDENTIFIED BY 'changeme';
GRANT ALL ON sonar.* TO 'sonar'@'%' IDENTIFIED BY 'changeme';
GRANT ALL ON sonar.* TO 'sonar'@'localhost' IDENTIFIED BY 'changeme';
FLUSH PRIVILEGES;
