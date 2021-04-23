package chess.controller;

import chess.domain.command.Commands;
import chess.domain.dto.MoveResponseDto;
import chess.domain.exception.DataException;
import chess.domain.dto.MoveDto;
import chess.service.SpringChessService;
import chess.view.ModelView;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/chess")
public class ChessController {

    private final SpringChessService springChessService;

    @Autowired
    public ChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("")
    public String play(Model model) throws DataException {
        model.addAllAttributes(ModelView.startResponse(springChessService.loadHistory()));
        return "play";
    }

    @GetMapping("/new-game")
    public String playNewGameWithNoSave(Model model) {
        model.addAllAttributes(ModelView.newGameResponse(springChessService.initialGameInfo()));
        return "chessGame";
    }

    @GetMapping("/{name}/new-game")
    public String playNewGameWithSave(Model model, @PathVariable String name) throws DataException {
        model.addAllAttributes(ModelView.newGameResponse(
                springChessService.initialGameInfo(),
                springChessService.addHistory(name)
        ));
        return "chessGame";
    }

    @GetMapping("/continue-game")
    public String continueGame(Model model, @RequestParam("name") String name) throws DataException {
        final String id = springChessService.getIdByName(name);
        model.addAllAttributes(ModelView.commonResponseForm(springChessService.continuedGameInfo(id), id));
        return "chessGame";
    }

    @GetMapping("/end-game")
    public String endGame() {
        return "play";
    }

    @PostMapping("/{historyId}/piece/movement")
    @ResponseBody
    public ResponseEntity<MoveResponseDto> move(@PathVariable String historyId, @RequestBody MoveDto moveDto)
        throws SQLException {
        String command = makeMoveCmd(moveDto.getSource(), moveDto.getTarget());
        springChessService.move(historyId, command, new Commands(command));
        MoveResponseDto moveResponseDto = new MoveResponseDto(springChessService
            .continuedGameInfo(historyId), historyId);
        return ResponseEntity.status(200)
            .body(moveResponseDto);
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
