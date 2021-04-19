package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @PostMapping("/create")
    public String createRoom(@RequestParam("roomName") String roomName) {
        int roomNo = springChessService.createRoom(roomName);
        return "redirect:/game/" + roomNo;
    }

    @GetMapping("/game/{roomNo}")
    public ModelAndView enterRoom(@PathVariable("roomNo") int roomNo) {
        ChessGameDto chessGameDto = springChessService.loadRoom(roomNo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("chessGame", chessGameDto);
        modelAndView.setViewName("game");
        return modelAndView;
    }

    @PutMapping(value = "/game/{roomNo}/move")
    @ResponseBody
    public ChessGameDto movePiece(@PathVariable("roomNo") int roomNo, @RequestBody MoveRequestDto moveRequestDto) {
        System.out.println(roomNo);
        System.out.println(moveRequestDto.getCommand());
        ChessGameDto chessGameDto = springChessService.movePiece(roomNo, moveRequestDto);
        return chessGameDto;
    }

    @GetMapping(value = "/rooms")
    public ModelAndView showRooms() {
        List<RoomDto> rooms = springChessService.getAllSavedRooms();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @GetMapping("/load")
    public String loadRoom(@RequestParam("roomNo") int roomNo) {
        return "redirect:/game/" + roomNo;
    }

    @DeleteMapping("/exit/{roomNo}")
    public ResponseEntity deleteRoom(@PathVariable("roomNo") int roomNo) {
        springChessService.deleteRoom(roomNo);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> exceptionHandle(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
