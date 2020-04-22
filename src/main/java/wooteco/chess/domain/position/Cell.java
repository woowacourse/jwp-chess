package wooteco.chess.domain.position;

import static wooteco.chess.domain.piece.Team.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wooteco.chess.domain.piece.Team;

public class Cell {
	private static final Map<Team, Cell> PAWN_INITIAL_ROW;
	private static final int MINIMUM_DISTANCE = 1;

	static {
		PAWN_INITIAL_ROW = new HashMap<>();
		PAWN_INITIAL_ROW.put(BLACK, new Cell(7));
		PAWN_INITIAL_ROW.put(WHITE, new Cell(2));
	}

	private final int number;

	public Cell(int number) {
		this.number = number;
	}

	public static List<Cell> valuesBetween(Cell start, Cell end) {
		if (start.number > end.number) {
			return buildDescendingCells(start, end);
		} else {
			return buildAscendingCells(start, end);
		}
	}

	private static List<Cell> buildAscendingCells(Cell start, Cell end) {
		List<Cell> cells = new ArrayList<>();
		for (int i = start.number + 1; i < end.number; i++) {
			cells.add(new Cell(i));
		}
		return cells;
	}

	private static List<Cell> buildDescendingCells(Cell start, Cell end) {
		List<Cell> cells = new ArrayList<>();
		for (int i = start.number - 1; i > end.number; i--) {
			cells.add(new Cell(i));
		}
		return cells;
	}

	public int getNumber() {
		return this.number;
	}

	public boolean isNear(Cell other) {
		return Math.abs(this.getNumber() - other.getNumber()) <= MINIMUM_DISTANCE;
	}

	public int calculateAbsolute(Cell other) {
		return Math.abs(this.getNumber() - other.getNumber());
	}

	public boolean isInitialPawnRow(Team team) {
		return this.equals(PAWN_INITIAL_ROW.get(team));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Cell cell = (Cell)o;
		return number == cell.number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public String toString() {
		return "Cell{" +
			"number=" + number +
			'}';
	}
}
