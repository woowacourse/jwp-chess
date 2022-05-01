package chess.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import chess.dao.SessionToChessRepository;
import chess.domain.board.ChessBoard;
import chess.domain.board.position.Position;
import chess.dto.request.web.SaveGameRequest;
import chess.dto.response.web.GameResponse;
import chess.service.ChessService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chess")
@RequiredArgsConstructor
public class ChessApiController {

    private final ChessService chessService;

    private final SessionToChessRepository sessionToChessRepository;

    @GetMapping(value = "first", produces = APPLICATION_JSON_VALUE)
    public GameResponse init(HttpSession session) {
        ChessBoard chessBoard = chessService.createChessBoard();
        sessionToChessRepository.add(session, chessBoard);

        return new GameResponse(chessBoard);
    }

    @PutMapping(value = "/move", produces = APPLICATION_JSON_VALUE)
    public GameResponse move(HttpSession session,
                             @RequestParam Position from,
                             @RequestParam Position to) {

        ChessBoard chessBoard = sessionToChessRepository.get(session);
        chessService.movePiece(chessBoard, from, to);

        return new GameResponse(chessBoard);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void saveGame(@RequestBody SaveGameRequest saveGameRequest) {
        chessService.saveGame(saveGameRequest);
    }

    @GetMapping(value = "/last", produces = APPLICATION_JSON_VALUE)
    public GameResponse loadLastGame(HttpSession session) {

        ChessBoard chessBoard = chessService.loadLastGame();
        sessionToChessRepository.add(session, chessBoard);

        return new GameResponse(chessBoard);
    }

    @DeleteMapping
    public void deleteGame(HttpSession session) {
        sessionToChessRepository.delete(session);
    }
}
