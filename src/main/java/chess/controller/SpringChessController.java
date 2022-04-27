package chess.controller;

import chess.database.dto.RoomDto;
import chess.domain.game.GameState;
import chess.dto.Arguments;
import chess.dto.BoardResponse;
import chess.dto.GameStateResponse;
import chess.dto.PathResponse;
import chess.service.ChessRoomService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringChessController {

    @Autowired
    private ChessRoomService chessRoomService;

    @GetMapping("/")
    public String showRooms(Model model) {
        Map<String, String> roomStates = chessRoomService.findAllRoomState();
        model.addAttribute("roomStates", roomStates);
        return "rooms";
    }

    @PostMapping("/rooms/create")
    @ResponseBody
    public ResponseEntity<PathResponse> createRoom(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String roomName = jsonObject.get("roomName").getAsString();
        String roomPassword = jsonObject.get("roomPassword").getAsString();
        RoomDto roomDto = chessRoomService.createNewRoom(roomName, roomPassword);
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

    @GetMapping(path = "/rooms/enter/{roomName}")
    @ResponseBody
    public ResponseEntity<PathResponse> enterGame(Model model,
        @PathVariable("roomName") String roomName) {
        RoomDto roomDto = chessRoomService.findByName(roomName);
        return respondPath("/rooms/" + roomDto.getId());
    }

    @GetMapping(path = "/rooms/{roomId}/start")
    public ResponseEntity<PathResponse> start(@PathVariable("roomId") int roomId) {
        chessRoomService.startGame(roomId);
        return respondPath("/rooms/" + roomId);
    }

    @PostMapping(path = "/rooms/{roomId}/move")
    public ResponseEntity<GameStateResponse> move(@PathVariable("roomId") int roomId,
        @RequestBody String body) {
        final Arguments arguments = Arguments.ofJson(body, Command.MOVE.getParameters());
        GameState state = chessRoomService.moveBoard(roomId, arguments);
        return ResponseEntity.ok().body(GameStateResponse.of(state));
    }

    @GetMapping(path = "/rooms/{roomId}/end")
    public ResponseEntity<PathResponse> end(@PathVariable("roomId") int roomId) {
        chessRoomService.finishGame(roomId);
        return respondPath("/");
    }

    @GetMapping(path = "/rooms/{roomId}/status")
    public ResponseEntity<Map<String, Object>> status(@PathVariable("roomId") int roomId) {
        GameState state = chessRoomService.readGameState(roomId);
        return ResponseEntity.ok().body(Map.of("board", BoardResponse.of(state.getPointPieces()),
            "score", state.getColorScore())
        );
    }

    @PostMapping("/rooms/remove")
    public ResponseEntity<PathResponse> removeRoom(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String roomName = jsonObject.get("roomName").getAsString();
        String roomPassword = jsonObject.get("roomPassword").getAsString();
        RoomDto roomDto = new RoomDto(roomName, roomPassword);
        chessRoomService.removeRoom(roomDto);
        return respondPath("/");
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
