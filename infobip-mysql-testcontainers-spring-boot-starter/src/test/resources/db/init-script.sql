DROP TABLE IF EXISTS test_table;

CREATE TABLE test_table(
    id serial PRIMARY KEY,
    name VARCHAR(4) NOT NULL
);

INSERT INTO test_table(id, name)
VALUES (1, 'Test');
