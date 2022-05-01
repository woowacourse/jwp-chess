package chess.domain.game;

import chess.domain.piece.Color;

public class ChessRoom {

    private static final int PASSWORD_LIMIT_LENGTH = 30;
    public static final int NAME_LIMIT_LENGTH = 255;

    private final String name;
    private final String password;
    private final ChessGame chessGame;

    public ChessRoom(String name, String password, ChessGame chessGame) {
        validateNameEmpty(name);
        validateNameLength(name);
        validatePasswordLength(password);

        this.name = name;
        this.password = password;
        this.chessGame = chessGame;
    }

    public ChessRoom(String name, String password, String turnValue) {
        this(name, password, new ChessGame(Color.of(turnValue)));
    }

    private void validateNameLength(String name) {
        if (name.length() > NAME_LIMIT_LENGTH) {
            throw new IllegalArgumentException("이름은 255자 이하여야합니다.");
        }
    }

    private void validatePasswordLength(String password) {
        if (password.length() > PASSWORD_LIMIT_LENGTH) {
            throw new IllegalArgumentException("패스워드는 30자 이하여야합니다.");
        }
    }

    private void validateNameEmpty(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("방 제목은 빈 문자열일 수 없습니다.");
        }
    }

    public void checkSamePassword(String target) {
        if (!password.equals(target)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean isEnd() {
        return chessGame.isEnd();
    }
}
