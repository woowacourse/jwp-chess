package chess.controller;

import chess.domain.command.Commands;
import chess.domain.dto.MoveResponseDto;
import chess.domain.exception.DataException;
import chess.domain.dto.MoveRequestDto;
import chess.domain.dto.NameDto;
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
        return "lobby";
    }

    @PostMapping("")
    @ResponseBody
    public String saveName(@RequestBody NameDto nameDto) {
        return GSON.toJson(ModelView.idResponse(chessService.addHistory(nameDto.getName())));
    }

    @GetMapping("/{id}")
    public String play(Model model, @PathVariable String id) throws DataException {
        model.addAllAttributes(ModelView.newGameResponse(
                chessService.initialGameInfo(),
                id
        ));
        return "chessGame";
    }

    @GetMapping("/continue/{id}")
    public String continueGame(Model model, @PathVariable String id) throws DataException {
        model.addAllAttributes(ModelView.commonResponseForm(chessService.continuedGameInfo(id), id));
        return "chessGame";
    }

    @GetMapping("/end")
    public String endGame() {
        return "lobby";
    }

    @PostMapping( "/move")
    @ResponseBody
    public String move(@RequestBody MoveRequestDto moveRequestDto) {
        String command = makeMoveCmd(moveRequestDto.getSource(), moveRequestDto.getTarget());
        String id = moveRequestDto.getGameId();
        try {
            chessService.move(id, command, new Commands(command));
            return GSON.toJson(new MoveResponseDto(chessService.continuedGameInfo(id), id));
        } catch (IllegalArgumentException | SQLException e) {
            return e.getMessage();
        }
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
