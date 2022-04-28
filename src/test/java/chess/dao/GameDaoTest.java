package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.game.GameDao;
import chess.dao.game.PieceDao;
import chess.dao.game.SpringJdbcGameDao;
import chess.dao.game.SpringJdbcPieceDao;
import chess.dao.member.MemberDao;
import chess.dao.member.SpringJdbcMemberDao;
import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.Room;
import chess.domain.piece.detail.Team;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class GameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GameDao gameDao;
    private MemberDao memberDao;
    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        memberDao = new SpringJdbcMemberDao(jdbcTemplate);
        pieceDao = new SpringJdbcPieceDao(jdbcTemplate);
        gameDao = new SpringJdbcGameDao(jdbcTemplate, pieceDao, memberDao);
    }

    @Test
    @DisplayName("체스 게임을 저장소에 저장한다.")
    void save() {
        Member memberOne = new Member("썬");
        final Long memberOneId = memberDao.save(memberOne);
        memberOne = new Member(memberOneId, memberOne.getName());
        Member memberTwo = new Member("알렉스");
        final Long memberTwoId = memberDao.save(memberTwo);
        memberTwo = new Member(memberTwoId, memberTwo.getName());
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(memberOne, memberTwo);
        final String title = "테스트 방";
        final String password = "1234";
        final ChessGame game = new ChessGame(board, Team.WHITE, new Room(title, password, participant));

        final Long gameId = gameDao.save(game);

        assertThat(gameDao.findById(gameId).get().getTurn()).isEqualTo(game.getTurn());
    }

    @Test
    @DisplayName("저장소에 저장된 모든 게임을 불러온다.")
    void findAll() {
        List<ChessGame> games = new ArrayList<>();

        Member memberOne = new Member("썬");
        final Long memberOneId = memberDao.save(memberOne);
        memberOne = new Member(memberOneId, memberOne.getName());
        Member memberTwo = new Member("알렉스");
        final Long memberTwoId = memberDao.save(memberTwo);
        memberTwo = new Member(memberTwoId, memberTwo.getName());
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(memberOne, memberTwo);
        final String title = "테스트 방";
        final String password = "1234";

        games.add(new ChessGame(board, Team.WHITE, new Room(title, password, participant)));
        games.add(new ChessGame(board, Team.WHITE, new Room(title, password, participant)));
        games.add(new ChessGame(board, Team.WHITE, new Room(title, password, participant)));

        for (ChessGame game : games) {
            gameDao.save(game);
        }

        assertThat(gameDao.findAll().size()).isEqualTo(games.size());
    }
}
