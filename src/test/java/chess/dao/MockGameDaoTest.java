package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.Room;
import chess.domain.piece.detail.Team;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MockGameDaoTest {

    private final MockGameDao dao = new MockGameDao();

    @Test
    @DisplayName("체스 게임을 저장소에 저장한다.")
    void save() {
        final Member memberOne = new Member(1L, "썬");
        final Member memberTwo = new Member(2L, "알렉스");
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(memberOne, memberTwo);
        final Room room = new Room("some", "123", participant);
        final ChessGame game = new ChessGame(2L, board, Team.WHITE, room);

        final ChessGame savedGame = dao.save(game);

        assertThat(savedGame.getTurn()).isEqualTo(game.getTurn());
    }

    @Test
    @DisplayName("저장소에 저장된 모든 게임을 불러온다.")
    void findAll() {
        List<ChessGame> games = new ArrayList<>();

        final Member memberOne = new Member(1L, "썬");
        final Member memberTwo = new Member(2L, "알렉스");
        final Board board = new Board(BoardInitializer.create());
        final Participant participant = new Participant(memberOne, memberTwo);
        final Room room = new Room("some", "123", participant);

        games.add(new ChessGame(board, Team.WHITE, room));
        games.add(new ChessGame(board, Team.WHITE, room));
        games.add(new ChessGame(board, Team.WHITE, room));

        for (ChessGame game : games) {
            dao.save(game);
        }

        assertThat(dao.findAll().size()).isEqualTo(games.size());
    }
}
