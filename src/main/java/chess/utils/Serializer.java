package chess.utils;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceMapper;
import chess.dto.ChessBoardDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Serializer {
    private static final int LAST_INDEX_OF_EACH_PIECE = 3;
    private static final int STARTING_INDEX_OF_POSITION = 1;

    public static String serializeGame(ChessGame chessGame) {
        return chessGame.getChessBoardMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getName() + entry.getKey().getStringPosition())
                .collect(Collectors.joining());
    }

    public static ChessBoardDto chessGameAsDto(ChessGame chessGame) {
        return deserializeGameAsDto(serializeGame(chessGame));
    }

    public static ChessBoard deserializeGame(String response) {
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

    public static ChessBoardDto deserializeGameAsDto(String response) {
        Map<String, String> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += LAST_INDEX_OF_EACH_PIECE) {
            String name = response.substring(i, i + STARTING_INDEX_OF_POSITION);
            String position = response.substring(i + STARTING_INDEX_OF_POSITION, i + LAST_INDEX_OF_EACH_PIECE);
            chessBoard.put(position, name);
        }
        return new ChessBoardDto(chessBoard);
    }
}
