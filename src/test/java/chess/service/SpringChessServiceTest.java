package chess.service;

import chess.domain.ChessGame;
import chess.domain.team.Team;
import chess.webdao.MysqlChessDao;
import chess.webdto.view.ChessGameDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpringChessServiceTest {
    @InjectMocks
    SpringChessService springChessService;

    @Mock
    MysqlChessDao mysqlChessDao;

    static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("새로운 게임 시작하기")
    void startNewGame() throws Exception {
        ChessGameDto original = new ChessGameDto(new ChessGame(Team.blackTeam(), Team.whiteTeam()));

        ChessGameDto result = springChessService.startNewGame();

        assertThat(objectMapper.writeValueAsString(result)).isEqualTo(objectMapper.writeValueAsString(original));
    }


}