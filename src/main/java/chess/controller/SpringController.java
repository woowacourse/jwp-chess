package chess.controller;

import chess.dto.requestdto.MoveRequestDto;
import chess.dto.requestdto.StartRequestDto;
import chess.dto.response.ResponseCode;
import chess.dto.response.ResponseDto;
import chess.dto.responsedto.GridAndPiecesResponseDto;
import chess.exception.ChessException;
import chess.service.ChessService;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SpringController {

    @Autowired
    private ChessService chessService;


    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping("/move")
    public ResponseEntity move(@RequestBody MoveRequestDto moveRequestDto) throws SQLException {
        return new ResponseEntity(chessService.move(moveRequestDto), HttpStatus.OK);
    }

    @GetMapping("/grid/{roomName}")
    public ResponseEntity getRoom(@PathVariable("roomName") String roomName) {
        StartRequestDto startRequestDto = new StartRequestDto(roomName);
        GridAndPiecesResponseDto gridAndPiecesResponseDto = chessService
            .getGridAndPieces(startRequestDto);
        return new ResponseEntity(new ResponseDto(ResponseCode.OK, gridAndPiecesResponseDto),
            HttpStatus.OK);
    }

    @PostMapping("/grid/{gridId}/start")
    public ResponseEntity start(@PathVariable("gridId") String gridId) throws SQLException {
        chessService.start(Long.parseLong(gridId));
        return new ResponseEntity(new ResponseDto(ResponseCode.NO_CONTENT), HttpStatus.OK);
    }

    @PostMapping("/grid/{gridId}/finish")
    public ResponseEntity finish(@PathVariable("gridId") String gridId) {
        chessService.finish(Long.parseLong(gridId));
        return new ResponseEntity(new ResponseDto(ResponseCode.NO_CONTENT), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}/restart")
    public ResponseEntity restart(@PathVariable("roomId") String roomId) {
        return new ResponseEntity(
            new ResponseDto(ResponseCode.OK, chessService.restart(Long.parseLong(roomId))),
            HttpStatus.OK);
    }

    @GetMapping("/room")
    public ResponseEntity getRooms() {
        return new ResponseEntity(new ResponseDto(ResponseCode.OK, chessService.getAllRooms()),
            HttpStatus.OK);
    }

    @ExceptionHandler({ChessException.class})
    public ResponseEntity<Object> handleAll(ChessException e) {
        return new ResponseEntity<>(new ResponseDto(e.getCode(), e.getMessage()),
            HttpStatus.BAD_REQUEST);
    }
}
