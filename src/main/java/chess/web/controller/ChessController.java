package chess.web.controller;

import chess.domain.board.dto.BoardDto;
import chess.domain.command.dto.MoveCommandDto;
import chess.domain.game.ChessGame;
import chess.domain.game.dto.ChessGameDto;
import chess.domain.game.dto.GameNameDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Long> createGameView(@RequestBody GameNameDto gameNameDto) {
        return new ResponseEntity<>(
                chessService.addChessGame(gameNameDto.getGameName()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{gameId}")
    public String gameDetailView(@PathVariable String gameId, Model model) {
        ChessGameDto chessGameDTO = chessService.findGameById(gameId);
        model.addAttribute("chessGame", chessGameDTO);
        return "game";
    }

    @GetMapping(value = "/{gameId}/board")
    @ResponseBody
    public ResponseEntity<BoardDto> boardDetailApi(@PathVariable String gameId) {
        ChessGame chessGame = chessService.replayedChessGame(gameId);
        return ResponseEntity.ok(new BoardDto(chessGame));
    }

    @PostMapping(value = "/{gameId}/move")
    @ResponseBody
    public ResponseEntity<BoardDto> movePieceApi(@PathVariable String gameId, @RequestBody MoveCommandDto moveDto) {
        return new ResponseEntity<>(
                chessService.movePiece(gameId, moveDto),
                HttpStatus.CREATED
        );
    }
}
