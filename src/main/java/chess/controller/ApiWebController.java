package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.dto.GameIdDto;
import chess.dto.GameResultDto;
import chess.dto.MoveDto;
import chess.dto.PathDto;
import chess.dto.PromotionTypeDto;
import chess.dto.RoomsDto;
import chess.dto.SourceDto;
import chess.dto.UserNameDto;
import chess.dto.UserNamesDto;
import chess.model.domain.piece.Team;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import chess.service.RoomService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiWebController {

    private final ChessGameService chessGameService;
    private final RoomService roomService;
    private final ResultService resultService;

    public ApiWebController(ChessGameService chessGameService, RoomService roomService,
        ResultService resultService) {
        this.chessGameService = chessGameService;
        this.roomService = roomService;
        this.resultService = resultService;
    }

    @GetMapping("/rooms")
    public RoomsDto getRooms() {
        return roomService.getUsedRooms();
    }

    @PostMapping("/room")
    public RoomsDto postRoom(@RequestBody CreateRoomDto createRoomDto) {
        roomService.addRoom(createRoomDto);

        return roomService.getUsedRooms();
    }

    @DeleteMapping("/room")
    public RoomsDto deleteRoom(@RequestBody DeleteRoomDto deleteRoomDto) {
        roomService.deleteRoom(deleteRoomDto);
        chessGameService.findProceedGameId(deleteRoomDto.getRoomId())
            .ifPresent(chessGameService::closeGame);

        return roomService.getUsedRooms();
    }

    @PostMapping("/room/{roomId}/game")
    public GameIdDto postGame(@PathVariable Integer roomId,
        @RequestParam(defaultValue = "WHITE") String whiteName,
        @RequestParam(defaultValue = "BLACK") String blackName,
        @RequestParam String way) {
        if (way.equals("new")) {
            return createGame(roomId, whiteName, blackName);
        }
        if (way.equals("load")) {
            return loadGame(roomId, whiteName, blackName);
        }
        return null;
    }

    private GameIdDto createGame(Integer roomId, String whiteName, String blackName) {
        chessGameService.findProceedGameId(roomId).ifPresent(chessGameService::closeGame);
        Map<Team, String> userNames = makeUserNames(whiteName, blackName);
        chessGameService.saveNewUserNames(userNames);
        return new GameIdDto(chessGameService.saveNewGameInfo(userNames, roomId));
    }

    private GameIdDto loadGame(Integer roomId, String whiteName, String blackName) {
        return new GameIdDto(chessGameService.findProceedGameId(roomId)
            .orElseGet(() -> chessGameService
                .create(roomId, makeUserNames(whiteName, blackName))));
    }

    @GetMapping("/room/{roomId}/game/{gameId}/board")
    public ChessGameDto getBoard(@PathVariable Integer gameId) {
        return chessGameService.loadChessGame(gameId);
    }

    @PostMapping("/room/{roomId}/game/{gameId}/move")
    public ChessGameDto move(@PathVariable Integer gameId, @RequestBody MoveDto MoveDto) {
        ChessGameDto chessGameDto = chessGameService.move(gameId, MoveDto);
        resultService.updateResult(chessGameDto);
        return chessGameDto;
    }

    @PostMapping("/room/{roomId}/game/{gameId}/path")
    public PathDto path(@PathVariable Integer gameId, @RequestBody SourceDto sourceDto) {
        return chessGameService.findPath(gameId, sourceDto);
    }

    @PostMapping("/room/{roomId}/game/{gameId}/promotion")
    public ChessGameDto promotion(@PathVariable Integer gameId,
        @RequestBody PromotionTypeDto promotionTypeDTO) {
        return chessGameService.promote(gameId, promotionTypeDTO);
    }

    @PostMapping("/room/{roomId}/game/{gameId}/end")
    public ChessGameDto end(@PathVariable Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        return new ChessGameDto(chessGameEntity.makeUserNames())
            .teamScore(chessGameEntity.makeTeamScore());
    }

    @GetMapping("/result/viewUsers")
    public UserNamesDto viewUsers() {
        return resultService.getUsers();
    }

    @PostMapping("/result/userResult")
    public GameResultDto userResult(@RequestBody UserNameDto userNameDto) {
        return resultService.getResult(userNameDto);
    }

    private Map<Team, String> makeUserNames(String whiteName, String blackName) {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, blackName);
        userNames.put(Team.WHITE, whiteName);
        return userNames;
    }
}
