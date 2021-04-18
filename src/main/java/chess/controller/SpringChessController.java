package chess.controller;

import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import chess.dto.*;
import chess.repository.room.DuplicateRoomNameException;
import chess.repository.room.NoSuchRoomNameException;
import chess.service.SpringChessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    public ModelAndView game(@RequestParam("name") String name, ModelAndView modelAndView) {
        ChessGame chessGame = springChessService.initializeRoom(name);
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        modelAndView.addObject("name", name);
        return modelAndView;
    }

    @PostMapping(value = "/game/move", consumes = "application/json")
    public ModelAndView move(@RequestBody MoveRequestDto moveRequestDto, ModelAndView modelAndView) {
        ChessGame chessGame = springChessService.movePiece(moveRequestDto);
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        return modelAndView;
    }

    @GetMapping(value = "/rooms")
    public ModelAndView rooms(ModelAndView modelAndView) {
        List<String> roomNames = springChessService.getAllSavedRooms();
        modelAndView.addObject("roomNames", roomNames);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @PostMapping("/game/load")
    public ModelAndView load(@RequestParam("roomName") String roomName, ModelAndView modelAndView) {
        ChessGame chessGame = springChessService.loadRoom(roomName);
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        modelAndView.addObject("name", roomName);
        return modelAndView;
    }

    private void addChessGame(ModelAndView modelAndView, ChessGame chessGame) {
        Color turn = chessGame.getTurn();
        modelAndView.addObject("turn", new TurnDto(turn));

        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            modelAndView.addObject(entry.getKey().getPosition(), new PieceDto(entry.getValue()));
        }

        Result result = chessGame.calculateResult();
        modelAndView.addObject("score", new ScoreDto(result));
        if (!chessGame.isOngoing()) {
            modelAndView.addObject("outcome", new OutcomeDto(result));
        }
    }

    @ExceptionHandler(DuplicateRoomNameException.class)
    public ModelAndView gameHandleError(HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("alert", req.getParameter("name") + "는 이미 존재하는 방입니다.");
        modelAndView.setViewName("index");
        modelAndView.setStatus(HttpStatus.CONFLICT);
        return modelAndView;
    }

    @ExceptionHandler(NoSuchRoomNameException.class)
    public String loadHandleError() {
        return "repository";
    }
}
