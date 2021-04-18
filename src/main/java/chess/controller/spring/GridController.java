package chess.controller.spring;

import chess.dto.requestdto.StartRequestDto;
import chess.dto.response.Response;
import chess.dto.response.ResponseCode;
import chess.dto.responsedto.GridAndPiecesResponseDto;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class GridController {
    @Autowired
    private ChessService chessService;

    @GetMapping("/grid/{roomName}")
    public Response getRoom(@PathVariable("roomName") String roomName) {
        StartRequestDto startRequestDto = new StartRequestDto(roomName);
        GridAndPiecesResponseDto gridAndPiecesResponseDto = chessService.getGridAndPieces(startRequestDto);
        return new Response(ResponseCode.OK, gridAndPiecesResponseDto);
    }

    @PostMapping("/grid/{gridId}/start")
    public Response start(@PathVariable("gridId") String gridId) throws SQLException {
        chessService.start(Long.parseLong(gridId));
        return new Response(ResponseCode.NO_CONTENT);
    }

    @PostMapping("/grid/{gridId}/finish")
    public Response finish(@PathVariable("gridId") String gridId) {
        chessService.finish(Long.parseLong(gridId));
        return new Response(ResponseCode.NO_CONTENT);
    }
}
