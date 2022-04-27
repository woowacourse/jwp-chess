package chess;

import static chess.domain.state.Turn.WHITE_TURN;

import chess.domain.ChessGame;

public class ChessGameFixture {

    public static ChessGame createRunningChessGame(){
        return new ChessGame(WHITE_TURN.name(), "title", "password");
    }
}
