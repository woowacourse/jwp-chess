package chess.repository;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceMapper;
import chess.dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChessRepository {
    private static final int LAST_INDEX_OF_EACH_PIECE = 3;
    private static final int STARTING_INDEX_OF_POSITION = 1;
    private JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public RoomIdDTO addGame(ChessGame chessGame) {
        String addingGameQuery = "INSERT INTO chess_game (turn, finished, board) VALUES (?, ?, ?)";
        jdbcTemplate.update(addingGameQuery, chessGame.getTurn(), chessGame.isOver(), serialize(chessGame));
        String findingGameQuery = "SELECT count(*) FROM chess_game";
        return new RoomIdDTO(jdbcTemplate.queryForObject(findingGameQuery, String.class));
    }

    //DTO 를 컨트롤러에서 만든다.
    public ChessBoardDTO loadGameAsDTO(String gameId) {
        String loadingGameQuery = "SELECT board FROM chess_game WHERE id= ?";
        ChessBoardDTO chessBoardDTO = deserializeAsDTO(jdbcTemplate.queryForObject(loadingGameQuery, String.class, gameId));
        return chessBoardDTO;
    }
    //체스게임을 리턴한다, 디티오 말고.
    public ChessGame loadGame(String gameId) {
        String findingGameQuery = "SELECT board, turn FROM chess_game WHERE id= ?";
        return jdbcTemplate.queryForObject(findingGameQuery, (resultSet, rowNum) -> {
            ChessGame chessGame = new ChessGame(
                    deserialize(resultSet.getString("board")),
                    Color.of(resultSet.getString("turn")));
            return chessGame;
        }, gameId);
    }

    public TurnDTO turn(String gameId) {
        String findingTurnQuery = "SELECT turn FROM chess_game WHERE id = ?";
        TurnDTO turnDTO = new TurnDTO(jdbcTemplate.queryForObject(findingTurnQuery, String.class, gameId));
        return turnDTO;
    }

    public void saveGame(String gameId, ChessGame chessGame) {
        String savingGameQuery = "UPDATE chess_game SET turn = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(savingGameQuery, chessGame.getTurn(), serialize(chessGame), gameId);
    }

    //질문 여기가 맞는지?
    public String serialize(ChessGame chessGame) {
        return chessGame.getChessBoardMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getName() + entry.getKey().getStringPosition())
                .collect(Collectors.joining());
    }
    //얘도 서비스로 ㄲㄲ
    public ChessBoard deserialize(String response) {
        Map<Position, Piece> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += LAST_INDEX_OF_EACH_PIECE) {
            char name = response.charAt(i);
            String position = response.substring(i + STARTING_INDEX_OF_POSITION, i + LAST_INDEX_OF_EACH_PIECE);
            Position piecePosition = Position.of(position);
            Piece piece = PieceMapper.of(name);
            chessBoard.put(piecePosition, piece);
        }
        return new ChessBoard(chessBoard);
    }

    public ChessBoardDTO deserializeAsDTO(String response) {
        Map<String, String> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += LAST_INDEX_OF_EACH_PIECE) {
            String name = response.substring(i, i + STARTING_INDEX_OF_POSITION);
            String position = response.substring(i + STARTING_INDEX_OF_POSITION, i + LAST_INDEX_OF_EACH_PIECE);
            chessBoard.put(position, name);
        }
        return new ChessBoardDTO(chessBoard);
    }

    public FinishDTO isFinished(String gameId) {
        String finishedQuery = "SELECT finished FROM chess_game WHERE id = ?";
        FinishDTO finishDTO = new FinishDTO(jdbcTemplate.queryForObject(finishedQuery, Boolean.class, gameId));
        return finishDTO;
    }

    public void finish(String gameId) {
        String savingGameQuery = "UPDATE chess_game SET finished = ? WHERE id = ?";
        jdbcTemplate.update(savingGameQuery, true, gameId);
    }

    public void restart(String gameId, ChessGame chessGame) {
        String restartQuery = "UPDATE chess_game SET turn = ?, finished = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(restartQuery, chessGame.getTurn(), chessGame.isOver(), serialize(chessGame), gameId);
    }
}
