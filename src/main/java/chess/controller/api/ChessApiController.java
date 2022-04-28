package chess.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import chess.domain.board.ChessBoard;
import chess.domain.board.position.Position;
import chess.domain.piece.PieceTeam;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.GameResponse;
import chess.exception.ClientException;
import chess.service.ChessService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chess")
@RequiredArgsConstructor
public class ChessApiController {

    private final ChessService chessService;

    @GetMapping(value = "/init", produces = APPLICATION_JSON_VALUE)
    public GameResponse init(HttpSession session) {

        ChessBoard chessBoard = chessService.initAndGetChessBoard(session);

        double white = chessBoard.calculateScoreByTeam(PieceTeam.WHITE);
        double black = chessBoard.calculateScoreByTeam(PieceTeam.BLACK);

        log.info("init {}, {}", white, black);

        return new GameResponse(chessBoard);
    }

    @PatchMapping(value = "/move", produces = APPLICATION_JSON_VALUE)
    public GameResponse move(HttpSession session,
                             @RequestParam("from") String fromString,
                             @RequestParam("to") String toString) {

        Position from = Position.of(fromString);
        Position to = Position.of(toString);

        chessService.movePiece(session, from, to);

        ChessBoard chessBoard = chessService.getChessBoard(session);

        double white = chessBoard.calculateScoreByTeam(PieceTeam.WHITE);
        double black = chessBoard.calculateScoreByTeam(PieceTeam.BLACK);

        log.info("move {}, {}", white, black);

        return new GameResponse(chessService.getChessBoard(session));
    }

    @PutMapping(value = "/save-game", consumes = APPLICATION_JSON_VALUE)
    public void saveGame(@RequestBody SaveRequest saveRequest) {
        chessService.saveGame(saveRequest);
    }

    @GetMapping(value = "/load-last-game", produces = APPLICATION_JSON_VALUE)
    public GameResponse loadLastGame(HttpSession session) {
        GameResponse gameResponse = chessService.loadLastGame();
        return gameResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String illegalExHandle(ClientException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String exHandle(Exception exception) throws Exception {
        exception.printStackTrace();
        return exception.getMessage();
    }
}
