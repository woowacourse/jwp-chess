package chess.dto;

import chess.domain.piece.Piece;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RankDto {

    private final List<PieceDto> pieces;
    private final int rankLine;

    public RankDto(List<PieceDto> pieces, int rankLine) {
        this.pieces = pieces;
        this.rankLine = rankLine;
    }

    public static RankDto toDto(List<Piece> pieces, int rankLine) {
        return new RankDto(toPieceSignature(pieces), rankLine);
    }

    private static List<PieceDto> toPieceSignature(List<Piece> pieces) {
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
