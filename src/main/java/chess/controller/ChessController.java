package chess.controller;

import chess.domain.command.Commands;
import chess.domain.dto.MoveResponseDto;
import chess.domain.exception.DataException;
import chess.domain.dto.MoveDto;
import chess.service.ChessService;
import chess.view.ModelView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/play")
public class ChessController {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("")
    public String play(Model model) throws DataException {
        model.addAllAttributes(ModelView.startResponse(chessService.loadHistory()));
        return "play";
    }

    @GetMapping("/new")
    public String playNewGameWithNoSave(Model model) {
        model.addAllAttributes(ModelView.newGameResponse(chessService.initialGameInfo()));
        return "chessGame";
    }

    @GetMapping("/{name}/new")
    public String playNewGameWithSave(Model model, @PathVariable String name) throws DataException {
        model.addAllAttributes(ModelView.newGameResponse(
                chessService.initialGameInfo(),
                chessService.addHistory(name)
        ));
        return "chessGame";
    }

    @GetMapping("/continue")
    public String continueGame(Model model, @RequestParam("name") String name) throws DataException {
        final String id = chessService.getIdByName(name);
        model.addAllAttributes(ModelView.commonResponseForm(chessService.continuedGameInfo(id), id));
        return "chessGame";
    }

    @GetMapping("/end")
    public String endGame() {
        return "play";
    }

    @PostMapping("/move")
    @ResponseBody
    public MoveResponseDto move(@RequestBody MoveDto moveDto) {
        String command = makeMoveCmd(moveDto.getSource(), moveDto.getTarget());
        String historyId = moveDto.getGameId();

        try {
            chessService.move(historyId, command, new Commands(command));
            return ModelView.moveResponse(chessService.continuedGameInfo(historyId), historyId);
        } catch (IllegalArgumentException | SQLException e) {
            return new MoveResponseDto(e.getMessage());
        }
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
