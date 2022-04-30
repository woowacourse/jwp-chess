package chess.domain;

import chess.domain.chessboard.ChessBoardFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class Room {

    private static final int NAME_MAX_LENGTH = 10;
    
    private final String name;
    private final String password;
    private final ChessGame chessGame;

    public Room(final String name, final String password, final ChessGame chessGame) {
        validateName(name);
        this.name = name;
        validatePassword(password);
        this.password = password;
        this.chessGame = chessGame;
    }

    public Room(final String name, final String password) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.chessGame = new ChessGame(ChessBoardFactory.createChessBoard());
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("방 이름에 공백은 허용되지 않습니다.");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("방 이름은 최대 " + NAME_MAX_LENGTH + "자 까지 허용됩니다.");
        }
    }

    private void validatePassword(final String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("비밀번호에 공백은 허용되지 않습니다.");
        }
    }

    public void startGame() {
        chessGame.start();
    }

    public boolean canRemove(final String plainPassword) {
        if (chessGame.isPlaying()) {
            throw new IllegalArgumentException("게임이 진행 중입니다.");
        }
        if (BCrypt.checkpw(plainPassword, password)) {
            return true;
        }
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
}
