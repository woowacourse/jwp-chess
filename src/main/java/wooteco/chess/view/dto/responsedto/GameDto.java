package wooteco.chess.view.dto.responsedto;

import java.util.Objects;

import wooteco.chess.domain.piece.Team;

public class GameDto {
	private final String turn;
	private final String gameState;

	private GameDto(String turn, String gameState) {
		this.turn = turn;
		this.gameState = gameState;
	}

	public static GameDto of(Team turn, String stateType) {
		return new GameDto(turn.getTeam(), stateType);
	}

	public String getTurn() {
		return turn;
	}

	public String getGameState() {
		return gameState;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GameDto gameDTO = (GameDto)o;
		return Objects.equals(turn, gameDTO.turn) &&
			Objects.equals(gameState, gameDTO.gameState);
	}

	@Override
	public int hashCode() {
		return Objects.hash(turn, gameState);
	}
}
