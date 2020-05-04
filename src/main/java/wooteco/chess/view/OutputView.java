package wooteco.chess.view;

import org.springframework.ui.Model;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;

import java.util.HashMap;
import java.util.Map;

public class OutputView {
    public static void constructModel(Long roomId, String title, Board board, Model model) {
        Map<String, String> pieces = toImageNames(board);
        for (String positionKey: pieces.keySet()) {
            model.addAttribute(positionKey, pieces.get(positionKey));
        }
        model.addAttribute("id", roomId);
        model.addAttribute("title", title);
    }

    private static Map<String, String> toImageNames(Board board) {
        Map<Position, Piece> originalPieces = board.getPieces();
        Map<String, String> result = new HashMap<>();

        for (Position position : originalPieces.keySet()) {
            String pieceKey = toImageName(originalPieces.get(position));
            result.put(position.getKey(), pieceKey);
        }
        return result;
    }

    private static String toImageName(Piece piece) {
        if (piece.isEmpty()) {
            return "blank";
        }
        return piece.getTeamSymbol() + piece.getSymbol();
    }
}
