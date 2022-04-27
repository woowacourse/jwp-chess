package chess.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;

public class PieceDto {
    private static final int INITIAL_CAPACITY = 64;
    private String position;
    private String team;
    private String symbol;

    public PieceDto(Piece piece) {
        Team team = piece.getTeam();
        this.team = team.getTeam();
        this.symbol = piece.getSymbolByTeam().toLowerCase();
    }

    private PieceDto(Piece piece, PositionDto position) {
        this.position = position.getPosition();
        Team team = piece.getTeam();
        this.team = team.getTeam();
        this.symbol = piece.getSymbolByTeam().toLowerCase();
    }

    private PieceDto(PositionDto positionDto) {
        this.position = positionDto.getPosition();
    }

    public static PieceDto of(Piece piece, PositionDto position) {
        if (piece != null) {
            return new PieceDto(piece, position);
        }

        return new PieceDto(position);
    }

    public static List<PieceDto> getOnBoard(Map<Position, Piece> cells) {
        List<PieceDto> pieceDtos = new ArrayList<>(INITIAL_CAPACITY);
        addRowPieces(cells, pieceDtos);
        return pieceDtos;
    }

    private static void addRowPieces(Map<Position, Piece> cells, List<PieceDto> pieceDtos) {
        for (Rank rank : Rank.reverseValues()) {
            addColumnPieces(cells, pieceDtos, rank);
        }
    }

    private static void addColumnPieces(Map<Position, Piece> cells, List<PieceDto> pieceDtos, Rank rank) {
        for (File file : File.values()) {
            Position position = Position.of(file, rank);
            pieceDtos.add(PieceDto.of(cells.get(position), PositionDto.from(position)));
        }
    }

    public String getTeam() {
        return team;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPosition() {
        return position;
    }
}
