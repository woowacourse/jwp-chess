package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.auth.AuthCredentials;
import chess.domain.event.InitEvent;
import chess.domain.event.MoveEvent;
import chess.domain.game.NewGame;
import chess.dto.CreatedGameDto;
import chess.dto.GameCountDto;
import chess.dto.GameSnapshotDto;
import chess.dto.GameOverviewDto;
import chess.dto.GameResultDto;
import chess.dto.SearchResultDto;
import chess.service.fixture.EventDaoStub;
import chess.service.fixture.GameDaoStub;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class ChessServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ChessService service;

    @BeforeEach
    void setup() {
        service = new ChessService(new GameDaoStub(namedParameterJdbcTemplate),
                new EventDaoStub(namedParameterJdbcTemplate));
    }

    @Test
    void findGames_메서드는_모든_게임의_id와_이름_정보를_반환한다() {
        List<GameOverviewDto> actual = service.findGames();

        List<GameOverviewDto> expected = List.of(
                new GameOverviewDto(1, "진행중인_게임"),
                new GameOverviewDto(2, "이미_존재하는_게임명"),
                new GameOverviewDto(3, "종료된_게임"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void countGames_메서드는_전체_게임_수와_실행_중인_게임_수를_담은_데이터를_반환한다() {
        GameCountDto actual = service.countGames();
        GameCountDto expected = new GameCountDto(3, 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void initGame_메서드는_새로운_게임을_DB에_저장하고_생성된_게임ID가_담긴_데이터를_반환한다() {
        AuthCredentials authCredentials = new AuthCredentials("유효한_게임명", "비밀번호");
        CreatedGameDto actual = service.initGame(authCredentials);
        CreatedGameDto expected = new CreatedGameDto(4);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void searchGame_메서드는_gameId에_해당되는_게임이_있다면_true가_담긴_데이터를_반환한다() {
        SearchResultDto actual = service.searchGame(1);
        SearchResultDto expected = new SearchResultDto(1, true);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void searchGame_메서드는_gameId에_해당되는_게임이_없다면_false가_담긴_데이터를_반환한다() {
        SearchResultDto actual = service.searchGame(99999);
        SearchResultDto expected = new SearchResultDto(99999, false);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findGame_메서드는_현재_게임의_상태와_체스말_정보를_반환한다() {
        GameSnapshotDto actual = service.findGame(1);

        GameSnapshotDto expected = new NewGame().play(new InitEvent())
                .play(new MoveEvent("e2 e4"))
                .play(new MoveEvent("d7 d5"))
                .play(new MoveEvent("f1 b5"))
                .toDtoOf(1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findGame_메서드는_존재하지_않는_게임인_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> service.findGame(999999))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("아직 게임이 시작되지 않았습니다.");
    }

    @Test
    void playGame_메서드는_이동_명령에_따라_이동시킨_후_그_결과를_반환한다() {
        GameSnapshotDto actual = service.playGame(1, new MoveEvent("a7 a5"));

        GameSnapshotDto expected = new NewGame().play(new InitEvent())
                .play(new MoveEvent("e2 e4"))
                .play(new MoveEvent("d7 d5"))
                .play(new MoveEvent("f1 b5"))
                .play(new MoveEvent("a7 a5"))
                .toDtoOf(1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void playGame_메서드는_이동_명령에_따라_이동시키며_게임이_종료된_경우_OVER로_상태를_변경한다() {
        service.playGame(2, new MoveEvent("b5 e8"));

        GameCountDto actual = service.countGames();
        GameCountDto expected = new GameCountDto(3, 2 - 1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void playGame_메서드는_존재하지_않는_게임인_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> service.findGame(999999))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("아직 게임이 시작되지 않았습니다.");
    }

    @Test
    void findGameResult_메서드는_종료된_게임의_승자_및_점수_정보를_계산하여_반환한다() {
        GameResultDto actual = service.findGameResult(3);

        GameResultDto expected = new GameResultDto(3, new NewGame()
                .play(new InitEvent())
                .play(new MoveEvent("e2 e4"))
                .play(new MoveEvent("d7 d5"))
                .play(new MoveEvent("f1 b5"))
                .play(new MoveEvent("a7 a5"))
                .play(new MoveEvent("b5 e8")));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findGameResult_메서드는_게임이_종료되지_않은_경우_예외_발생() {
        assertThatThrownBy(() -> service.findGameResult(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아직 게임 결과가 산출되지 않았습니다.");
    }
}