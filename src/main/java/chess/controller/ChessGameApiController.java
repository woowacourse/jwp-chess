package chess.controller;

import chess.domain.game.dto.BoardDTO;
import chess.domain.game.dto.MoveDTO;
import chess.domain.gameRoom.ChessGame;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
public class ChessGameApiController implements WebMvcConfigurer {

    private final ChessService chessService;

    public ChessGameApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("game.hbs")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @GetMapping("/chess/game/{id}/board")
    public ResponseEntity<BoardDTO> showBoard(@PathVariable String id) {
        ChessGame chessGame = chessService.getChessGamePlayed(id);
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }

    @PostMapping("/chess/game/{id}/move")
    public ResponseEntity<BoardDTO> movePiece(@PathVariable String id, MoveDTO moveDTO) {
        System.out.println("출력 : " + moveDTO);
        ChessGame chessGame = chessService.movePiece(id, moveDTO);
        return ResponseEntity.ok(new BoardDTO(chessGame));
    }
}
