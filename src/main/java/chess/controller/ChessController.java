package chess.controller;

import chess.domain.command.Commands;
import chess.domain.dto.HistoryDto;
import chess.domain.dto.MoveResponseDto;
import chess.domain.dto.MoveDto;
import chess.service.SpringChessService;
import chess.view.ModelView;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/chess")
public class ChessController {

    private final SpringChessService springChessService;

    public ChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping()
    public String play(Model model) {
        model.addAllAttributes(ModelView.startResponse(springChessService.loadHistory()));
        return "play";
    }

    @GetMapping("/game/temporary")
    public String playNewGameWithNoSave(Model model) {
        model.addAllAttributes(ModelView.newGameResponse(springChessService.initialGameInfo()));
        return "chessGame";
    }

    @PostMapping("/game/{name}")
    public ResponseEntity<HistoryDto> playNewGameWithSave(Model model, @PathVariable String name) {
        springChessService.initialGameInfo();
        springChessService.addHistory(name);
        return ResponseEntity.ok()
            .body(new HistoryDto(name));
    }

    @GetMapping("/game/{name}")
    public String continueGame(Model model, @PathVariable String name) {
        final String id = springChessService.getIdByName(name);
        model.addAllAttributes(ModelView.commonResponseForm(
            springChessService.continuedGameInfo(id),
            id));
        return "chessGame";
    }

    @GetMapping("/game/termination")
    public String endGame() {
        return "play";
    }

    @PostMapping("/game/{historyId}/pieces")
    @ResponseBody
    public ResponseEntity<MoveResponseDto> move(@PathVariable String historyId,
        @RequestBody MoveDto moveDto) {
        String command = makeMoveCmd(moveDto.getSource(), moveDto.getTarget());
        springChessService.move(historyId, command, new Commands(command));
        MoveResponseDto moveResponseDto = new MoveResponseDto(springChessService
            .continuedGameInfo(historyId), historyId);
        return ResponseEntity.ok()
            .body(moveResponseDto);
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
