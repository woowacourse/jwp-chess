package chess.database.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.database.GameStateGenerator;
import chess.database.dto.GameStateDto;
import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.BoardFixtures;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.domain.game.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameStateGeneratorTest {

    @Test
    @DisplayName("상태와 색깔 문자열로 상태 객체를 만든다.")
    public void createStateByStateAndColorString() {
        // given
        Board board = BoardFixtures.EMPTY;
        GameStateDto gameStateDto = GameStateDto.of(State.READY, Color.WHITE);
        // when
        GameState generated = GameStateGenerator.generate(board, gameStateDto);
        // then
        assertThat(generated).isInstanceOf(Ready.class);
    }
}
