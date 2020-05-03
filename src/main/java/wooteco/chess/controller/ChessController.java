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

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChessController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
        chessService.start();
    }

    @GetMapping("/")
    public String startGame(Model model) {
        model.addAllAttributes(chessService.makeStartResponse());
        return "chessGameStart";
    }

    @PostMapping("/")
    public String playing(@RequestParam("newRoomName") String newRoomName) {
        Long newRoomId = chessService.playNewGame(newRoomName);
        return "redirect:/playing/" + newRoomId;
    }

    @GetMapping("/playing/{roomId}")
    public String lastGame(@PathVariable("roomId") Long roomId, Model model) {
        model.addAllAttributes(chessService.playLastGame(roomId));
        return "chessGame";
    }

    @PostMapping("/playing/{roomId}")
    @ResponseBody
    public String move(@PathVariable("roomId") Long roomId, @RequestBody MoveDto moveDto) {
        chessService.move(moveDto.getSource(), moveDto.getTarget(), roomId);
        Map<String, Object> model = new HashMap<>(chessService.makeMoveResponse(roomId));
        chessService.checkIfPlaying(roomId);

        return GSON.toJson(model);
    }

    @GetMapping("/end/{roomId}")
    public String endGame(@PathVariable("roomId") Long roomId) {
        chessService.end(roomId);
        return "chessGameEnd";
    }
}
