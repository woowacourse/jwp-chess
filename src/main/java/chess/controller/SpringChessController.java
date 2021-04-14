package chess.controller;

import chess.dto.BoardDto;
import chess.dto.MovablePositionDto;
import chess.dto.MoveRequestDto;
import chess.service.ChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Map;

@Controller
public class SpringChessController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/start")
    public String startGame(@RequestParam("room") String id, Model model) {
        model.addAttribute("roomId", id);
        return "index";
    }

    @GetMapping("/create/{id}")
    @ResponseBody
    public ResponseEntity<BoardDto> createRoom(@PathVariable("id") String id) {
        BoardDto boardDto = chessService.loadRoom(id);
        return ResponseEntity.ok(boardDto);
    }

    public String move(Request request, Response response) throws JsonProcessingException {
        MoveRequestDto moveRequestDto = OBJECT_MAPPER.readValue(request.body(), MoveRequestDto.class);
        try {
            return OBJECT_MAPPER.writeValueAsString(chessService.move(moveRequestDto));
        } catch (Exception e) {
            return OBJECT_MAPPER.writeValueAsString(chessService.loadRoom(moveRequestDto.getRoomId()));
        }
    }

    public String movablePosition(Request request, Response response) throws JsonProcessingException {
        MovablePositionDto movablePositionDto = OBJECT_MAPPER.readValue(request.body(), MovablePositionDto.class);
        return OBJECT_MAPPER.writeValueAsString(chessService.movablePosition(movablePositionDto));
    }

    public String score(Request request, Response response) throws JsonProcessingException {
        MovablePositionDto movablePositionDto = OBJECT_MAPPER.readValue(request.body(), MovablePositionDto.class);
        return OBJECT_MAPPER.writeValueAsString(chessService.boardStatusDto(movablePositionDto.getRoomId()));
    }

    public String clear(Request request, Response response) {
        String roomNumber = request.params(":room");
        chessService.deleteRoom(roomNumber);
        response.redirect("/");
        return null;
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
