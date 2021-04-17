package chess.domain.response;

import chess.domain.board.Team;
import chess.dto.GameInfoDto;
import chess.dto.ScoreDto;
import chess.dto.SquareDto;

import java.util.List;

public class GameResponse {

    private final List<SquareDto> squares;
    private final Team turn;
    private final List<ScoreDto> scores;
    private final String gameId;
    private final Team winner;

    public GameResponse(GameInfoDto gameInfoDto, String gameId) {
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
