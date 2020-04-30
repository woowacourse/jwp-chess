package spark.dao;

import chess.team.Team;
import spark.db.DBConnection;
import spark.dto.ChessGameVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChessGameDao {
    public ChessGameVo findChessGameBy(int gameId) throws SQLException {
        String query = "SELECT * FROM game WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return new ChessGameVo(
                    rs.getInt("id"),
                    rs.getString("white_name"),
                    rs.getString("black_name"),
                    rs.getInt("turn_is_black")
            );
        }
    }

    public void updateTurn(Team turn, int gameId) throws SQLException {
        String query = "UPDATE game SET turn_is_black = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, String.valueOf(turn.getTurnIsBlack()));
            pstmt.setString(2, String.valueOf(gameId));
            pstmt.executeUpdate();
        }
    }
}
