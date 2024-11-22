-- make sure create database, jdk can't do - doing scripting here
-- if init script isnt running, presume db already exists
CREATE DATABASE "inventory-service";
GRANT ALL PRIVILEGES ON DATABASE "inventory-service" TO "admin"