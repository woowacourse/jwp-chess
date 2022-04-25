package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.dao.ChessGameDao;
import web.dao.PieceDao;
import web.dto.ChessGameDto;
import web.dto.GameStatus;
import web.exception.ChessGameException;
import web.service.ChessGameService;

@Controller
public class ChessGameController {

    private final ChessGameService service;
    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessGameController(ChessGameService service, ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.service = service;
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    @GetMapping("/chess-game")
    public String chessGame(@RequestParam("chess-game-id") int chessGameId, Model model) {
        ChessGameDto chessGameDto = chessGameDao.findById(chessGameId);

        if (!isGameRunning(chessGameDto)) {
            chessGameDto = service.prepareNewChessGame(chessGameDto);
        }

        model.addAttribute("pieces", pieceDao.findPieces(chessGameDto.getId()));
        model.addAttribute("chessGame", chessGameDto);
        return "chess-game";
    }

    private boolean isGameRunning(ChessGameDto chessGameDto) {
        return chessGameDto.getStatus() == GameStatus.RUNNING;
    }

    @PostMapping("/chess-game/move")
    public String move(@RequestParam("chess-game-id") int chessGameId,
                       @RequestParam String from,
                       @RequestParam String to,
                       RedirectAttributes attributes) {
        ChessGameDto chessGameDto = service.move(chessGameId, new Movement(from, to));
        if (isGameFinished(chessGameDto)) {
            attributes.addFlashAttribute("isFinished", true);
            attributes.addFlashAttribute("winner", chessGameDto.getWinner());
        }
        return "redirect:/chess-game?chess-game-id=" + chessGameId;
    }

    private boolean isGameFinished(ChessGameDto chessGameDto) {
        return chessGameDto.getStatus() == GameStatus.FINISHED;
    }

    @ExceptionHandler(ChessGameException.class)
    public String exceptionHandler(ChessGameException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/chess-game?chess-game-id=" + e.getChessGameId();
    }
}
