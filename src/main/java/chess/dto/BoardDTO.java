package chess.dto;

import chess.dao.ChessGame;
import chess.domain.piece.property.Team;
import chess.domain.piece.unit.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public final class BoardDTO {

    private Map<String, Object> board = new HashMap<>();
    private Team currentTurn;
    private double blackScore;
    private double whiteScore;
    private boolean isGameSet = Boolean.FALSE;
    private String winner = "";

    public BoardDTO(final ChessGame chessGame) {
        Map<Position, Piece> board = chessGame.getChessBoard().getBoard();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            String symbol = convertPieceSymbol(piece);
            this.board.put(position.toString(), symbol);
        }
        this.currentTurn = chessGame.getChessBoard().getCurrentTurn();
        this.blackScore = GameStatus.calculateTeamScore(chessGame.getChessBoard().getBoard(), Team.BLACK);
        this.whiteScore = GameStatus.calculateTeamScore(chessGame.getChessBoard().getBoard(), Team.WHITE);
        if (chessGame.isGameSet()){
            isGameSet = Boolean.TRUE;
            this.winner = chessGame.getChessBoard().calculateWhoWinner().toString();
        }
    }

    private String convertPieceSymbol(final Piece piece) {
        String symbol = "";
        if (piece != null) {
            symbol = piece.getSymbolByTeam();
        }
        return symbol;
    }

    public Map<String, Object> getBoard() {
        return board;
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public boolean isGameSet() {
        return isGameSet;
    }

    public String getWinner() {
        return winner;
    }
}
