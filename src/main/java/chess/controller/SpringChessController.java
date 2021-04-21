package chess.controller;

import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import chess.dto.*;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/game")
    public String game(@RequestParam("roomName") String roomName) {
        long id = springChessService.saveRoom(roomName);

        return "redirect:/game/" + roomName + "-" + id;
    }

    @GetMapping("/game/{roomNameAndId}")
    public ModelAndView enterRoom(@PathVariable("roomNameAndId") String roomNameAndId) {
        String[] split = roomNameAndId.split("-");
        String roomName = split[0];
        long id = Long.parseLong(split[1]);

        ChessGame chessGame = springChessService.loadRoom(id);

        ModelAndView modelAndView = new ModelAndView();
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        modelAndView.addObject("roomName", roomName);

        return modelAndView;
    }

    @PostMapping(value = "/game/*/move", consumes = "application/json")
    public ModelAndView move(@RequestBody MoveRequestDto moveRequestDto) {
        ChessGame chessGame = springChessService.movePiece(moveRequestDto);

        ModelAndView modelAndView = new ModelAndView();
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        return modelAndView;
    }

    @GetMapping(value = "/rooms")
    public ModelAndView rooms() {
        List<String> roomNames = springChessService.getAllSavedRooms();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomNames", roomNames);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @PostMapping("/load")
    public String load(@RequestParam("roomName") String roomName) {
        long id = springChessService.getRoomId(roomName);

        return "redirect:/game/" + roomName + "-" + id;
    }

    @DeleteMapping(value = "/delete/{roomName}")
    @ResponseBody
    public ResponseEntity<DeleteResponseDto> delete(@PathVariable("roomName") String roomName) {
        springChessService.deleteRoom(roomName);
        DeleteResponseDto deleteResponseDto = new DeleteResponseDto(roomName, true);
        return ResponseEntity.ok().body(deleteResponseDto);
    }

    private void addChessGame(ModelAndView modelAndView, ChessGame chessGame) {
        Color turn = chessGame.getTurn();
        modelAndView.addObject("turn", new TurnResponseDto(turn));

        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            modelAndView.addObject(entry.getKey().getPosition(), new PieceResponseDto(entry.getValue()));
        }

        Result result = chessGame.calculateResult();
        modelAndView.addObject("score", new ScoreResponseDto(result));
        if (!chessGame.isOngoing()) {
            modelAndView.addObject("outcome", new OutcomeResponseDto(result));
        }
    }
}
