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
import wooteco.chess.dto.request.MoveRequest;
import wooteco.chess.dto.response.MoveResponse;
import wooteco.chess.dto.response.StartResponse;
import wooteco.chess.service.ChessService;

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
        StartResponse startResponse = chessService.getStartResponse();
        model.addAttribute(startResponse);
        return "chessGameStart";
    }

    @PostMapping("/")
    public String playNewGame(@RequestParam("newRoomName") String newRoomName) {
        Long newRoomId = chessService.playNewGame(newRoomName);
        return "redirect:/playing/" + newRoomId;
    }

    @GetMapping("/playing/{roomId}")
    public String playLastGame(@PathVariable("roomId") Long roomId, Model model) {
        chessService.playLastGame(roomId);
        MoveResponse moveResponse = chessService.getMoveResponse(roomId);
        model.addAttribute(moveResponse);
        return "chessGame";
    }

    @PostMapping("/playing/{roomId}")
    @ResponseBody
    public String move(@PathVariable("roomId") Long roomId, @RequestBody MoveRequest moveRequest) {
        chessService.move(moveRequest.getSource(), moveRequest.getTarget(), roomId);
        MoveResponse moveResponse = chessService.getMoveResponse(roomId);
        chessService.checkIfPlaying(roomId);

        return GSON.toJson(moveResponse);
    }

    @GetMapping("/end/{roomId}")
    public String endGame(@PathVariable("roomId") Long roomId) {
        chessService.end(roomId);
        return "chessGameEnd";
    }
}
