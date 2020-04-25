package chess.controller;

import chess.model.dto.ChessGameDto;
import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.model.dto.GameResultDto;
import chess.model.dto.MoveDto;
import chess.model.dto.PathDto;
import chess.model.dto.PromotionTypeDto;
import chess.model.dto.RoomsDto;
import chess.model.dto.SourceDto;
import chess.model.dto.UserNameDto;
import chess.model.dto.UserNamesDto;
import chess.service.ChessGameService;
import chess.service.ResultService;
import chess.service.RoomService;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiWebController {

    private static final Gson GSON = new Gson();

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ResultService resultService;

    @GetMapping("/viewRooms")
    public String viewRooms() {
        return GSON.toJson(roomService.getUsedRooms());
    }

    @PostMapping("/createRoom")
    public RoomsDto createRoom(@RequestBody String req) {
        roomService.addRoom(GSON.fromJson(req, CreateRoomDto.class));
        return roomService.getUsedRooms();
    }

    @PostMapping("/deleteRoom")
    public RoomsDto deleteRoom(@RequestBody String req) {
        roomService.deleteRoom(GSON.fromJson(req, DeleteRoomDto.class));
        return roomService.getUsedRooms();
    }

    @PostMapping("/game/board")
    public ChessGameDto board(@RequestBody String req) {
        Integer gameId = GSON
            .fromJson(JsonParser.parseString(req).getAsJsonObject().get("gameId"), Integer.class);
        return chessGameService.loadChessGame(gameId);
    }

    @PostMapping("/game/move")
    public ChessGameDto move(@RequestBody String req) {
        MoveDto moveDTO = GSON.fromJson(req, MoveDto.class);
        return chessGameService.move(moveDTO);
    }

    @PostMapping("/game/path")
    public PathDto path(@RequestBody String req) {
        SourceDto sourceDto = GSON.fromJson(req, SourceDto.class);
        return chessGameService.getPath(sourceDto);
    }

    @PostMapping("/game/promotion")
    public ChessGameDto promotion(@RequestBody String req) {
        PromotionTypeDto promotionTypeDTO = GSON.fromJson(req, PromotionTypeDto.class);
        return chessGameService.promote(promotionTypeDTO);
    }

    @PostMapping("/game/end")
    public ChessGameDto end(@RequestBody String req) {
        Integer gameId = GSON
            .fromJson(JsonParser.parseString(req).getAsJsonObject().get("gameId"), Integer.class);
        return chessGameService.endGame(gameId);
    }

    @GetMapping("/result/viewUsers")
    public UserNamesDto viewUsers() {
        return resultService.getUsers();
    }

    @GetMapping("/result/userResult")
    public GameResultDto userResult(@RequestBody String req) {
        return resultService.getResult(GSON.fromJson(req, UserNameDto.class));
    }
}
