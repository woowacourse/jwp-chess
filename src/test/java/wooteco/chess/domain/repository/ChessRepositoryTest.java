package wooteco.chess.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.entity.ChessGameEntity;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.Ready;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChessRepositoryTest {

    @Autowired
    private ChessRepository chessRepository;

    @Test
    @DisplayName("체스 게임 생성 테스트")
    void create() {
        //given
        ChessGame chessGame = new ChessGame(new Ready());
        chessGame.start();
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        //when
        ChessGameEntity resultEntity = chessRepository.save(chessGameEntity);
        //then
        assertThat(chessGameEntity.getId()).isNotNull();
        chessRepository.delete(chessGameEntity);
    }

    @Test
    @DisplayName("ID 조회 기능 테스트")
    void findById() {
        //given
        ChessGame chessGame = new ChessGame(new Ready());
        chessGame.start();
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        //when
        ChessGameEntity resultEntity = chessRepository.save(chessGameEntity);
        //then
        assertThat(chessRepository.findById(resultEntity.getId()).get().getId())
                .isEqualTo(chessGameEntity.getId());
        chessRepository.delete(chessGameEntity);
    }
}
