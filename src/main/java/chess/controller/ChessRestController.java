package chess.controller;

import chess.domain.command.Commands;
import chess.domain.dto.MoveRequestDto;
import chess.domain.dto.NameDto;
import chess.domain.response.GameResponse;
import chess.service.ChessService;
import chess.view.ModelView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/play")
public class ChessRestController {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ChessService chessService;

    public ChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    @ResponseBody
    public String saveName(@RequestBody NameDto nameDto) {
        return GSON.toJson(ModelView.idResponse(chessService.addHistory(nameDto.getName())));
    }

    @PostMapping("/move")
    @ResponseBody
    public String move(@RequestBody MoveRequestDto moveRequestDto) {
        String command = makeMoveCmd(moveRequestDto.getSource(), moveRequestDto.getTarget());
        String id = moveRequestDto.getGameId();
        try {
            chessService.move(id, command, new Commands(command));
            return GSON.toJson(new GameResponse(chessService.continuedGameInfo(id), id));
        } catch (IllegalArgumentException | SQLException e) {
            return e.getMessage();
        }
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
