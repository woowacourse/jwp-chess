package chess.domain.board.factory;

import static chess.domain.piece.PieceTeam.EMPTY;

import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import chess.domain.entity.BoardPiece;
import chess.domain.piece.EmptySpace;
import chess.domain.piece.Piece;
import chess.domain.piece.factory.PieceFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbBoardFactory implements BoardFactory {

    private final List<BoardPiece> boardPieces;

    private static final EmptySpace EMPTY_SPACE = new EmptySpace(EMPTY);


    public DbBoardFactory(List<BoardPiece> boardPieces) {
        this.boardPieces = boardPieces;
    }

    @Override
    public Map<Position, Piece> create() {
        Map<Position, Piece> board = new HashMap<>();

        placeAllEmptyPieces(board);

        for (BoardPiece boardPiece : boardPieces) {
            String piece = boardPiece.getPiece();
            String position = boardPiece.getPosition();
            board.put(Position.of(position), PieceFactory.create(piece));
        }

        return board;
    }

    private void placeAllEmptyPieces(Map<Position, Piece> board) {

        Map<Position, Piece> emptyPiecesByPositions = Arrays.stream(Rank.values())
                .flatMap(rank -> Arrays.stream(File.values()).map(file -> Position.of(file, rank)))
                .collect(Collectors.toMap((position) -> position, (piece) -> EMPTY_SPACE));

        board.putAll(emptyPiecesByPositions);
    }
}
