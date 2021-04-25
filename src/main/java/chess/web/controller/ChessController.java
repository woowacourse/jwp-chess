package chess.web.controller;

import chess.domain.board.dto.BoardDto;
import chess.domain.command.dto.MoveCommandDto;
import chess.domain.game.ChessGame;
import chess.domain.game.dto.ChessGameDto;
import chess.domain.game.dto.GameNameDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/chess")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Object createGameView(@RequestBody GameNameDto gameNameDto) {
        return chessService.addChessGame(gameNameDto.getGameName());
    }

    @GetMapping("/{gameId}")
    public String gameDetailView(@PathVariable String gameId, Model model) {
        ChessGameDto chessGameDTO = chessService.findGameById(gameId);
        model.addAttribute("chessGame", chessGameDTO);
        return "game";
    }

    @GetMapping(value = "/{gameId}/board")
    @ResponseBody
    public BoardDto boardDetailApi(@PathVariable String gameId) {
        ChessGame chessGame = chessService.replayedChessGame(gameId);
        return new BoardDto(chessGame);
    }

    @PostMapping(value = "/{gameId}/move")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDto movePieceApi(@PathVariable String gameId, @RequestBody MoveCommandDto moveDto) {
        return chessService.movePiece(gameId, moveDto);
    }
}
