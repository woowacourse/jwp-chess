package chess.controller.spring;

import chess.dto.response.Response;
import chess.dto.response.ResponseCode;
import chess.dto.responsedto.GridAndPiecesResponseDto;
import chess.dto.responsedto.RoomsResponseDto;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {
    @Autowired
    private ChessService chessService;

    @PostMapping("/room/{roomId}/restart")
    public Response<GridAndPiecesResponseDto> restart(@PathVariable("roomId") String roomId) {
        return new Response(ResponseCode.OK, chessService.restart(Long.parseLong(roomId)));
    }

    @GetMapping("/room")
    public Response<RoomsResponseDto> getRooms() {
        return new Response(ResponseCode.OK, chessService.getAllRooms());
    }
}
