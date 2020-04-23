package wooteco.chess.controller;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Route;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.service.ChessService;

public class WebUIChessController {
    private final ChessService service;

    public WebUIChessController(ChessService service) {
        this.service = service;
    }

    public Route getNewChessGameRoute() {
        return (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.queryParams("error") != null) {
                model.put("error", req.queryParams("error"));
            }
            return render(model, "chess-before-start.html");
        };
    }

    public Route initChessGameRoute() {
        return (req, res) -> {
            int roomId = service.createBoard(req.queryParams("new-room-name"));
            Board board = service.getSavedBoard(roomId);
            Map<String, Object> model = createBasicModel(roomId, board);
            return render(model, "chess-running.html");
        };
    }

    public Route getChessGameRoute() {
        return (req, res) -> {
            int roomId = Integer.parseInt(req.queryParams("room-id"));
            Board board = service.getSavedBoard(roomId);
            Map<String, Object> model = createBasicModel(roomId, board);
            if (!board.isBothKingAlive()) {
                res.redirect("/result?room-id=" + roomId);
            }
            if (req.queryParams("error") != null) {
                model.put("error", req.queryParams("error"));
            }
            return render(model, "chess-running.html");
        };
    }

    public Route getResultRoute() {
        return (req, res) -> {
            int roomId = Integer.parseInt(req.queryParams("room-id"));
            Board board = service.getSavedBoard(roomId);
            Map<String, Object> model = allocatePiecesOnMap(board);
            model.put("winner", board.getWinner().getName());
            return render(model, "chess-result.html");
        };
    }

    public Route findChessGameRoute() {
        return (req, res) -> {
            int roomId = Integer.parseInt(req.queryParams("existing-room-name"));
            try {
                Board board = service.getSavedBoard(roomId);
                Map<String, Object> model = createBasicModel(roomId, board);
                if (!board.isBothKingAlive()) {
                    res.redirect("/result?room-id=" + roomId);
                }
                return render(model, "chess-running.html");
            } catch (Exception e) {
                res.redirect("/new?error=true");
            }
            return null;
        };
    }

    public Route postMoveRoute() {
        return (req, res) -> {
            int roomId = Integer.parseInt(req.queryParams("room-id"));
            Board board = service.getSavedBoard(roomId);
            String source = req.queryParams("source");
            String destination = req.queryParams("destination");
            try {
                service.processMoveInput(board, source, destination, roomId);
                res.redirect("/?room-id=" + roomId);
            } catch (Exception e) {
                res.redirect("/?room-id=" + roomId + "&error=true");
            }
            return null;
        };
    }

    public Route postInitializeRoute() {
        return (req, res) -> {
            int roomId = Integer.parseInt(req.queryParams("room-id"));
            service.initBoard(roomId);
            res.redirect("/?room-id=" + roomId);
            return null;
        };
    }

    private Map<String, Object> allocatePiecesOnMap(Board board) {
        Map<String, Object> model = new HashMap<>();
        Pieces pieces = board.getPieces();
        Map<Position, Piece> positionPieceMap = pieces.getPieces();
        Map<String, Piece> pieceMap = new HashMap<>();
        for (Position position : positionPieceMap.keySet()) {
            pieceMap.put(position.toString(), positionPieceMap.get(position));
        }
        model.put("map", pieceMap);
        return model;
    }

    private Map<String, Object> createBasicModel(int roomId, Board board) {
        Map<String, Object> model = allocatePiecesOnMap(board);
        model.put("teamWhiteScore", board.calculateScoreByTeam(Team.WHITE));
        model.put("teamBlackScore", board.calculateScoreByTeam(Team.BLACK));
        model.put("id", roomId);
        model.put("turn", board.getTurn());
        return model;
    }

    private String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
