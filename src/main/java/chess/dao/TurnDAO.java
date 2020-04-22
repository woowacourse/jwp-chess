package chess.dao;

import chess.domain.piece.PieceColor;
import chess.dto.TurnDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TurnDAO {
    private static TurnDAO instance = new TurnDAO();

    private TurnDAO() {
    }

    public static TurnDAO getInstance() {
        return instance;
    }

    public void saveTurn(TurnDTO turnDTO) throws SQLException {
        String query = "INSERT INTO turn VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, turnDTO.getCurrentTeam());
            pstmt.executeUpdate();
        }
    }

    public void deletePreviousTurn() throws SQLException {
        String query = "DELETE FROM turn";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
        }
    }

    public TurnDTO findTurn() throws SQLException {
        String query = "SELECT * FROM turn";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            return TurnDTO.from(rs.getString("teamName"));
        }
    }

    public void updateTurn(TurnDTO turnDTO) throws SQLException {
        String teamName = turnDTO.getCurrentTeam();
        String oppositeTeamName = PieceColor.change(teamName).getName();

        String query = "UPDATE turn SET teamName = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, oppositeTeamName);
            pstmt.executeUpdate();
        }
    }
}
