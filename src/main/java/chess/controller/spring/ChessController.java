package chess.controller.spring;

import chess.controller.spring.vo.SessionVO;
import chess.domain.board.ChessBoard;
import chess.domain.history.History;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.dto.MoveRequestDTO;
import chess.dto.ResultDTO;
import chess.dto.board.BoardDTO;
import chess.service.spring.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/chessgame/{roomId}")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/chessboard")
    public ResponseEntity<BoardDTO> showChessBoard(@PathVariable int roomId) {
        BoardDTO chessBoardDTO = findChessBoard(roomId);
        return ResponseEntity.ok().body(chessBoardDTO);
    }

    private BoardDTO findChessBoard(int roomId) {
        ChessBoard chessBoard = chessService.findChessBoard(roomId);
        TeamType teamType = chessService.findCurrentTeamType(roomId);
        return BoardDTO.of(chessBoard, teamType);
    }

    @PutMapping("/chessboard")
    public ResponseEntity<BoardDTO> move(@PathVariable int roomId, @RequestBody @Valid MoveRequestDTO moveRequestDTO, HttpSession httpSession) {
        SessionVO sessionVO = (SessionVO) httpSession.getAttribute("session");
        History history = new History(moveRequestDTO.getCurrent(), moveRequestDTO.getDestination(), moveRequestDTO.getTeamType());
        chessService.move(history, roomId, sessionVO.getPassword());
        BoardDTO chessBoardDTO = findChessBoard(roomId);
        return ResponseEntity.ok().body(chessBoardDTO);
    }

    @GetMapping("/result")
    public ResponseEntity<ResultDTO> showResult(@PathVariable int roomId) {
        Result result = chessService.calculateResult(roomId);
        ResultDTO resultDTO = ResultDTO.from(result);
        return ResponseEntity.ok().body(resultDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> exit(@PathVariable int roomId, HttpSession httpSession) {
        chessService.deleteGame(roomId);
        httpSession.invalidate();
        String location = "/";
        return ResponseEntity.ok().body(location);
    }
}
