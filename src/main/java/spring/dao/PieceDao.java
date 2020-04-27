package spring.dao;

import spring.chess.board.ChessBoard;
import spring.chess.location.Location;
import spring.chess.piece.type.Piece;
import spring.db.DBConnection;
import spring.vo.PieceVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PieceDao {
    public void addPiece(PieceVo pieceVO) throws SQLException {
        String query = "INSERT INTO piece(game_id, name, row, col) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, pieceVO.getGameId());
            pstmt.setString(2, pieceVO.getName());
            pstmt.setInt(3, pieceVO.getRow());
            pstmt.setString(4, pieceVO.getCol());

            pstmt.executeUpdate();
        }
    }

    public void deleteGame(int gameId) throws SQLException {
        String query = "DELETE FROM piece where game_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, String.valueOf(gameId));
            pstmt.execute();
        }
    }

    public List<PieceVo> findAll(int gameId) throws SQLException {
        String query = "SELECT * FROM piece WHERE game_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            return getPieceVos(rs);
        }
    }

    private List<PieceVo> getPieceVos(ResultSet resultSet) throws SQLException {
        ArrayList<PieceVo> pieceVos = new ArrayList<>();
        while (resultSet.next()) {
            PieceVo pieceVO = new PieceVo(
                    resultSet.getInt("game_id"),
                    resultSet.getString("name"),
                    resultSet.getInt("row"),
                    resultSet.getString("col")
            );
            pieceVos.add(pieceVO);
        }
        return pieceVos;
    }

    public void update(Location now, Location destination, Piece piece, int gameId) throws SQLException {
        String query = "UPDATE piece SET row = ?, col = ?, name = ? where row = ? AND col = ? AND game_id = ?;";

        piece.toString();
        piece.getName();
        String.valueOf(piece.getName());
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, String.valueOf(destination.getRowValue()));
            pstmt.setString(2, String.valueOf(destination.getColValue()));
            pstmt.setString(3, String.valueOf(piece.getName()));

            pstmt.setString(4, String.valueOf(now.getRowValue()));
            pstmt.setString(5, String.valueOf(now.getColValue()));

            pstmt.setString(6, String.valueOf(gameId));
            pstmt.executeUpdate();
        }
    }

    public void delete(Location destination, int gameId) throws SQLException {
        String query = "DELETE FROM piece WHERE row = ? AND col = ? AND game_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, String.valueOf(destination.getRowValue()));
            pstmt.setString(2, String.valueOf(destination.getColValue()));
            pstmt.setString(3, String.valueOf(gameId));

            pstmt.execute();
        }
    }

    public void resetBoard(ChessBoard chessBoard, int gameId) throws SQLException {
        Map<Location, Piece> board = chessBoard.getBoard();
        PieceDao pieceDao = new PieceDao();

        pieceDao.deleteGame(gameId);
        addPieces(gameId, board, pieceDao);
    }

    // TODO : addBatch 활용해보자.
    private void addPieces(int gameId, Map<Location, Piece> board, PieceDao pieceDAO) throws SQLException {
        for (Map.Entry<Location, Piece> entry : board.entrySet()) {
            Location location = entry.getKey();
            Piece piece = entry.getValue();

            PieceVo pieceVo = new PieceVo(gameId
                    , Character.toString(piece.getName())
                    , location.getRowValue()
                    , Character.toString(location.getColValue()));

            pieceDAO.addPiece(pieceVo);
        }
    }
}
