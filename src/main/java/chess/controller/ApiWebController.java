package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.dto.GameResultDto;
import chess.dto.MoveDto;
import chess.dto.PathDto;
import chess.dto.PromotionTypeDto;
import chess.dto.RoomsDto;
import chess.dto.SourceDto;
import chess.dto.UserNameDto;
import chess.dto.UserNamesDto;
import chess.model.repository.ChessGameEntity;
import chess.service.ChessGameService;
import chess.service.ResultService;
import chess.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/viewRooms")
    public RoomsDto viewRooms() {
        return roomService.getUsedRooms();
    }

    @PostMapping("/createRoom")
    public RoomsDto createRoom(@RequestBody CreateRoomDto createRoomDto) {
        roomService.addRoom(createRoomDto);
        return roomService.getUsedRooms();
    }

    @PostMapping("/deleteRoom")
    public RoomsDto deleteRoom(@RequestBody DeleteRoomDto deleteRoomDto) {
        roomService.deleteRoom(deleteRoomDto);
        chessGameService.findProceedGameId(deleteRoomDto.getRoomId())
            .ifPresent(chessGameService::closeGame);

        return roomService.getUsedRooms();
    }

    @PostMapping("/game/{gameId}/board")
    public ChessGameDto board(@PathVariable Integer gameId) {
        return chessGameService.loadChessGame(gameId);
    }

    @PostMapping("/game/move")
    public ChessGameDto move(@RequestBody MoveDto MoveDto) {
        ChessGameDto chessGameDto = chessGameService.move(MoveDto);
        resultService.updateResult(chessGameDto);
        return chessGameDto;
    }

    @PostMapping("/game/path")
    public PathDto path(@RequestBody SourceDto sourceDto) {
        return chessGameService.findPath(sourceDto);
    }

    @PostMapping("/game/promotion")
    public ChessGameDto promotion(@RequestBody PromotionTypeDto promotionTypeDTO) {
        return chessGameService.promote(promotionTypeDTO);
    }

    @PostMapping("/game/{gameId}/end")
    public ChessGameDto end(@PathVariable Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameService.closeGame(gameId);
        resultService.setGameResult(chessGameEntity);
        return new ChessGameDto(chessGameEntity.makeTeamScore(), chessGameEntity.makeUserNames());
    }

    @GetMapping("/result/viewUsers")
    public UserNamesDto viewUsers() {
        return resultService.getUsers();
    }

    @PostMapping("/result/userResult")
    public GameResultDto userResult(@RequestBody UserNameDto userNameDto) {
        return resultService.getResult(userNameDto);
    }
}
