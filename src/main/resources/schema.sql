CREATE TABLE IF NOT EXISTS t_user
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(64)  NOT NULL,
    email      VARCHAR(128) NOT NULL,
    age        INT          NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);
