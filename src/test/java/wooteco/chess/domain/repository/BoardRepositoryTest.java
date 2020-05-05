package wooteco.chess.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wooteco.chess.domain.piece.PieceType;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("보드 말 하 저장 확인")
    void saveTest(){
        BoardEntity boardEntity = boardRepository.save(new BoardEntity((long) 1,"a2", PieceType.FIRST_WHITE_PAWN.name()));
        assertThat(boardRepository.findByRoomId((long) 1).size()).isEqualTo(1);
        assertThat(boardRepository.findByRoomIdAndPosition((long) 1, "a2")).isEqualTo(boardEntity);

    }
}
