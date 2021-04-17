package chess.controller;

import chess.config.ChessRoom;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import chess.dto.OutcomeDto;
import chess.dto.PieceDto;
import chess.dto.ScoreDto;
import chess.dto.TurnDto;
import chess.repository.room.Room;
import chess.service.SpringChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    @Autowired
    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/game")
    public ModelAndView game(@RequestParam("name") String name, ModelAndView modelAndView) {
        Optional<ChessGame> chessGameOptional = springChessService.createRoom(name);
        if (chessGameOptional.isPresent()) {
            addChessGame(modelAndView, chessGameOptional.get());
            modelAndView.setViewName("game");
            modelAndView.addObject("name", name);
            return modelAndView;
        }
        modelAndView.addObject("alert", name + "는 이미 존재하는 방입니다.");
        modelAndView.setViewName("index");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @PostMapping(value = "/game/move", consumes = "text/plain")
    public ModelAndView move(@RequestBody String command, ModelAndView modelAndView) {
        Optional<ChessGame> chessGameOptional = springChessService.movePiece(command);
        if (chessGameOptional.isPresent()) {
            addChessGame(modelAndView, chessGameOptional.get());
            modelAndView.setViewName("game");
            return modelAndView;
        }
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @GetMapping(value = "/rooms")
    public ModelAndView rooms(ModelAndView modelAndView) {
        List<String> roomNames = springChessService.getAllSavedRooms();
        modelAndView.addObject("roomNames", roomNames);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @PostMapping(value = "/game/save")
    public ResponseEntity<String> save(@ChessRoom Room room) {
        boolean isSaved = springChessService.saveRoom(room);
        String response = Boolean.toString(isSaved);
        if (isSaved) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/game/load")
    public ModelAndView load(@RequestParam("roomName") String roomName, ModelAndView modelAndView) {
        Optional<ChessGame> chessGameOptional = springChessService.loadRoom(roomName);
        if (chessGameOptional.isPresent()) {
            addChessGame(modelAndView, chessGameOptional.get());
            modelAndView.setViewName("game");
            modelAndView.addObject("name", roomName);
        }
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
}
