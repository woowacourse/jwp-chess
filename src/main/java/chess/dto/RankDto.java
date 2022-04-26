package chess.dto;

import chess.domain.piece.Piece;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RankDto {

    private final List<PieceDto> pieces;
    private final int rankLine;

    public RankDto(final List<PieceDto> pieces, final int rankLine) {
        this.pieces = pieces;
        this.rankLine = rankLine;
    }

    public static RankDto toDto(final List<Piece> pieces, final int rankLine) {
        return new RankDto(toPieceSignature(pieces), rankLine);
    }

    private static List<PieceDto> toPieceSignature(final List<Piece> pieces) {
        return pieces.stream()
                .map(PieceDto::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public int getRankLine() {
        return rankLine;
    }
}
