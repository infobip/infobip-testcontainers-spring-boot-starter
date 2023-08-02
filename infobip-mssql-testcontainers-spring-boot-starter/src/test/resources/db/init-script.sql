CREATE DATABASE JtdsTestDatabase;

USE JtdsTestDatabase;

CREATE TABLE test_table (
    id INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(4) NOT NULL
);

INSERT INTO test_table (name)
VALUES ('Test');
