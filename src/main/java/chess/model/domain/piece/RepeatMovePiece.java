package chess.model.domain.piece;

import chess.model.domain.board.Square;
import chess.model.domain.board.CastlingSetting;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class RepeatMovePiece extends Piece {

    protected RepeatMovePiece(Team team, Type type) {
        super(team, type);
    }

    @Override
    protected int getRepeatCount() {
        return Square.MAX_FILE_AND_RANK_COUNT - Square.MIN_FILE_AND_RANK_COUNT;
    }

    @Override
    public Set<Square> getMovableArea(Square boardSquare, Map<Square, Piece> board,
        Set<CastlingSetting> castlingElements) {
        Set<Square> allMovableArea = getAllMovableArea(boardSquare);
        Set<Square> containSquares = getContainsSquares(board, allMovableArea);
        for (Square square : containSquares) {
            int fileCompare = square.getFileCompare(boardSquare);
            int rankCompare = square.getRankCompare(boardSquare);
            allMovableArea.removeAll(findSquaresToRemove(square, fileCompare, rankCompare));
            allMovableArea.removeAll(getSameColorSquare(board, square));
        }
        return allMovableArea;
    }

    private Set<Square> getContainsSquares(Map<Square, Piece> board,
        Set<Square> allMovableArea) {
        return allMovableArea.stream()
            .filter(board::containsKey)
            .collect(Collectors.toSet());
    }

    private Set<Square> getSameColorSquare(Map<Square, Piece> board,
        Square containSquare) {
        Set<Square> sameColorSquare = new HashSet<>();
        if (isSameTeam(board.get(containSquare))) {
            sameColorSquare.add(containSquare);
        }
        return sameColorSquare;
    }
}
