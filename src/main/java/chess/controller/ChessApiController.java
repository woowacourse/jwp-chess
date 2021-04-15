package chess.controller;

import chess.domain.chessgame.ChessGame;
import chess.dto.request.BoardRequestDto;
import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChessApiController {

    private final ChessService chessService;
    private ChessGame chessGame = new ChessGame();

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping(value = "/rooms")
    public RoomsResponseDto getRooms() {
        return chessService.getRooms();
    }

    @PostMapping(value = "/pieces")
    public PiecesResponseDto postPieces(@RequestBody PiecesRequestDto piecesRequestDto) {
        return chessService.postPieces(piecesRequestDto);
    }

    @PutMapping(value = "/board")
    public PiecesResponseDto putBoard(@RequestBody BoardRequestDto boardRequestDto) {
        return chessService.putBoard(boardRequestDto);
    }

    @GetMapping(value = "/score")
    public ScoreResponseDto getScore(@RequestParam("roomId") int roomId, @RequestParam("color") String colorName) {
        return chessService.getScore(roomId, colorName);
    }

}
