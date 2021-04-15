package chess.dao.jdbcspark;

import chess.dao.BackupBoardDao;
import chess.dao.RoomDao;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.exceptions.DatabaseException;
import chess.domain.piece.PieceColor;
import chess.dto.SquareDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static chess.dao.jdbcspark.DbConnection.getConnection;

public class BackupBoardDaoJdbc implements BackupBoardDao {

    public void savePlayingBoard(String name, Board board, PieceColor turnColor) {
        deleteExistingBoard(name);

        updateTurnColor(name, turnColor);
        addPlayingBoard(name, board);
    }

    @Override
    public void deleteExistingBoard(String name) {
        String query = "DELETE FROM backup_board WHERE room_name = ?";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt =
                createPreparedStatementWithOneParameter(con.prepareStatement(query), name)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    private void updateTurnColor(String roomName, PieceColor turnColor) {
        RoomDao roomDaoJdbc = new RoomDaoJdbc();
        roomDaoJdbc.updateTurn(roomName, turnColor);
    }

    public void addPlayingBoard(String name, Board board) {
        String query = "INSERT INTO backup_board VALUES (?, ?, ?)";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {

            board.positions()
                .forEach(position -> addSquare(pstmt, name, position, board));

            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseException();
        }

    }

    private void addSquare(PreparedStatement pstmt, String name, Position position, Board board) {
        try {
            pstmt.setString(1, name);
            pstmt.setString(2, position.toString());
            pstmt.setString(3, board.pieceAtPosition(position).toString());
            pstmt.addBatch();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public List<SquareDto> findPlayingBoardByRoom(String name) {
        String query = "SELECT backup_board.position, backup_board.piece FROM backup_board " +
            "WHERE backup_board.room_name = ?";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = createPreparedStatementWithOneParameter(
                con.prepareStatement(query), name);
            ResultSet rs = pstmt.executeQuery()) {

            List<SquareDto> squareDtos = new ArrayList<>();

            while (rs.next()) {
                addSquareToBoard(rs, squareDtos);
            }

            return squareDtos;
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    private void addSquareToBoard(ResultSet rs, List<SquareDto> squareDtos) throws SQLException {
        squareDtos.add(new SquareDto(rs.getString("position"), rs.getString("piece")));
    }

    private PreparedStatement createPreparedStatementWithOneParameter(
        PreparedStatement ps, String param) throws SQLException {
        ps.setString(1, param);
        return ps;
    }
}
