package wooteco.chess.domain;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceScore;
import wooteco.chess.domain.piece.Turn;

public class Status {
	private final Map<Turn, Double> status;

	private Status(Map<Turn, Double> status) {
		this.status = status;
	}

	public static Status of(List<Piece> pieces) {
		return new Status(Map.of(
			Turn.BLACK, calculateOf(groupByTeam(pieces, Turn.BLACK)),
			Turn.WHITE, calculateOf(groupByTeam(pieces, Turn.WHITE))
		));
	}

	private static List<Piece> groupByTeam(List<Piece> pieces, Turn turn) {
		return pieces.stream()
			.filter(piece -> piece.isSameTeam(turn))
			.collect(Collectors.toList());
	}

	private static double calculateOf(List<Piece> teams) {
		return groupByColumn(teams)
			.stream()
			.mapToDouble(PieceScore::calculateScoreOf)
			.sum();
	}

	private static Collection<List<Piece>> groupByColumn(List<Piece> teams) {
		return teams.stream()
			.collect(groupingBy(
				piece -> piece.getPosition().getColumn(),
				mapping(Function.identity(), toList())))
			.values();
	}

	public Turn getWinner() {
		if (status.get(Turn.BLACK).equals(status.get(Turn.WHITE))) {
			return Turn.NONE;
		}

		double winnerScore = Collections.max(status.values());
		return status.keySet()
			.stream()
			.filter(key -> status.get(key).equals(winnerScore))
			.findFirst()
			.orElseThrow(NullPointerException::new);
	}

	public Map<Turn, Double> toMap() {
		return Map.copyOf(status);
	}
}
