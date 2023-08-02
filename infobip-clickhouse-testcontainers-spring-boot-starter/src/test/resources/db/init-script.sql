CREATE TABLE test_table
(
    id Int32,
    name String
) ENGINE = MergeTree() ORDER BY id;

INSERT INTO test_table (id, name)
VALUES (1, 'Test');