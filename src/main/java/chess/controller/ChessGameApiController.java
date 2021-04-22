package chess.controller;

import chess.service.SpringChessService;
import chess.service.dto.ChessSaveRequestDto;
import chess.service.dto.GameStatusDto;
import chess.service.dto.GameStatusRequestDto;
import chess.service.dto.GameStatusRequestsDto;
import chess.service.dto.MoveRequestDto;
import chess.service.dto.MoveResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class ChessGameApiController {

    private final SpringChessService chessService;

    public ChessGameApiController(SpringChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<Void> saveChess(@RequestBody final ChessSaveRequestDto requestDto) {
        chessService.saveChess(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> finishChess(@RequestBody final GameStatusRequestDto requestDto) {
        chessService.changeGameStatus(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/view")
    public ResponseEntity<GameStatusRequestsDto> viewRoom() {
        final GameStatusRequestsDto gameStatusRequestsDto = chessService.roomInfos();
        return new ResponseEntity<>(gameStatusRequestsDto, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<GameStatusDto> loadChess(@PathVariable final String name) {
        return ResponseEntity.ok(chessService.loadChess(name));
    }

    @PutMapping("/{name}/pieces")
    public ResponseEntity<MoveResponseDto> movePieces(@PathVariable("name") final String gameName,
        @RequestBody final MoveRequestDto requestDto) {
        return ResponseEntity.ok(chessService.movePiece(gameName, requestDto));
    }
}
