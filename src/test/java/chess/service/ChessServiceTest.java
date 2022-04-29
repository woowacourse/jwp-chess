package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.domain.auth.PlayerCookie;
import chess.domain.board.piece.Color;
import chess.domain.event.Event;
import chess.domain.event.InitEvent;
import chess.domain.event.MoveEvent;
import chess.domain.game.Game;
import chess.domain.game.NewGame;
import chess.dto.response.CreatedGameDto;
import chess.dto.response.SearchResultDto;
import chess.dto.view.FullGameDto;
import chess.dto.view.GameCountDto;
import chess.dto.view.GameOverviewDto;
import chess.dto.view.GameResultDto;
import chess.dto.view.GameSnapshotDto;
import chess.service.fixture.EventDaoStub;
import chess.service.fixture.GameDaoStub;
import chess.util.CookieUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ChessServiceTest {

    private static final MoveEvent GAME_ONE_VALID_BLACK_MOVE = new MoveEvent("a7 a5");
    private GameDaoStub gameDao;
    private EventDaoStub eventDao;
    private ChessService service;

    @BeforeEach
    void setup() {
        gameDao = new GameDaoStub();
        eventDao = new EventDaoStub();
        service = new ChessService(gameDao, eventDao);
    }

    @Test
    void findGames_메서드는_모든_게임의_id와_이름_정보를_반환한다() {
        List<GameOverviewDto> actual = service.findGames();

        List<GameOverviewDto> expected = findAllTestData();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findGame 메서드로 게임의 정보 조회 가능")
    @Nested
    class FindGameTest {

        @Test
        void 방명과_현재_게임의_상태_및_체스말_정보를_반환한다() {
            FullGameDto actual = service.findGame(1);

            FullGameDto expected = new FullGameDto(
                    new GameOverviewDto(1, "진행중인_게임"),
                    currentGameSnapshotOfGameIdOne().toSnapshotDto());

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_게임인_경우_예외를_발생시킨다() {
            assertThatThrownBy(() -> service.findGame(999999))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 게임입니다.");
        }
    }

    @DisplayName("findGameResult 메서드로 종료된 게임의 정보 조회 가능")
    @Nested
    class FindGameResultTest {

        @Test
        void 종료된_게임의_승자_및_점수_정보를_계산하여_반환한다() {
            GameResultDto actual = service.findGameResult(3);

            Game expectedGame = new NewGame().play(new InitEvent()).play(new MoveEvent("e2 e4"))
                    .play(new MoveEvent("d7 d5")).play(new MoveEvent("f1 b5"))
                    .play(GAME_ONE_VALID_BLACK_MOVE).play(new MoveEvent("b5 e8"));
            GameResultDto expected = new GameResultDto(new GameOverviewDto(3, "종료된_게임"),
                    expectedGame.toSnapshotDto(), expectedGame.getResult());

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 게임이_종료되지_않은_경우_예외_발생() {
            assertThatThrownBy(() -> service.findGameResult(1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아직 게임 결과가 산출되지 않았습니다.");
        }
    }

    @DisplayName("searchGame 메서드로 게임의 존재 여부 조회 가능")
    @Nested
    class SearchGameTest {

        @Test
        void gameId에_해당되는_게임이_있다면_true가_담긴_데이터를_반환한다() {
            SearchResultDto actual = service.searchGame(1);
            SearchResultDto expected = new SearchResultDto(1, true);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void gameId에_해당되는_게임이_없다면_false가_담긴_데이터를_반환한다() {
            SearchResultDto actual = service.searchGame(99999);
            SearchResultDto expected = new SearchResultDto(99999, false);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @Test
    void countGames_메서드는_전체_게임_수와_실행_중인_게임_수를_담은_데이터를_반환한다() {
        GameCountDto actual = service.countGames();
        GameCountDto expected = new GameCountDto(
                GameDaoStub.TOTAL_GAME_COUNT, GameDaoStub.RUNNING_GAME_COUNT);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void initGame_메서드는_새로운_게임을_DB에_저장하고_생성된_게임ID가_담긴_데이터를_반환한다() {
        EncryptedAuthCredentials authCredentials = new EncryptedAuthCredentials("유효한_게임명", "비밀번호");
        CreatedGameDto actual = service.initGame(authCredentials);
        CreatedGameDto expected = CreatedGameDto.of(4);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("playGame 메서드로 진행 중인 게임의 체스말 이동")
    @Nested
    class PlayGameTest {

        @Test
        void 이동_명령에_따라_체스말을_이동시킨다() {
            service.playGame(1, GAME_ONE_VALID_BLACK_MOVE, getPlayerCookieOf(1, Color.BLACK));
            FullGameDto actual = service.findGame(1);

            GameSnapshotDto expectedGame = currentGameSnapshotOfGameIdOne()
                    .play(GAME_ONE_VALID_BLACK_MOVE)
                    .toSnapshotDto();
            FullGameDto expected = new FullGameDto(
                    new GameOverviewDto(1, "진행중인_게임"), expectedGame);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 이동_명령에_따라_체스말을_이동시키며_게임이_종료된_경우_OVER로_상태를_변경한다() {
            service.playGame(2, new MoveEvent("b5 e8"),
                    getPlayerCookieOf(2, Color.WHITE));

            boolean actual = gameDao.checkRunning(2);

            assertThat(actual).isFalse();
        }

        @Test
        void 존재하지_않는_게임인_경우_예외를_발생시킨다() {
            assertThatThrownBy(() -> service.playGame(999999, new MoveEvent("b5 e8"),
                    getPlayerCookieOf(999999, Color.WHITE)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 게임입니다.");
        }
    }

    @DisplayName("playGame 메서드에 잘못된 플레이어 인증 정보를 전달했을 때의 예외처리 테스트")
    @Nested
    class PlayGameAuthTest {

        @Test
        void 다른_게임과_관련된_쿠키값인_경우_예외를_발생시킨다() {
            assertThatThrownBy(() ->  service.playGame(1, GAME_ONE_VALID_BLACK_MOVE,
                    getPlayerCookieOf(99999, Color.BLACK)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 게임의 플레이어가 아닙니다.");
        }

        @Test
        void 본인의_차례가_아닌_플레이어의_쿠키값의_경우_예외를_발생시킨다() {
            assertThatThrownBy(() ->  service.playGame(1, GAME_ONE_VALID_BLACK_MOVE,
                    getPlayerCookieOf(1, Color.WHITE)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상대방이 움직일 차례입니다!");
        }
    }

    @DisplayName("deleteFinishedGame 메서드로 종료된 게임 삭제 가능")
    @Nested
    class DeleteFinishedGameTest {

        @Test
        void 종료된_게임은_삭제_가능() {
            service.deleteFinishedGame(3, new EncryptedAuthCredentials("종료된_게임", "encrypted3"));

            boolean exists = gameDao.checkById(3);

            assertThat(exists).isFalse();
        }

        @Test
        void 종료된_게임_삭제시_이벤트도_전부_삭제() {
            service.deleteFinishedGame(3, new EncryptedAuthCredentials("종료된_게임", "encrypted3"));

            List<Event> relatedEvents = eventDao.findAllByGameId(3);

            assertThat(relatedEvents).isEmpty();
        }

        @Test
        void 게임이_종료되지_않은_경우_예외_발생() {
            assertThatThrownBy(() -> service.deleteFinishedGame(1,
                    new EncryptedAuthCredentials("진행중인_게임", "encrypted1")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아직 진행 중인 게임입니다.");
        }
    }

    private List<GameOverviewDto> findAllTestData() {
        return List.of(
                new GameOverviewDto(1, "진행중인_게임"),
                new GameOverviewDto(2, "이미_존재하는_게임명"),
                new GameOverviewDto(3, "종료된_게임"),
                new GameOverviewDto(4, "참여자가_있는_게임"),
                new GameOverviewDto(5, "참여자가_없는_게임"));
    }

    private Game currentGameSnapshotOfGameIdOne() {
        return new NewGame().play(new InitEvent())
                .play(new MoveEvent("e2 e4"))
                .play(new MoveEvent("d7 d5"))
                .play(new MoveEvent("f1 b5"));
    }

    private PlayerCookie getPlayerCookieOf(int gameId, Color playerColor) {
        if (playerColor == Color.WHITE) {
            return PlayerCookie.of(CookieUtil.generateGameOwnerCookie(gameId));
        }
        return PlayerCookie.of(CookieUtil.generateOpponentCookie(gameId));
    }
}