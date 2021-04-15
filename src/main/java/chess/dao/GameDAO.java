package chess.dao;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessBoardDto;
import chess.dto.PieceDeserializeTable;
import chess.dto.PieceDto;
import chess.exception.NoSavedGameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class GameDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GameDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    private final RowMapper<ChessBoard> chessBoardRowMapper = (resultSet, rowNum) -> {
        Map<Position, Piece> board = new HashMap<>();

        do {
            Piece piece = PieceDeserializeTable.deserializeFrom(
                    resultSet.getString("name"),
                    Color.of(resultSet.getString("color"))
            );
            Position position = Position.of(resultSet.getString("position"));
            board.put(position, piece);
        } while(resultSet.next());
        return ChessBoard.from(board);
    };

    private final RowMapper<Color> colorRowMapper = (resultSet, rowNum) -> {
        return Color.of(resultSet.getString("turn"));
    };

    public ChessGameManager loadGame(int gameId) throws SQLException {
        String gameQuery = "SELECT turn FROM game WHERE game_id = ?";
        Color currentTurn = this.jdbcTemplate.queryForObject(gameQuery, colorRowMapper, gameId);

        if (currentTurn != null) {
            String queryPiece = "SELECT * FROM piece WHERE game_id = ?";
            ChessBoard chessBoard = this.jdbcTemplate.queryForObject(queryPiece, chessBoardRowMapper, gameId);

            ChessGameManager chessGameManager = new ChessGameManager();
            chessGameManager.load(chessBoard, currentTurn);
            return chessGameManager;
        }
        throw new NoSavedGameException("저장된 게임이 없습니다.");
    }
}
