package chess.domain;

import static chess.domain.piece.detail.Team.NONE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;

public class Board {

    private final Map<Square, Piece> value;

    public Board(final Map<Square, Piece> value) {
        this.value = value;
    }

    public void move(final Square from, final Square to) {
        Piece source = value.get(from);
        Piece target = value.get(to);

        validateMove(source, target);
        validateRoute(from, to);

        value.put(to, source.moveTo(to));
        value.put(from, new Blank(NONE, from));
    }

    public boolean isSameTeam(final Square from, final Team team) {
        final Piece piece = value.get(from);
        return piece.isSameTeam(team);
    }

    public Map<File, List<Piece>> findFilesByTeam(final Team team) {
        Map<File, List<Piece>> files = new EnumMap<>(File.class);

        for (File file : File.values()) {
            final List<Piece> pieces = getPiecesByFileAndTeam(file, team);
            files.put(file, pieces);
        }

        return files;
    }

    public boolean isKingDead() {
        return value.values()
                .stream()
                .filter(Piece::isKing)
                .count() == 1;
    }

    public Team getTeamWithAliveKing() {
        return value.values()
                .stream()
                .filter(Piece::isKing)
                .findFirst()
                .map(Piece::getTeam)
                .orElseThrow(() -> new IllegalStateException("보드에 킹이 존재하지 않습니다."));
    }

    private List<Piece> getPiecesByFileAndTeam(final File file, final Team team) {
        return value.keySet()
                .stream()
                .filter(square -> square.getFile() == file)
                .map(value::get)
                .filter(piece -> piece.getTeam() == team)
                .collect(Collectors.toList());
    }

    private void validateMove(final Piece source, final Piece target) {
        if (!source.canMove(target)) {
            throw new IllegalArgumentException("해당 위치로 이동할 수 없습니다.");
        }
    }

    private void validateRoute(final Square from, final Square to) {
        Direction direction = Direction.findAllDirections(from, to);
        for (Square square = from.next(direction); !square.equals(to); square = square.next(direction)) {
            validateBlock(square);
        }
    }

    private void validateBlock(final Square square) {
        if (!value.get(square).isBlank()) {
            throw new IllegalArgumentException("이동 경로에 장애물이 있습니다.");
        }
    }

    public Piece getPieceAt(final Square square) {
        return value.get(square);
    }

    public List<Piece> getPieces() {
        return new ArrayList<>(value.values());
    }

    public List<Piece> getPiecesByRank(final Rank rank) {
        return Arrays.stream(File.values())
                .map(file -> value.get(Square.of(file, rank)))
                .collect(Collectors.toList());
    }
}
