package wooteco.chess.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.dto.BoardDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ChessGameEntityTest {
    @Test
    @DisplayName("엔티티 클래스 생성 테스트")
    void create() {
        //given
        ChessGame chessGame = new ChessGame(new Ready());
        chessGame.start();
        String boardString = String.join("", new BoardDto(chessGame.board()).getBoard());
        //when
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        //then
        assertThat(chessGameEntity.getBoard()).isEqualTo(boardString);
        assertThat(chessGameEntity.getTurn()).isEqualTo(chessGame.turn().getColor().toString());
        assertThat(chessGameEntity.getState()).isEqualTo(chessGame.getState().toString());
    }
}
