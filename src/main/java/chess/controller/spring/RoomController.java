package chess.controller.spring;

import chess.dto.response.Response;
import chess.dto.responsedto.GridAndPiecesResponseDto;
import chess.dto.responsedto.RoomsResponseDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RoomController {
    private final ChessService chessService;

    public RoomController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/room/{roomId}/restart")
    public Response<GridAndPiecesResponseDto> restart(@PathVariable("roomId") String roomId) {
        return new Response<>(HttpStatus.OK, chessService.restart(Long.parseLong(roomId)));
    }

    @GetMapping("/room")
    public Response<RoomsResponseDto> getRooms(@PathVariable(value = "page", required = false) Optional<Integer> page) {
        return page.map(p -> new Response<>(HttpStatus.OK, chessService.getAllRooms(p))).orElseGet(
                () -> new Response<>(HttpStatus.OK, chessService.getAllRooms()));
    }
}
