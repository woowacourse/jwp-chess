package chess.service;

import chess.dto.repository.GameIdDto;
import chess.dto.repository.GameInfoDto;
import chess.dto.repository.MovableAreasDto;
import chess.dto.view.GameInformationDto;
import chess.dto.view.MoveDto;
import chess.dto.view.PromotionTypeDto;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.ChessGameFactory;
import chess.model.domain.board.Square;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import chess.model.repository.ChessGameEntity;
import chess.model.repository.ChessGameRepository;
import chess.model.repository.ResultEntity;
import chess.model.repository.ResultRepository;
import chess.model.repository.RoomEntity;
import chess.model.repository.RoomRepository;
import chess.util.TFAndYNConverter;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameRepository chessGameRepository;
    private final RoomRepository roomRepository;
    private final ResultRepository resultRepository;

    public ChessGameService(ChessGameRepository chessGameRepository, RoomRepository roomRepository,
        ResultRepository resultRepository) {
        this.chessGameRepository = chessGameRepository;
        this.roomRepository = roomRepository;
        this.resultRepository = resultRepository;
    }

    public Integer create(Integer roomId, Map<Team, String> userNames) {
        Integer gameId = saveNewGameInfo(userNames, roomId);
        saveNewUserNames(userNames);
        return gameId;
    }

    public Integer saveNewGameInfo(Map<Team, String> userNames, Integer roomId) {
        ChessGame chessGame = ChessGameFactory.create();
        ChessGameEntity chessGameEntity = saveGame(userNames, roomId, chessGame);
        chessGameEntity.saveBoard(chessGame);
        chessGameRepository.save(chessGameEntity);
        return chessGameEntity.getId();
    }

    private ChessGameEntity saveGame(Map<Team, String> userNames, Integer roomId,
        ChessGame chessGame) {
        RoomEntity roomEntity = roomRepository.findById(roomId)
            .orElseThrow(IllegalArgumentException::new);
        Map<Team, Double> teamScore = chessGame.deriveTeamScore().getTeamScore();

        ChessGameEntity chessGameEntity = makeChessGameEntity(userNames, chessGame, teamScore);
        roomEntity.addGame(chessGameEntity);
        roomRepository.save(roomEntity);

        return chessGameEntity;
    }

    private ChessGameEntity makeChessGameEntity(Map<Team, String> userNames, ChessGame chessGame,
        Map<Team, Double> teamScore) {
        return new ChessGameEntity(
            chessGame.getTurn().getName()
            , "Y"
            , userNames.get(Team.BLACK)
            , userNames.get(Team.WHITE)
            , teamScore.get(Team.BLACK)
            , teamScore.get(Team.WHITE));
    }

    public void saveNewUserNames(Map<Team, String> userNames) {
        userNames.values()
            .stream()
            .filter(name -> !resultRepository.findByUserName(name).isPresent())
            .forEach(name -> resultRepository.save(new ResultEntity(name)));
    }

    public GameInformationDto move(Integer gameId, MoveDto moveDTO) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        ChessGame chessGame = combineChessGame(gameId, gameInfo.getTurn());
        MoveState moveState
            = chessGame.move(new MoveInfo(moveDTO.getSource(), moveDTO.getTarget()));
        saveGameAndBoard(chessGameEntity, chessGame, moveState);

        return new GameInformationDto(gameInfo.getUserNames())
            .chessGame(chessGame)
            .moveState(moveState);
    }

    private ChessGameEntity findChessGameEntity(Integer gameId) {
        return chessGameRepository.findProceedingById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("진행중인 gameId(" + gameId + ")가 없습니다."));
    }

    private void saveGameAndBoard(ChessGameEntity chessGameEntity, ChessGame chessGame,
        MoveState moveState) {
        String proceed = TFAndYNConverter.convertYN(moveState != MoveState.KING_CAPTURED);

        if (moveState.isSucceed()) {
            chessGameEntity.update(chessGame, proceed);
            chessGameEntity.saveBoard(chessGame);
            chessGameRepository.save(chessGameEntity);
        }
    }

    private GameInfoDto getGameInfo(ChessGameEntity chessGameEntity) {
        Team team = Team.of(chessGameEntity.getTurnName());
        return new GameInfoDto(team, chessGameEntity.makeUserNames(),
            chessGameEntity.makeTeamScore());
    }

    private ChessGame combineChessGame(Integer gameId) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        return combineChessGame(gameId, gameInfo.getTurn());
    }

    public GameInformationDto loadChessGame(Integer gameId) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        return new GameInformationDto(gameInfo.getUserNames())
            .chessGame(combineChessGame(gameId, gameInfo.getTurn()));
    }

    private ChessGame combineChessGame(Integer gameId, Team turn) {
        ChessGameEntity chessGameEntity = chessGameRepository.findById(gameId)
            .orElseThrow(IllegalArgumentException::new);
        return ChessGameFactory.of(turn, chessGameEntity.getBoardEntities());
    }

    public ChessGameEntity closeGame(Integer gameId) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        chessGameEntity.setProceeding("N");
        chessGameRepository.save(chessGameEntity);
        return chessGameEntity;
    }

    public GameInformationDto promote(Integer gameId, PromotionTypeDto promotionTypeDTO) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        ChessGame chessGame = combineChessGame(gameId, gameInfo.getTurn());
        MoveState moveState = chessGame.promote(Type.of(promotionTypeDTO.getPromotionType()));

        saveGameAndBoard(chessGameEntity, chessGame, moveState);

        return new GameInformationDto(gameInfo.getUserNames())
            .chessGame(chessGame)
            .moveState(moveState);
    }

    public MovableAreasDto findPath(Integer gameId, String source) {
        ChessGame chessGame = combineChessGame(gameId);
        return new MovableAreasDto(chessGame.findMovableAreas(Square.of(source)));
    }

    public Optional<Integer> findProceedGameId(Integer roomId) {
        return chessGameRepository.findProceedingByRoomId(roomId);
    }

    public GameIdDto loadGame(Integer roomId, Map<Team, String> userNames) {
        return new GameIdDto(findProceedGameId(roomId)
            .orElseGet(() -> create(roomId, userNames)));
    }
}
