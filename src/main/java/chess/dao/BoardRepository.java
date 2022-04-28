package chess.dao;

import chess.domain.member.Member;
import chess.domain.pieces.Color;
import chess.entities.ChessGame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

    private final BoardDao<ChessGame> boardDao;
    private final MemberDao<Member> memberDao;

    public BoardRepository(BoardDao<ChessGame> boardDao, MemberDao<Member> memberDao) {
        this.boardDao = boardDao;
        this.memberDao = memberDao;
    }

    public ChessGame getById(int id) {
        ChessGame byId = boardDao.getById(id);
        List<Member> allByBoardId = memberDao.getAllByBoardId(id);
        return new ChessGame(byId.getId(), byId.getRoomTitle(), byId.getTurn(), allByBoardId, byId.getPassword());
    }

    private ChessGame makeBoard(ResultSet resultSet) throws SQLException {
        return new ChessGame(
                resultSet.getInt("id"),
                resultSet.getString("room_title"),
                Color.findColor(resultSet.getString("turn")),
                memberDao.getAllByBoardId(resultSet.getInt("id")),
                resultSet.getString("password"));
    }
}
