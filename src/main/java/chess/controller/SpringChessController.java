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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

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

    @GetMapping("/game")
    public String game(@RequestParam("roomName") String roomName, RedirectAttributes redirectAttributes) {
        long id = springChessService.initializeRoom(roomName);
        redirectAttributes.addFlashAttribute("roomName", roomName);

        return "redirect:/game/" + id;
    }

    @GetMapping("/game/{id}")
    public ModelAndView enterRoom(HttpServletRequest request, @PathVariable("id") long id) {
        ChessGame chessGame = springChessService.loadRoom(id);

        String roomName = getRoomName(request);

        ModelAndView modelAndView = new ModelAndView();
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        modelAndView.addObject("roomName", roomName);

        return modelAndView;
    }

    private String getRoomName(HttpServletRequest request) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        assert inputFlashMap != null;

        return (String) inputFlashMap.get("roomName");
    }

    @PostMapping(value = "/game/{id}/move", consumes = "text/plain")
    public ModelAndView move(@PathVariable("id") long id, @RequestBody String moveCommand) {
        MoveRequestDto moveRequestDto = new MoveRequestDto(id, moveCommand);
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

    @GetMapping("/load")
    public String load(@RequestParam("roomName") String roomName, RedirectAttributes redirectAttributes) {
        long id = springChessService.getRoomId(roomName);
        redirectAttributes.addFlashAttribute("roomName", roomName);

        return "redirect:/game/" + id;
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
