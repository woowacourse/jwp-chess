package wooteco.chess.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.entity.ChessGameEntity;

@SpringBootTest
class ChessGameRepositoryTest {

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Test
    @DisplayName("새로운 체스 게임 생성")
    void save() {
        //given
        ChessGame chessGame = ChessGame.of(new Ready());
        chessGame.start();
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        //when
        ChessGameEntity savedChessGameEntity = chessGameRepository.save(chessGameEntity);
        //then
        assertThat(savedChessGameEntity.getId()).isNotNull();
        chessGameRepository.delete(savedChessGameEntity);
    }

    @Test
    @DisplayName("id로 체스방 조회")
    void findById() {
        //given
        ChessGame chessGame = ChessGame.of(new Ready());
        chessGame.start();
        ChessGameEntity chessGameEntity = new ChessGameEntity(chessGame);
        //when
        ChessGameEntity savedChessGameEntity = chessGameRepository.save(chessGameEntity);
        //then
        assertThat(chessGameRepository.findById(savedChessGameEntity.getId()).get().getId())
            .isEqualTo(chessGameEntity.getId());
        chessGameRepository.delete(savedChessGameEntity);
    }
}
