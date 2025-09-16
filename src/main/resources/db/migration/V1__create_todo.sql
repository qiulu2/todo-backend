CREATE TABLE todo
(
    id   VARCHAR(255) NOT NULL,
    text VARCHAR(255) NULL,
    done BIT(1)       NOT NULL,
    CONSTRAINT pk_todo PRIMARY KEY (id)
);