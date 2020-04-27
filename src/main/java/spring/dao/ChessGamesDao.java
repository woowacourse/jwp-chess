package spring.dao;

import spring.db.DBConnection;
import spring.dto.ChessGameVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChessGamesDao {
    public ArrayList<ChessGameVo> findAll() throws SQLException {
        String query = "SELECT * FROM game;";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {
            ArrayList<ChessGameVo> chessGameVos = getChessGameDtos(rs);
            return chessGameVos;
        }
    }

    private ArrayList<ChessGameVo> getChessGameDtos(ResultSet resultSet) throws SQLException {
        ArrayList<ChessGameVo> chessGameVos = new ArrayList<>();
        while (resultSet.next()) {
            ChessGameVo chessGameVo = new ChessGameVo(
                    resultSet.getInt("id"),
                    resultSet.getString("white_name"),
                    resultSet.getString("black_name"),
                    resultSet.getInt("turn_is_black"));
            chessGameVos.add(chessGameVo);
        }
        return chessGameVos;
    }
}
