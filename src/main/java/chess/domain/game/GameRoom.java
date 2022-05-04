package chess.domain.game;

public class GameRoom {

    private final String gameRoomId;
    private final String name;
    private final String password;
    private final ChessGame chessGame;

    public GameRoom(final String gameRoomId, final String name, final String password, final ChessGame chessGame) {
        this.gameRoomId = gameRoomId;
        this.name = name;
        this.password = password;
        this.chessGame = chessGame;
    }

    public String getGameRoomId() {
        return gameRoomId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }
}
