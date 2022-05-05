package chess.controller;

import chess.database.dto.RoomDto;
import chess.database.dto.RouteDto;
import chess.database.dto.StatusDto;
import chess.domain.game.GameState;
import chess.dto.GameStateResponse;
import chess.dto.PathResponse;
import chess.service.ChessRoomService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebChessController {

    @Autowired
    private ChessRoomService chessRoomService;

    @GetMapping("/")
    public String showRooms(Model model) {
        Map<RoomDto, String> roomStates = chessRoomService.findAllRoomState();
        model.addAttribute("roomStates", roomStates);
        return "rooms";
    }

    @PostMapping("/rooms")
    @ResponseBody
    public ResponseEntity<PathResponse> createRoom(@RequestBody RoomDto requestRoomDto) {
        RoomDto roomDto = chessRoomService.createNewRoom(requestRoomDto.getName(), requestRoomDto.getPassword());
        return respondPath("/rooms/" + roomDto.getId());
    }

    @GetMapping(path = "/rooms/{roomId}")
    public String showGame(Model model, @PathVariable("roomId") int roomId) {
        RoomDto roomDto = chessRoomService.findById(roomId);
        GameState state = chessRoomService.readGameState(roomId);
        GameStateResponse response = GameStateResponse.of(state);

        model.addAttribute("roomId", roomId);
        model.addAttribute("roomName", roomDto.getName());
        model.addAttribute("response", response);
        return "game";
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<PathResponse> removeRoom(@RequestBody RoomDto requestRoomDto) {
        chessRoomService.removeRoom(requestRoomDto);
        return respondPath("/");
    }

    @GetMapping(path = "/rooms/{roomId}/enter")
    @ResponseBody
    public ResponseEntity<PathResponse> enterGame(@PathVariable("roomId") int roomId) {
        return respondPath("/rooms/" + roomId);
    }

    @GetMapping(path = "/rooms/{roomId}/start")
    public ResponseEntity<PathResponse> start(@PathVariable("roomId") int roomId) {
        chessRoomService.startGame(roomId);
        return respondPath("/rooms/" + roomId);
    }

    @PostMapping(path = "/rooms/{roomId}/move")
    public ResponseEntity<GameStateResponse> move(@PathVariable("roomId") int roomId, @RequestBody RouteDto requestRouteDto) {
        GameState state = chessRoomService.moveBoard(roomId, requestRouteDto);
        return ResponseEntity.ok().body(GameStateResponse.of(state));
    }

    @GetMapping(path = "/rooms/{roomId}/end")
    public ResponseEntity<PathResponse> end(@PathVariable("roomId") int roomId) {
        chessRoomService.finishGame(roomId);
        return respondPath("/");
    }

    @GetMapping(path = "/rooms/{roomId}/status")
    public ResponseEntity<StatusDto> status(@PathVariable("roomId") int roomId) {
        GameState state = chessRoomService.readGameState(roomId);
        StatusDto response = new StatusDto(state);
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
