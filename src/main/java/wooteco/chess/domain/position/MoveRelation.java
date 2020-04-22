package wooteco.chess.domain.position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public enum MoveRelation {
	SAME_ROW(Position::isSameRow, MoveRelation::findSameRowTrace),
	DIAGONAL(Position::isDiagonal, MoveRelation::findDiagonalTrace),
	SAME_COLUMN(Position::isSameColumn, MoveRelation::findSameColumnTrace);

	private final BiPredicate<Position, Position> relationChecker;

	private final BiFunction<Position, Position, List<Position>> traceMaker;
	MoveRelation(
		BiPredicate<Position, Position> relationChecker,
		BiFunction<Position, Position, List<Position>> traceMaker) {
		this.relationChecker = relationChecker;
		this.traceMaker = traceMaker;
	}

	static List<Position> findMultipleStepTrace(Position from, Position to) {
		return Arrays.stream(values())
			.filter(moveRelation -> moveRelation.relationChecker.test(from, to))
			.map(moveRelation -> moveRelation.traceMaker.apply(from, to))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 위치 이동 경로는 찾을 수 없습니다."));
	}

	private static List<Position> findSameColumnTrace(Position from, Position to) {
		List<Cell> ranks = Cell.valuesBetween(from.getRank(), to.getRank());
		return ranks.stream()
			.map(rank -> Position.of(from.getFile(), rank))
			.collect(Collectors.toList());
	}

	private static List<Position> findDiagonalTrace(Position from, Position to) {
		List<Position> positions = new ArrayList<>();
		List<Cell> files = Cell.valuesBetween(from.getFile(), to.getFile());
		List<Cell> ranks = Cell.valuesBetween(from.getRank(), to.getRank());
		for (int i = 0; i < files.size(); i++) {
			positions.add(Position.of(files.get(i), ranks.get(i)));
		}
		return positions;
	}

	private static List<Position> findSameRowTrace(Position from, Position to) {
		List<Cell> files = Cell.valuesBetween(from.getFile(), to.getFile());
		return files.stream()
			.map(file -> Position.of(file, from.getRank()))
			.collect(Collectors.toList());
	}
}
