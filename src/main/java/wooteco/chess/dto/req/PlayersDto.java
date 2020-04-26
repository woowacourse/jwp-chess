package wooteco.chess.dto.req;

public class PlayersDto {
	private String player1Name;
	private String player1Password;
	private String player2Name;
	private String player2Password;

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}

	public String getPlayer1Password() {
		return player1Password;
	}

	public void setPlayer1Password(String player1Password) {
		this.player1Password = player1Password;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
	}

	public String getPlayer2Password() {
		return player2Password;
	}

	public void setPlayer2Password(String player2Password) {
		this.player2Password = player2Password;
	}
}
