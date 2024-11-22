-- make sure create database, jdk can't do - doing scripting here
-- if init script isnt running, presume db already exists
CREATE DATABASE "order-service";
GRANT ALL PRIVILEGES ON DATABASE "order-service" TO "admin"