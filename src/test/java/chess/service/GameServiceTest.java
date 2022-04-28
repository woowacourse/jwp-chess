package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.MockGameDao;
import chess.dao.MockMemberDao;
import chess.domain.ChessGame;
import chess.domain.piece.pawn.Pawn;
import chess.domain.square.Square;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameServiceTest {

    private final MockGameDao mockGameDao = new MockGameDao();
    private final MockMemberDao mockMemberDao = new MockMemberDao();

    @BeforeEach
    void beforeEach() {
        mockGameDao.deleteAll();
        mockMemberDao.deleteAll();
    }

    @DisplayName("새 게임을 생성해서 저장소에 저장한다.")
    @Test
    void createGame() {
        final GameService gameService = new GameService(mockGameDao, mockMemberDao);
        final MemberService memberService = new MemberService(mockMemberDao);
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("루피");

        final Long gameId = gameService.createGame("테스트 방", "1234", member1Id, member2Id);

        assertThat(gameService.findByGameId(gameId).getWhiteId()).isEqualTo(member1Id);
    }

    @DisplayName("현재 진행중인 게임 리스트를 반환한다.")
    @Test
    void findPlayingGames() {
        final GameService gameService = new GameService(mockGameDao, mockMemberDao);
        final MemberService memberService = new MemberService(mockMemberDao);
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("루피");

        gameService.createGame("테스트 방", "1234", member1Id, member2Id);
        gameService.createGame("테스트 방", "1234", member1Id, member2Id);
        gameService.createGame("테스트 방", "1234", member1Id, member2Id);

        assertThat(gameService.findPlayingGames().size()).isEqualTo(3);
    }

    @DisplayName("특정 멤버의 id로 그 멤버가 참여했던 종료된 게임 목록을 불러온다.")
    @Test
    void findHistoriesByMemberId() {
        final GameService gameService = new GameService(mockGameDao, mockMemberDao);
        final MemberService memberService = new MemberService(mockMemberDao);
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("루피");
        final Long game1Id = gameService.createGame("테스트 방", "1234", member1Id, member2Id);
        final Long game2Id = gameService.createGame("테스트 방", "1234", member1Id, member2Id);
        final Long game3Id = gameService.createGame("테스트 방", "1234", member1Id, member2Id);

        gameService.findByGameId(game1Id).terminate();
        gameService.findByGameId(game2Id).terminate();

        assertThat(gameService.findHistoriesByMemberId(member1Id).size()).isEqualTo(2);
    }

    @DisplayName("특정 게임의 움직임 명령을 수행한다.")
    @Test
    void move() {
        final GameService gameService = new GameService(mockGameDao, mockMemberDao);
        final MemberService memberService = new MemberService(mockMemberDao);
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("루피");
        final Long gameId = gameService.createGame("테스트 방", "1234", member1Id, member2Id);

        gameService.move(gameId, "a2", "a4");
        final ChessGame game = gameService.findByGameId(gameId);

        assertThat(game.getBoard().getPieceAt(Square.from("a4"))).isInstanceOf(Pawn.class);
    }

    @DisplayName("특정 게임을 삭제한다.")
    @Test
    void deleteGame() {
        final GameService gameService = new GameService(mockGameDao, mockMemberDao);
        final MemberService memberService = new MemberService(mockMemberDao);
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("루피");
        final Long gameId = gameService.createGame("테스트 방", "1234", member1Id, member2Id);

        gameService.deleteGame(gameId);

        assertThatThrownBy(() -> gameService.findByGameId(gameId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("찾는 게임이 존재하지 않습니다.");
    }
}