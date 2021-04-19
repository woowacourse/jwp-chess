package chess.utils;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceMapper;
import chess.dto.ChessBoardDTO;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 이게 어디로 가야 될지...?
 */
public class Serializer {
    private static final int LAST_INDEX_OF_EACH_PIECE = 3;
    private static final int STARTING_INDEX_OF_POSITION = 1;

    // 얘는 서비스로 가야할거 같은데..
    public static String serialize(ChessGame chessGame) {
        return chessGame.getChessBoardMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getName() + entry.getKey().getStringPosition())
                .collect(Collectors.joining());
    }

    // 얘는 서비스로 가야할거 같은데..
    public static ChessBoard deserialize(String response) {
        Map<Position, Piece> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += LAST_INDEX_OF_EACH_PIECE) {
            char name = response.charAt(i);
            String position = response.substring(i + STARTING_INDEX_OF_POSITION, i + LAST_INDEX_OF_EACH_PIECE);
            Position piecePosition = Position.of(position);
            Piece piece = PieceMapper.of(name);
            chessBoard.put(piecePosition, piece);
        }
        return new ChessBoard(chessBoard);
    }

    // 얘는 DTO로 변환이라 컨트롤러로 가야하는 것인가..
    public static ChessBoardDTO deserializeAsDTO(String response) {
        Map<String, String> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += LAST_INDEX_OF_EACH_PIECE) {
            String name = response.substring(i, i + STARTING_INDEX_OF_POSITION);
            String position = response.substring(i + STARTING_INDEX_OF_POSITION, i + LAST_INDEX_OF_EACH_PIECE);
            chessBoard.put(position, name);
        }
        return new ChessBoardDTO(chessBoard);
    }
}
