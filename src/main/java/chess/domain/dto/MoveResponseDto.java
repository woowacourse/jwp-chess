package chess.domain.dto;

import chess.domain.board.Team;
import java.util.List;

public class MoveResponseDto {

    private List<SquareDto> squares;
    private Team turn;
    private List<ScoreDto> scores;
    private Team winner;
    private String gameId;
    private String errorMessage;

    public MoveResponseDto(GameInfoDto gameInfoDto, String gameId) {
        this.squares = gameInfoDto.squares();
        this.turn = gameInfoDto.turn();
        this.scores = gameInfoDto.scores();
        this.winner = null;
        this.gameId = gameId;
    }

    public MoveResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public List<SquareDto> getSquares() {
        return squares;
    }

    public Team getTurn() {
        return turn;
    }

    public List<ScoreDto> getScores() {
        return scores;
    }

    public Team getWinner() {
        return winner;
    }

    public String getGameId() {
        return gameId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
