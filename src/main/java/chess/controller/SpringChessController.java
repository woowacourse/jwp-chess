package chess.controller;

import chess.dto.GameResultDto;
import chess.dto.MoveCommandDto;
import chess.dto.PiecesDto;
import chess.service.ChessGameService;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessController {

    AtomicReference<ChessGameService> chessGameService = new AtomicReference<>();

    //    private void exception() {
//        exception(Exception.class, (exception, request, response) -> {
//            response.status(400);
//            response.body(exception.getMessage());
//        });
//    }
//
//    private void postExit() {
//        post("/game/exit", (req, res) -> {
//            chessGameService.get()
//                    .cleanGame();
//
//            res.redirect("/");
//            return true;
//        });
//    }
//
//    private void getEnd() {
//        post("/game/end", (req, res) -> {
//            chessGameService.get()
//                    .forceEnd();
//
//            res.redirect("/game/progress");
//            return true;
//        });
//    }
//
    @PostMapping("/game/save")
    private PiecesDto postSave() {
        chessGameService.get().save();
        return chessGameService.get().getCurrentGame();
    }

    @PostMapping("/game/move")
    private PiecesDto postMove(@RequestBody MoveCommandDto moveDto) {
        System.out.println(moveDto);
        chessGameService.get().move(moveDto);
        return chessGameService.get().getCurrentGame();
    }

    @GetMapping("/game/status")
    private GameResultDto getStatus() {
        return chessGameService.get().calculateGameResult();
    }

    @GetMapping("/game/progress")
    private PiecesDto getProgressPage() {
        return chessGameService.get().getCurrentGame();
    }

    @GetMapping("/game/start")
    private PiecesDto getGamePage(@RequestParam String gameId) {
        chessGameService.set(new ChessGameService(gameId));
        return chessGameService.get().createOrGet();
    }

    @GetMapping("/")
    private PiecesDto getInitPage() {
        return new PiecesDto(List.of());
    }
}
