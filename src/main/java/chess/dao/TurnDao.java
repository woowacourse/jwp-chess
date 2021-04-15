package chess.dao;

import chess.dao.setting.DBConnection;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.request.TurnRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TurnDao extends DBConnection {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TurnDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initializeTurn() {
        String query = "INSERT INTO turn (current_turn) VALUE (?)";
        try (
                Connection connection = getConnection();
                PreparedStatement psmt = connection.prepareStatement(query)
        ) {
            psmt.setString(1, "white");
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TurnRequestDto> showCurrentTurn() {
        List<TurnRequestDto> turn = new ArrayList<>();
        String query = "SELECT * FROM turn";
        try (
                Connection connection = getConnection();
                PreparedStatement psmt = connection.prepareStatement(query);
                ResultSet rs = psmt.executeQuery()
        ) {
            while (rs.next()) {
                turn.add(new TurnRequestDto(
                        rs.getLong("id"),
                        rs.getString("current_turn")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turn;
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        String query = "UPDATE turn SET current_turn=? WHERE current_turn=?";
        try (
                Connection connection = getConnection();
                PreparedStatement psmt = connection.prepareStatement(query)
        ) {
            psmt.setString(1, turnChangeRequestDto.getNextTurn());
            psmt.setString(2, turnChangeRequestDto.getCurrentTurn());
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTurn() {
        String query = "DELETE FROM turn";
        try (
                Connection connection = getConnection();
                PreparedStatement pstm = connection.prepareStatement(query)
        ) {
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
