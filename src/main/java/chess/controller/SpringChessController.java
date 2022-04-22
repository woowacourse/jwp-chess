package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.MoveCommandDto;
import chess.dto.PiecesDto;
import chess.service.ChessGameService;
import chess.web.view.BoardView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringChessController {

    ChessGameService chessGameService = new ChessGameService();

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
//    @PostMapping("/game/end")
//    private void getEnd(@RequestBody ChessGameDto chessGameDto) {
//            chessGameService.forceEnd(chessGameDto.getGameId());
//
//            return true;
//        });
//    }

    @PostMapping("/game/move")
    private String postMove(@RequestBody MoveCommandDto moveDto) {
        chessGameService.move(moveDto.getGameId(), moveDto);
        return "redirect:/game/progress";
    }

    @GetMapping("/game/status")
    private ChessGameDto getStatus(@RequestParam String gameId) {
        return new ChessGameDto(chessGameService.calculateGameResult(gameId), gameId);
    }

    @GetMapping("/game/progress")
    private ChessGameDto getProgressPage(@RequestParam String gameId) {
        ModelAndView modelAndView = getModel();
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        return new ChessGameDto(chessGameService.getCurrentGame(gameId), gameId);
    }

    @GetMapping("/game/start")
    public ModelAndView getGamePage(@RequestParam String gameId) {
        chessGameService.createOrGet(gameId);
        PiecesDto piecesDto = chessGameService.getCurrentGame(gameId);
        ModelAndView modelAndView = getModel();
        modelAndView.addObject("pieces", BoardView.of(piecesDto).getBoardView());
        modelAndView.addObject("gameId", gameId);
        return modelAndView;
    }

    private ModelAndView getModel() {
        return new ModelAndView("game");
    }
}
