package chess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chess.domain.chessgame.ChessGame;
import chess.domain.position.Position;
import chess.dto.request.MoveRequestDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.ChessService;

@RestController
@RequestMapping("/api")
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping(value = "/rooms")
    public RoomsResponseDto getRooms() {
        return new RoomsResponseDto(chessService.getRooms());
    }

    @PostMapping(value = "/room")
    public int postRoom(@RequestParam String title) {
        return chessService.postRooms(title);
    }

    @PostMapping(value = "/pieces/{roomId}")
    public PiecesResponseDto postPieces(@PathVariable int roomId) {
        return new PiecesResponseDto(chessService.postPieces(roomId));
    }

    @PutMapping(value = "/board/{roomId}")
    public PiecesResponseDto putBoard(@PathVariable int roomId, @RequestBody MoveRequestDto moveRequestDto) {
        Position source = new Position(moveRequestDto.getSource());
        Position target = new Position(moveRequestDto.getTarget());

        ChessGame chessGame = chessService.putBoard(roomId, source, target);
        return new PiecesResponseDto(chessGame.matchColor(), chessGame.isPlaying(), chessGame.pieces());
    }

    @GetMapping(value = "/score/{roomId}/{colorName}")
    public ScoreResponseDto getScore(@PathVariable int roomId, @PathVariable String colorName) {
        return new ScoreResponseDto(chessService.getScore(roomId, colorName));
    }
}
