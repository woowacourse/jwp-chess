package wooteco.chess.controller;

import org.springframework.http.ResponseEntity;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.controller.dto.ChessPieceDto;
import wooteco.chess.controller.dto.ChessWebIndexDto;
import wooteco.chess.controller.dto.PieceDto;
import wooteco.chess.controller.dto.ResponseDto;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.service.ChessService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

public class ChessWebController {

    private ChessService chessService = new ChessService();

    public void run() {
        get("/", (req, res) -> {
            ChessWebIndexDto chessWebIndexDto = ChessWebIndexDto.of(chessService.getRoomId());
            return render(chessWebIndexDto, "index.html");
        });

        get("/chess/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id").trim());
            chessService.load(id);
            Map<String, Object> model = new HashMap<>();
            List<ChessPieceDto> pieces = getPieceDto(id);
            model.put("chessPiece", pieces);
            model.put("id", id);
            return render(model, "contents/chess.hbs");
        });

        post("/createChessGame", (req, res) -> chessService.createGame());

        post("/restart/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id").trim());
            chessService.restart(id);
            return ResponseEntity.ok();
        });

        post("/save/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id").trim());
            chessService.save(id);
            return ResponseEntity.ok();
        });

        post("/end/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id").trim());
            chessService.remove(id);
            return ResponseEntity.ok();
        });

        post("/move/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id").trim());
            List<String> parameters = Arrays.asList(req.queryParams("source"), req.queryParams("target"));
            chessService.move(id, parameters);
            return chessService.isEnd(id);
        });

        get("/status/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id").trim());
            Team team = chessService.getCurrentTeam(id);
            return team + "팀 " + chessService.getScore(id) + "점";
        });

        exception(RuntimeException.class, (exception, req, res) -> {
            res.status(400);
            res.body(exception.getMessage());
        });

        get("/movable/:id/:position", (req, res) -> {
            Long id = Long.valueOf(req.params(":id"));
            Position position = Position.of(req.params(":position"));
            List<Position> positions = chessService.getMovablePositions(id, position);
            return positions.stream().map(Position::getName).collect(Collectors.joining(","));
        });
    }

    private List<ChessPieceDto> getPieceDto(Long id) {
        ResponseDto responseDto = chessService.getResponseDto(id);
        Map<Position, PieceDto> board = responseDto.getBoard();
        return board.entrySet().stream()
                .map(entry ->
                        new ChessPieceDto(
                                entry.getKey().getName(),
                                entry.getValue().getPieceType(),
                                entry.getValue().getTeam())
                )
                .collect(Collectors.toList());
    }

    private static String render(Object object, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(object, templatePath));
    }
}
