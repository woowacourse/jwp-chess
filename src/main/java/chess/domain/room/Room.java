package chess.domain.room;

import chess.domain.ChessGame;
import chess.domain.chessboard.ChessBoardFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class Room {

    private final RoomName name;
    private final String password;
    private final ChessGame chessGame;

    public Room(final String name, final String password, final ChessGame chessGame) {
        this.name = new RoomName(name);
        validatePassword(password);
        this.password = password;
        this.chessGame = chessGame;
    }

    public Room(final String name, final String password) {
        this.name = new RoomName(name);
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.chessGame = new ChessGame(ChessBoardFactory.createChessBoard());
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
