package chess.domain.dto;

import chess.domain.board.Team;

import java.util.List;

public class MoveResponseDto {

    private final List<SquareDto> squares;
    private final Team turn;
    private final List<ScoreDto> scores;
    private final String gameId;
    private final Team winner;

    public MoveResponseDto(GameInfoDto gameInfoDto, String gameId) {
        this.squares = gameInfoDto.squares();
        this.turn = gameInfoDto.turn();
        this.scores = gameInfoDto.scores();
        this.gameId = gameId;
        this.winner = gameInfoDto.winner();
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

    public String getGameId() {
        return gameId;
    }

    public Team getWinner() {
        return winner;
    }
}
