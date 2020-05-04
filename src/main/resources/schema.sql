

CREATE TABLE room
  (
      id    bigint       NOT NULL auto_increment,
      title VARCHAR(256) NOT NULL,
      turn  VARCHAR(64)  NOT NULL,
      PRIMARY KEY (id)

  );

CREATE TABLE board
(
    id       bigint      NOT NULL auto_increment,
    roomId   bigint      NOT NULL,
    position VARCHAR(64) NOT NULL,
    piece    VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);