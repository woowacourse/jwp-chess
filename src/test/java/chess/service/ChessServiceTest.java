package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.domain.ChessRepository;
import chess.domain.Color;
import chess.domain.Position;
import chess.domain.game.Game;
import chess.domain.game.state.GameStateFactory;
import chess.domain.piece.movable.Pawn;
import chess.domain.piece.movable.single.King;
import chess.domain.player.Player;
import chess.domain.player.Players;
import chess.repository.FakeChessRepository;
import chess.service.dto.response.GameResponseDto;
import chess.service.dto.response.GameStatusResponseDto;
import chess.service.dto.response.PieceResponseDto;
import chess.service.dto.response.PlayerResponseDto;
import chess.service.dto.response.PositionResponseDto;

class ChessServiceTest {

    private static final String TITLE = "TITLE";
    private static final String PASSWORD = "PASSWORD";

    private ChessService chessService;
    private ChessRepository chessRepository;

    @BeforeEach
    void setUp() {
        this.chessRepository = new FakeChessRepository();
        chessRepository.save(Game.initializeGame(TITLE, PASSWORD));

        this.chessService = new ChessService(chessRepository);
    }

    @DisplayName("게임 목록을 확인할 수 있어야 한다.")
    @Test
    void listGames() {
        final List<GameStatusResponseDto> gameStatusResponseDtos = chessService.listGames();
        assertThat(gameStatusResponseDtos).hasSize(1);
    }

    @DisplayName("새로운 게임을 생성할 수 있어야 한다.")
    @Test
    void createNewGame() {
        final Long gameId = chessService.createNewGame(TITLE, PASSWORD);
        assertThat(gameId).isNotNull();
    }

    @DisplayName("비밀번호가 일치하지 않으면 게임을 삭제할 수 없어야 한다.")
    @Test
    void passwordDoesNotCorrectException() {
        assertThatThrownBy(() -> chessService.removeGame(1L, "Wrong" + PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("진행중인 게임은 삭제할 수 없어야 한다.")
    @Test
    void runningGameCannotDeleteException() {
        assertThatThrownBy(() -> chessService.removeGame(1L, PASSWORD))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("종료되지 않은 게임은 삭제할 수 없습니다.");
    }

    @DisplayName("게임을 삭제할 수 있어야 한다.")
    @Test
    void removeGame() {
        chessService.endGame(1L);
        chessService.removeGame(1L, PASSWORD);
        assertThat(chessService.listGames()).hasSize(0);
    }

    @DisplayName("저장된 게임을 불러올 수 있어야 한다.")
    @Test
    void loadGame() {
        assertThat(chessService.loadGame(1L)).isNotNull();
    }

    @DisplayName("기물을 옮길 수 있어야 한다.")
    @Test
    void movePiece() {
        final GameResponseDto gameResponseDto = chessService.movePiece(1L, "a2", "a4");
        final List<String> positions = gameResponseDto.getPlayersResponseDto()
                .getPlayerResponseDtos().values()
                .stream()
                .map(PlayerResponseDto::getPieceUnits)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .map(PositionResponseDto::getPosition)
                .collect(Collectors.toUnmodifiableList());

        assertAll(() -> {
            assertThat(positions).doesNotContain("a2");
            assertThat(positions).contains("a4");
        });
    }

    @DisplayName("프로모션할 수 있어야 한다.")
    @Test
    void promotion() {
        chessRepository.save(Game.loadGame(2L, TITLE, PASSWORD, GameStateFactory.loadGameState(
                new Players(
                        new Player(3L, Color.WHITE, new HashMap<>(Map.of(
                                Position.from("a1"), King.getInstance(),
                                Position.from("b8"), Pawn.getWhitePawn()))),
                        new Player(4L, Color.BLACK, new HashMap<>(Map.of(
                                Position.from("a8"), King.getInstance())))
                ), false, Color.WHITE)));

        final GameResponseDto gameResponseDto = chessService.promotion(2L, "Queen");
        final List<String> pieces = gameResponseDto.getPlayersResponseDto()
                .getPlayerResponseDtos().values()
                .stream()
                .map(PlayerResponseDto::getPieceUnits)
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(PieceResponseDto::getPiece)
                .collect(Collectors.toUnmodifiableList());

        assertAll(() -> {
            assertThat(pieces).doesNotContain("Pawn");
            assertThat(pieces).contains("Queen");
        });
    }

    @DisplayName("플레이어의 점수를 계산할 수 있어야 한다.")
    @Test
    void calculatePlayerScores() {
        final Map<String, Double> playerScores = chessService.calculatePlayerScores(1L).getPlayerScores();

        assertAll(() -> {
            assertThat(playerScores.get("White")).isEqualTo(38);
            assertThat(playerScores.get("Black")).isEqualTo(38);
        });
    }

    @DisplayName("게임을 중단할 수 있어야 한다.")
    @Test
    void endGame() {
        final GameResponseDto gameResponseDto = chessService.endGame(1L);
        assertThat(gameResponseDto.getFinished()).isTrue();
    }
}
