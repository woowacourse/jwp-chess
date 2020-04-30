package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String startGame(Model model) {
        chessService.start(); //TODO : 여러 게임 진행할 때를 대비해 옮겨야 함
        model.addAllAttributes(chessService.makeStartResponse());
        return "chessGameStart";
    }

    @PostMapping("/playing")
    public String playing(@RequestParam("newRoomName") String newRoomName, Model model) {
        chessService.playNewGame(newRoomName);
        model.addAllAttributes(chessService.makeMoveResponse());
        return "chessGame";
    }

    @GetMapping("/playing/lastGame/{roomId}")
    public String lastGame(@PathVariable("roomId") Long roomId, Model model) {
        chessService.playLastGame(roomId);
        model.addAllAttributes(chessService.makeMoveResponse());
        return "chessGame";
    }

//    @PostMapping("/playing/newGame")
//    public String newGame(@RequestParam("newRoomName") String roomName, Model model) {
//        chessService.playNewGame(roomName);
//        model.addAllAttributes(chessService.makeMoveResponse());
//        return "chessGame";
//    }

    @GetMapping("/end")
    public String endGame() {
        chessService.end();
        return "chessGameEnd";
    }

    @PostMapping("/playing/move")
    @ResponseBody
    public String move(@RequestBody MoveDto moveDto) {
        chessService.move(moveDto.getSource(), moveDto.getTarget(), 1L);
        return GSON.toJson(chessService.makeMoveResponse());
    }
}
