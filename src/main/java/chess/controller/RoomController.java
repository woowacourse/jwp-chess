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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoomController {

    @Autowired
    private ChessRoomService chessRoomService;

    @GetMapping("/rooms")
    public String showRooms(Model model) {
        List<RoomDto> roomDtoList = chessRoomService.findAllRoom();
        Map<String, String> roomStates = new HashMap<>();
        for(RoomDto roomDto : roomDtoList){
            String state = chessRoomService.readGameState(roomDto.getId()).getState();
            if(state.equals("RUNNING")){
                roomStates.put(roomDto.getName(), "disabled");
                continue;
            }
            roomStates.put(roomDto.getName(), "");
        }
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
        GameState state = chessRoomService.readGameState(roomId);
        GameStateResponse response = GameStateResponse.of(state);
        model.addAttribute("roomId", roomId);
        model.addAttribute("response", response);
        return "game";
    }

    @GetMapping(path = "/rooms/enter/{roomName}")
    @ResponseBody
    public ResponseEntity<PathResponse>  enterGame(Model model, @PathVariable("roomName") String roomName) {
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
        return respondPath("/rooms");
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
        RoomDto findRoomName = chessRoomService.findByName(roomName);
        GameState state = chessRoomService.readGameState(findRoomName.getId());
        if(state.getState().equals("RUNNING")){
            throw new IllegalArgumentException("[ERROR] 진행 중인 게임은 삭제할 수 없습니다.");
        }
        if (!findRoomName.getPassword().equals(roomPassword)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }
        chessRoomService.removeRoom(findRoomName);
        return respondPath("/rooms");
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
