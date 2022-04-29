package chess.controller.page;

import chess.entity.Square;
import chess.service.ChessServiceV2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

    private ChessServiceV2 chessServiceV2;

    public WebController(ChessServiceV2 chessServiceV2) {
        this.chessServiceV2 = chessServiceV2;
    }

    @GetMapping("/")
    public ModelAndView renderRoom() {
        return new ModelAndView("room", new HashMap<>());
    }

    @GetMapping("/room/{roomId}")
    public ModelAndView findOneRoom(@PathVariable Long roomId) {
        final List<Square> squares = chessServiceV2.findSquareAllById(roomId);
        final BoardRes boardRes = BoardRes.createBoardResToListSquare(squares);

        final Map<String, Object> model = new HashMap<>();
        model.put("board", boardRes);
        model.put("roomId", roomId);
        return new ModelAndView("room", model);
    }

//    @PostMapping("/game")
//    @ResponseBody
//    public ResponseEntity insertGame() {
//        final Long gameId = chessService.insertGame();
//        return ResponseEntity.ok().body(new GameDto(gameId));
//    }

//    @PutMapping("/game/board")
//    @ResponseBody
//    public ResponseEntity updateBoard(@RequestBody MoveDto moveDto) {
//        final Long gameId = chessService.updateBoard(moveDto.getFrom(), moveDto.getTo());
//        return ResponseEntity.ok().body(new GameDto(gameId));
//    }

//    @GetMapping("/game/status")
//    @ResponseBody
//    public ResponseEntity<StatusDto> selectStatus() {
//        StatusDto statusDto = chessService.selectStatus();
//        return ResponseEntity.ok().body(statusDto);
//    }

//    @DeleteMapping("/game")
//    @ResponseBody
//    public ResponseEntity deleteGame() {
//        final ChessService chessService = this.chessService;
//        final Long gameId = chessService.deleteGame();
//        return ResponseEntity.ok().body(new GameDto(gameId));
//    }
}
