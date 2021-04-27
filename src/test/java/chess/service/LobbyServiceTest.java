package chess.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import chess.repository.ChessRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LobbyServiceTest {

    @Mock
    private ChessRepository chessRepository;
    @InjectMocks
    private LobbyService lobbyService;


    @Test
    @DisplayName("새 게임 생성 테스트")
    void newGame() {
        given(chessRepository.addGame(any(), eq("test-room"))).willReturn("1");
        assertThat(lobbyService.newGame("test-room")).isEqualTo("1");
    }

    @Test
    @DisplayName("방 이름으로 id 찾아오는 테스트")
    void findGame() {
        given(chessRepository.findGame("test-room")).willReturn(Optional.of("1"));
        assertThat(lobbyService.findGame("test-room")).isEqualTo(Optional.of("1"));
    }

    @Test
    @DisplayName("방 이름으로 id 찾기 실패")
    void findGameFail() {
        assertThat(lobbyService.findGame("fail-room")).isEqualTo(Optional.empty());
    }
}
