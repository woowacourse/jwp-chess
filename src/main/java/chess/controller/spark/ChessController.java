package chess.controller.spark;

import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.dto.MoveRequestDTO;
import chess.dto.ResultDTO;
import chess.dto.board.BoardDTO;
import chess.service.spark.ChessService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class ChessController {
    private static final Gson GSON = new Gson();

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    public JsonElement showChessBoard(Request request, Response response) throws SQLException {
        BoardDTO boardDTO = findChessBoard();
        return GSON.toJsonTree(boardDTO);
    }

    private BoardDTO findChessBoard() throws SQLException {
        ChessBoard chessBoard = chessService.findChessBoard();
        TeamType teamType = chessService.findCurrentTeamType();
        return BoardDTO.of(chessBoard, teamType);
    }

    public JsonElement move(Request request, Response response) throws SQLException {
        MoveRequestDTO moveRequestDTO = GSON.fromJson(request.body(), MoveRequestDTO.class);
        String current = moveRequestDTO.getCurrent();
        String destination = moveRequestDTO.getDestination();
        String teamType = moveRequestDTO.getTeamType();
        chessService.move(current, destination, teamType);
        BoardDTO boardDTO = findChessBoard();
        return GSON.toJsonTree(boardDTO);
    }

    public JsonElement showResult(Request request, Response response) throws SQLException {
        Result result = chessService.calculateResult();
        ResultDTO resultDTO = ResultDTO.from(result);
        return GSON.toJsonTree(resultDTO);
    }

    public JsonElement restart(Request request, Response response) throws SQLException {
        chessService.deleteAllHistories();
        return GSON.toJsonTree("/");
    }
}
