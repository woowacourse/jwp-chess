package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

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
        Optional<ChessGameDto> optionalChessGameDto = springChessService.loadRoom(roomNo);
        if (optionalChessGameDto.isPresent()) {
            modelAndView.setViewName("game");
            modelAndView.addObject("chessGame", optionalChessGameDto.get());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @PostMapping(value = "/game/move")
    public ModelAndView move(@RequestBody MoveRequestDto moveRequestDto, ModelAndView modelAndView) {
        Optional<ChessGameDto> optionalChessGameDto = springChessService.movePiece(moveRequestDto);
        modelAndView.setViewName("game");
        if (optionalChessGameDto.isPresent()) {
            checkGameEnd(moveRequestDto.getRoomNo(), modelAndView);
            modelAndView.addObject("chessGame", optionalChessGameDto.get());
            return modelAndView;
        }
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    private void checkGameEnd(int roomNo, ModelAndView modelAndView) {
        if (springChessService.isGameEnd(roomNo)) {
            addResult(roomNo, modelAndView);
            springChessService.deleteGame(roomNo);
        }
    }

    private void addResult(int roomNo, ModelAndView modelAndView) {
        if (springChessService.getResult(roomNo).isPresent()) {
            modelAndView.addObject("result", springChessService.getResult(roomNo).get());
        }
    }

    @GetMapping(value = "/rooms")
    public ModelAndView rooms(ModelAndView modelAndView) {
        List<RoomDto> rooms = springChessService.getAllSavedRooms();
        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @GetMapping("/game/load")
    public ModelAndView load(@RequestParam("roomNo") int roomNo, ModelAndView modelAndView) {
        Optional<ChessGameDto> optionalChessGameDto = springChessService.loadRoom(roomNo);
        if (optionalChessGameDto.isPresent()) {
            modelAndView.setViewName("game");
            modelAndView.addObject("chessGame", optionalChessGameDto.get());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/rooms");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }
}
