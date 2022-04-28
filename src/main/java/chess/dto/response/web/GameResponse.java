package chess.dto.response.web;

import static chess.domain.piece.PieceTeam.BLACK;
import static chess.domain.piece.PieceTeam.WHITE;

import chess.domain.board.ChessBoard;
import chess.domain.board.position.Position;
import chess.domain.db.BoardPiece;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public class GameResponse {

    private static Map<String, Supplier<? extends Piece>> pieceCreator = new HashMap<>();

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
            Piece piece = pieceCreator.get(boardPiece.getPiece()).get();
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

    static {
        initWhitePieceCreator();
        initBlackPieceCreator();
    }

    private static void initWhitePieceCreator() {
        pieceCreator.put("whitePawn", () -> new Pawn(WHITE));
        pieceCreator.put("whiteKnight", () -> new Knight(WHITE));
        pieceCreator.put("whiteBishop", () -> new Bishop(WHITE));
        pieceCreator.put("whiteRook", () -> new Rook(WHITE));
        pieceCreator.put("whiteQueen", () -> new Queen(WHITE));
        pieceCreator.put("whiteKing", () -> new King(WHITE));
    }

    private static void initBlackPieceCreator() {
        pieceCreator.put("blackPawn", () -> new Pawn(BLACK));
        pieceCreator.put("blackKnight", () -> new Knight(BLACK));
        pieceCreator.put("blackBishop", () -> new Bishop(BLACK));
        pieceCreator.put("blackRook", () -> new Rook(BLACK));
        pieceCreator.put("blackQueen", () -> new Queen(BLACK));
        pieceCreator.put("blackKing", () -> new King(BLACK));
    }
}
