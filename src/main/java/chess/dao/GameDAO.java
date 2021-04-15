package chess.dao;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.dto.ChessBoardDto;
import chess.dto.PieceDto;
import chess.dto.SavedGameData;
import chess.exception.NoSavedGameException;

import java.sql.*;
import java.util.Map;

public class GameDAO {
    private static final int UNIQUE_GAME_ID = 1;

    private final Serializer serializer = Serializer.getInstance();

    public Connection getConnection() {
        Connection con = null;
        String server = "localhost:13306";  // hostname:port number
        String database = "chess_db"; // database 이름
        String option = "?useSSL=false&serverTimezone=UTC";
        String userName = "root";           // db userName
        String password = "root";           // db password

        // 드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }

        // 드라이버 연결
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + option, userName, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("연결 오류:" + e.getMessage());
            e.printStackTrace();
        }

        return con;
    }

    public int saveGame(ChessGameManager chessGameManager) throws SQLException {
        ChessBoardDto chessBoardDto = ChessBoardDto.from(chessGameManager.getBoard());
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();

        int gameId;
        String insertGameQuery = "insert into game(turn) values(?)";
        String findGameIdQuery = "select last_insert_id()";

        try (Connection connection = getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(insertGameQuery);
            pstmt.setString(1, currentTurnColor.name());
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement(findGameIdQuery);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) throw new SQLException();
            gameId = rs.getInt(1);
        }

        Map<String, PieceDto> board = chessBoardDto.board();
        for (String position : board.keySet()) {
            String query = "insert into piece(game_id, name, color, position) values(?, ?, ?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, gameId);
                PieceDto pieceDto = board.get(position);
                pstmt.setString(2, pieceDto.getName());
                pstmt.setString(3, pieceDto.getColor());
                pstmt.setString(4, position);
                pstmt.executeUpdate();
            }
        }

        return gameId;
    }

    public SavedGameData loadGame() throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM game WHERE game_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, UNIQUE_GAME_ID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ChessBoardDto chessBoardDto = serializer.fromJson(rs.getString("board"), ChessBoardDto.class);
                Color currentTurnColor = serializer.fromJson(rs.getString("turn"), Color.class);
                return new SavedGameData(chessBoardDto, currentTurnColor);
            }
            throw new NoSavedGameException("저장된 게임을 찾을 수 없습니다.");
        }
    }
}
