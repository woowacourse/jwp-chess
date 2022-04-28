package chess.dto.response.web;

import static chess.domain.piece.PieceTeam.BLACK;
import static chess.domain.piece.PieceTeam.WHITE;

import chess.domain.board.ChessBoard;
import chess.domain.board.position.Position;
import chess.domain.db.BoardPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.factory.PieceFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class GameResponse {

    private List<PieceResponse> board;
    private String teamName;

    private Map<String, Double> teamNameToScore;

    public GameResponse(final ChessBoard chessBoardParameter) {
        List<PieceResponse> board = new ArrayList<>();
        for (Position position : chessBoardParameter.getBoard().keySet()) {
            Piece piece = chessBoardParameter.getBoard().get(position);
            board.add(new PieceResponse(position, piece));
        }
        this.board = board;
        this.teamName = chessBoardParameter.currentStateName();
        this.teamNameToScore = createTeamNameToScore(chessBoardParameter);
    }

    public GameResponse(List<BoardPiece> boardPieces, String lastTeam) {
        final List<PieceResponse> board = new ArrayList<>();
        for (BoardPiece boardPiece : boardPieces) {
            Piece piece = PieceFactory.create(boardPiece.getPiece());
            board.add(new PieceResponse(boardPiece.getPosition(), piece));
        }
        this.board = board;
        this.teamName = lastTeam;
    }

    private Map<String, Double> createTeamNameToScore(ChessBoard chessBoardParameter) {
        double whiteScore = chessBoardParameter.calculateScoreByTeam(WHITE);
        double blackScore = chessBoardParameter.calculateScoreByTeam(BLACK);

        return Map.of("white", whiteScore, "black", blackScore);
    }
}
