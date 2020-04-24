package wooteco.chess.domain.position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wooteco.chess.domain.piece.Team;

public class Position {
	public static final int MIN_POSITION_NUMBER = 1;
	public static final int MAX_POSITION_NUMBER = 8;
	private static final int KNIGHT_MULTIPLICATION_OF_BETWEEN_FILE_DISTANCE_AND_RANK_DISTANCE = 2;
	private static final Map<String, Position> CACHE = new HashMap<>();

	static {
		initPositionCache();
	}

	private static void initPositionCache() {
		for (int col = MIN_POSITION_NUMBER; col <= MAX_POSITION_NUMBER; col++) {
			for (int row = MIN_POSITION_NUMBER; row <= MAX_POSITION_NUMBER; row++) {
				CACHE.put(getKey(col, row), new Position(new Cell(col), new Cell(row)));
			}
		}
	}

	private final Cell col;
	private final Cell row;

	private Position(Cell file, Cell rank) {
		this.col = Objects.requireNonNull(file);
		this.row = Objects.requireNonNull(rank);
	}

	public static Position of(String key) {
		return CACHE.get(key);
	}

	public static Position of(int file, int rank) {
		return CACHE.get(getKey(file, rank));
	}

	public static Position of(Cell file, Cell rank) {
		return CACHE.get(getKey(file.getNumber(), rank.getNumber()));
	}

	private static String getKey(int file, int rank) {
		return (char)('a' + file - 1) + String.valueOf(rank);
	}

	public static List<Position> findMultipleStepTrace(Position from, Position to) {
		return MoveRelation.findMultipleStepTrace(from, to);
	}

	public boolean isInitialPawnPosition(Team team) {
		return row.isInitialPawnRow(team);
	}

	public boolean isNotDiagonal(Position other) {
		return !isDiagonal(other);
	}

	public boolean isDiagonal(Position other) {
		return col.calculateAbsolute(other.col) == row.calculateAbsolute(other.row);
	}

	public boolean isNotStraight(Position other) {
		return !col.equals(other.col) && !row.equals(other.row);
	}

	public boolean isSameRow(Position other) {
		return row.equals(other.row);
	}

	public boolean isSameColumn(Position other) {
		return col.equals(other.col);
	}

	public boolean isNotDistanceOneSquare(Position other) {
		return !(this.row.isNear(other.row) && this.col.isNear(other.col));
	}

	public boolean isNotKnightMovable(Position other) {
		return this.col.calculateAbsolute(other.col) * this.row.calculateAbsolute(other.row)
			!= KNIGHT_MULTIPLICATION_OF_BETWEEN_FILE_DISTANCE_AND_RANK_DISTANCE;
	}

	public Cell getFile() {
		return col;
	}

	public Cell getRank() {
		return row;
	}

	public int getRankNumber() {
		return row.getNumber();
	}

	@Override
	public String toString() {
		return String.valueOf((char)('a' + col.getNumber() - 1)) + row.getNumber();
	}
}
