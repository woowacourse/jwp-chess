package chess;

import chess.controller.ChessController;
import chess.dao.*;
import chess.dto.ResponseDto;
import chess.dto.ScoreDto;
import chess.model.board.Board;
import chess.model.member.Member;
import chess.model.piece.Piece;
import chess.model.room.Room;
import chess.model.square.Square;
import chess.service.ChessService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class SparkChessApplication {

    private static final ChessController chessController;

    static {
        ConnectionManager connectionManager = new ConnectionManager();
        BoardDao<Board> chessBoardDao = new ChessBoardDao(connectionManager);
        SquareDao<Square> chessSquareDao = new ChessSquareDao(connectionManager);
        PieceDao<Piece> chessPieceDao = new ChessPieceDao(connectionManager);
        RoomDao<Room> chessRoomDao = new ChessRoomDao(connectionManager);
        MemberDao<Member> chessMemberDao = new ChessMemberDao(connectionManager);
        ChessService chessService = new ChessService(chessBoardDao, chessSquareDao, chessPieceDao, chessRoomDao, chessMemberDao);
        chessController = new ChessController(chessService);
    }

    public static void main(String[] args) {

        staticFiles.location("/static");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("rooms", chessController.getRooms());
            return render(model, "home.html");
        });

        post("/room", (req, res) -> {
            final List<String> createRoomInput = Arrays.stream(req.body().strip().split("\n"))
                    .map(s -> s.split("=")[1])
                    .collect(Collectors.toList());
            final int roomId = chessController.startGame(
                    createRoomInput.get(0),
                    createRoomInput.get(1),
                    createRoomInput.get(2));
            res.redirect("/room/" + roomId);
            return null;
        });

        get("/room/:roomId", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("roomId", Integer.parseInt(req.params(":roomId")));
            model.put("board", chessController.getBoard(Integer.parseInt(req.params(":roomId"))));
            return render(model, "chess-game.html");
        });

        post("/room/:roomId/move", (req, res) -> {
            final String[] split = req.body().strip().split("=")[1].split(" ");
            String source = split[0];
            String target = split[1];
            final int roomId = Integer.parseInt(req.params(":roomId"));
            ResponseDto response = chessController.move(roomId, source, target);
            return response.toString();
        });

        get("/room/:roomId/status", (req, res) -> {
            final ScoreDto scoreDto = chessController.score(Integer.parseInt(req.params(":roomId")));
            return scoreDto.toString();
        });

        post("/room/:roomId/end", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            final int roomId = Integer.parseInt(req.params(":roomId"));
            model.put("result", chessController.score(roomId));
            chessController.end(roomId);
            return render(model, "result.html");
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
