package chess.controller;

import chess.dao.GameDaoImpl;
import chess.dao.PieceDaoImpl;
import chess.db.DBConnector;
import chess.domain.command.MoveCommand;
import chess.dto.ChessResponseDto;
import chess.dto.ErrorResponseDto;
import chess.dto.MoveCommandDto;
import chess.dto.ScoresDto;
import chess.serviece.ChessService;
import chess.util.SqlConnectionException;
import chess.util.SqlQueryException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess-game")
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController() {
        final DBConnector dbConnector = new DBConnector();
        this.chessService = new ChessService(new PieceDaoImpl(dbConnector), new GameDaoImpl(dbConnector));
    }

    @GetMapping()
    public String init() {
        return "index";
    }

    @GetMapping("/load")
    public ResponseEntity<ChessResponseDto> load() {
        return ResponseEntity.ok().body(chessService.getChess());
    }

    @GetMapping("/start")
    public ResponseEntity<ChessResponseDto> start() {
        return ResponseEntity.ok().body(chessService.initializeGame());
    }

    @GetMapping("/score")
    public ResponseEntity<ScoresDto> score() {
        return ResponseEntity.ok().body(chessService.getScore());
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessResponseDto> move(@RequestBody MoveCommandDto moveCommandDto) {
        MoveCommand moveCommand = moveCommandDto.toEntity();
        return ResponseEntity.ok().body(chessService.movePiece(moveCommand));
    }

    @PostMapping("/end")
    public ResponseEntity<ScoresDto> end() {
        return ResponseEntity.ok().body(chessService.finishGame());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(Exception exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage());
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(SqlQueryException.class)
    public ResponseEntity<ErrorResponseDto> handleSqlQueryException() {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto("데이터를 처리하는 도중에 에러가 발생하였습니다. 다시 시도해 주시기 바랍니다.");
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(SqlConnectionException.class)
    public ResponseEntity<ErrorResponseDto> handleSqlConnectionException() {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto("서버에 연결되지 않았습니다.");
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
