package chess.dao;

import chess.domain.member.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChessMemberDao implements MemberDao<Member> {

    private final ConnectionManager connectionManager;

    public ChessMemberDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Member save(String name, int boardId) {
        final String sql = "INSERT INTO member(name, board_id) VALUES (?, ?)";
        return connectionManager.executeQuery(preparedStatement -> {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, boardId);
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new IllegalArgumentException(boardId + "의 " + name + "멤버 저장에 실패하였습니다.");
            }

            return new Member(generatedKeys.getInt(1), name, boardId);
        }, sql);
    }

    @Override
    public void saveAll(List<Member> members, int boardId) {
        for (Member member : members) {
            save(member.getName(), boardId);
        }
    }

    @Override
    public List<Member> getAllByBoardId(int boardId) {
        final String sql = "SELECT * FROM member WHERE board_id=?";
        return connectionManager.executeQuery(preparedStatement -> {
            List<Member> members = new ArrayList<>();
            preparedStatement.setInt(1, boardId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                members.add(makeMember(resultSet));
            }

            return members;
        }, sql);
    }

    private Member makeMember(ResultSet resultSet) throws SQLException {
        return new Member(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("board_id")
        );
    }
}
