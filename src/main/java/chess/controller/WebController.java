package chess.controller;

import chess.model.domain.piece.Team;
import chess.model.dto.MoveDto;
import chess.service.ChessGameService;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    private static final Gson GSON = new Gson();

    @Autowired
    private ChessGameService chessGameService;

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @PostMapping("/start")
    public String start(@RequestParam String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "start";
    }

    @PostMapping("/game")
    public String game(@RequestParam String roomId,
        @RequestParam(defaultValue = "WHITE") String WhiteName,
        @RequestParam(defaultValue = "BLACK") String BlackName,
        Model model) {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, BlackName);
        userNames.put(Team.WHITE, WhiteName);

        chessGameService.saveRoom(userNames, Integer.parseInt(roomId));
        chessGameService.saveUser(userNames);

        model.addAttribute("gameId", roomId);
        return "game";
    }

    @PostMapping("/game/api/board")
    @ResponseBody
    public String board(@RequestBody String req) {
        MoveDto moveDTO = GSON.fromJson(req, MoveDto.class);
        System.out.println(moveDTO);
        return GSON.toJson(chessGameService.loadChessGame(moveDTO.getGameId()));
    }


}
