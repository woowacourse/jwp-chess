package spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import spring.dto.ChessGameIdDto;
import spring.entity.repository.ChessGameRepository;

import static org.assertj.core.api.Assertions.assertThat;

// TODO : Test 어노테이션 공부하자~
@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class ChessServiceTest {

    @Autowired
    public ChessGameRepository chessGameRepository;

    // TODO : 서비스 테스트 방법
    @Test
    void makeChessBoard() {
        ChessService chessService = new ChessService(chessGameRepository);

        ChessGameIdDto chessGameIdDto = chessService.makeChessBoard();

        assertThat(chessGameIdDto.getId()).isEqualTo(1);
    }
}