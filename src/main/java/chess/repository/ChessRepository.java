package chess.repository;

import chess.domain.board.ChessBoard;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceMapper;
import chess.dto.ChessBoardDTO;
import chess.dto.RoomIdDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChessRepository {

    private JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoomIdDTO addGame(ChessGame chessGame) {
        String addQuery = "INSERT INTO chess_game (turn, finished, board) VALUES (?, ?, ?)";
        jdbcTemplate.update(addQuery, chessGame.getTurn(), chessGame.isOver(), serialize(chessGame));
        String findQuery = "SELECT count(*) FROM chess_game";
        return new RoomIdDTO(jdbcTemplate.queryForObject(findQuery, Integer.class));
    }

    public ChessBoardDTO loadGame(int gameId) {
        String query = "SELECT board FROM chess_game WHERE id= ?";
        ChessBoardDTO chessBoardDTO = deserialize(jdbcTemplate.queryForObject(query, String.class, gameId));
        return chessBoardDTO;
    }


    //질문 여기가 맞는지?
    public String serialize(ChessGame chessGame) {
        return chessGame.getChessBoardMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getName() + entry.getKey().getStringPosition())
                .collect(Collectors.joining());
    }

    public ChessBoardDTO deserialize(String response) {
        Map<String, String> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += 3) {
            String name = response.substring(i, i + 1);
            String position = response.substring(i + 1, i + 3);
            chessBoard.put(position, name);
        }
        return new ChessBoardDTO(chessBoard);
    }
}
