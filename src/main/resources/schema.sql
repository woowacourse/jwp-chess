CREATE TABLE ROOM_TB (
	   ID      INT         PRIMARY KEY AUTO_INCREMENT
     , NM      VARCHAR(20) NOT NULL
     , PW      VARCHAR(20)
     , USED_YN CHAR(1)     NOT NULL DEFAULT 'Y'
     , REG_DT  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
     , CHG_DT  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE CHESS_GAME_TB (
       ID            INT         AUTO_INCREMENT PRIMARY KEY
	 , ROOM_ENTITY   INT         NOT NULL
     , TURN_NM       CHAR(5)     NOT NULL
     , PROCEEDING_YN CHAR(1)     NOT NULL DEFAULT 'Y'
     , BLACK_USER_NM VARCHAR(20) NOT NULL
     , WHITE_USER_NM VARCHAR(20) NOT NULL
	 , BLACK_SCORE   FLOAT       NOT NULL
     , WHITE_SCORE   FLOAT       NOT NULL
     , REG_DT        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
     , CHG_DT        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     , FOREIGN KEY (ROOM_ENTITY) REFERENCES ROOM_TB(ID) ON UPDATE CASCADE
);

CREATE TABLE CHESS_BOARD_TB (
       ID                  INT       AUTO_INCREMENT PRIMARY KEY
     , CHESS_GAME_ENTITY   INT       NOT NULL
	 , BOARDSQUARE_NM      CHAR(2)   NOT NULL
	 , PIECE_NM            CHAR(12)  NOT NULL
     , CASTLING_ELEMENT_YN CHAR(1)   NOT NULL
     , EN_PASSANT_NM       CHAR(2)
     , REG_DT              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , CHG_DT              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     , FOREIGN KEY (CHESS_GAME_ENTITY) REFERENCES CHESS_GAME_TB(ID) ON UPDATE CASCADE
);

CREATE TABLE RESULT_TB (
       ID      INT         AUTO_INCREMENT PRIMARY KEY
	 , USER_NM VARCHAR(20) UNIQUE
	 , WIN     INT         DEFAULT 0
     , DRAW    INT         DEFAULT 0
     , LOSE    INT         NOT NULL DEFAULT 0
     , REG_DT  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
     , CHG_DT  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);