package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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

    @GetMapping("/create")
    public String createRoom(@RequestParam("roomName") String roomName) {
        Optional<Integer> roomNo = springChessService.createRoom(roomName);
        if (roomNo.isPresent()) {
            return "redirect:/game/" + roomNo.get();
        }
        return "redirect:/";
    }

    @GetMapping("/game/{roomNo}")
    public ModelAndView startGame(@PathVariable int roomNo, ModelAndView modelAndView) {
        Optional<ChessGameDTO> optionalChessGameDTO = springChessService.loadRoom(roomNo);
        if (optionalChessGameDTO.isPresent()) {
            modelAndView.setViewName("game");
            modelAndView.addObject("chessGame", optionalChessGameDTO.get());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @PostMapping(value = "/game/move")
    public ModelAndView move(@RequestBody String param, ModelAndView modelAndView) {
        Optional<ChessGameDTO> optionalChessGameDTO = springChessService.movePiece(param);
        modelAndView.setViewName("game");
        if (optionalChessGameDTO.isPresent()) {
            checkGameEnd(param, modelAndView);
            modelAndView.addObject("chessGame", optionalChessGameDTO.get());
            return modelAndView;
        }
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    private void checkGameEnd(String param, ModelAndView modelAndView) {
        if (springChessService.isGameEnd(param)) {
            addResult(param, modelAndView);
            springChessService.deleteGame(param);
        }
    }

    private void addResult(String param, ModelAndView modelAndView) {
        if (springChessService.getResult(param).isPresent()) {
            modelAndView.addObject("result", springChessService.getResult(param).get());
        }
    }

    @GetMapping(value = "/rooms")
    public ModelAndView rooms(ModelAndView modelAndView) {
        List<RoomDTO> rooms = springChessService.getAllSavedRooms();
        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @GetMapping("/game/load")
    public ModelAndView load(@RequestParam("roomNo") int roomNo, ModelAndView modelAndView) {
        Optional<ChessGameDTO> optionalChessGameDTO = springChessService.loadRoom(roomNo);
        if (optionalChessGameDTO.isPresent()) {
            modelAndView.setViewName("game");
            modelAndView.addObject("chessGame", optionalChessGameDTO.get());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/rooms");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }
}
