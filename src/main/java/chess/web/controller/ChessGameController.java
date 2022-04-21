package chess.web.controller;

import chess.domain.game.ChessGame;
import chess.domain.game.state.Player;
import chess.domain.game.state.RunningGame;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessGameService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChessGameController {

    private final ChessGameService service;

    public ChessGameController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ModelAndView root() {
        ModelAndView modelAndView = new ModelAndView("start");
        return modelAndView;
    }

    @GetMapping("/start")
    public String start() {
        service.start();
        return "redirect:/play";
    }

    @GetMapping("/play")
    public ModelAndView play() {
        Map<Position, Piece> board = service.findAllBoard();
        if (board.isEmpty()) {
            start();
        }

        ModelAndView modelAndView = new ModelAndView("index");
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            modelAndView.addObject(position.toString(), piece);
        }

        Player player = service.findAllPlayer();

        modelAndView.addObject("turn",  player.name());

        return modelAndView;
    }

    @GetMapping("/end")
    public ModelAndView end() {
        ModelAndView modelAndView = new ModelAndView("end");
        return modelAndView;
    }

    @PostMapping("/move")
    @ResponseBody
    public MoveResultDto move(@RequestBody MoveDto moveDto, RedirectAttributes attributes) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        ChessGame chessGame = ChessGame.of(new RunningGame(service.createChessBoard(), service.findAllPlayer()));
        String turn = chessGame.getTurn();
        MoveResultDto moveResultDto = new MoveResultDto();

        try {
            service.move(chessGame, source, target);
        } catch (Exception e) {
            moveResultDto.setCanMove(false);
        }

        if (isFinished(chessGame)) {
            moveResultDto.setGameOver(true);
            moveResultDto.setWinner(turn);
        }

        return moveResultDto;
    }

    private boolean isFinished(ChessGame chessGame) {
        return chessGame.isFinished();
    }
}
