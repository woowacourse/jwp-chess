package chess.service;

import chess.dto.ChessGameDto;
import chess.dto.GameInfoDto;
import chess.dto.MoveDto;
import chess.dto.PathDto;
import chess.dto.PromotionTypeDto;
import chess.dto.SourceDto;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.ChessGameFactory;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import chess.model.repository.BoardRepository;
import chess.model.repository.ChessGameEntity;
import chess.model.repository.ChessGameRepository;
import chess.model.repository.ResultEntity;
import chess.model.repository.ResultRepository;
import chess.model.repository.RoomEntity;
import chess.model.repository.RoomRepository;
import chess.util.BooleanYNConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameRepository chessGameRepository;
    private final BoardRepository boardRepository;
    private final RoomRepository roomRepository;
    private final ResultRepository resultRepository;

    public ChessGameService(ChessGameRepository chessGameRepository,
        BoardRepository boardRepository, RoomRepository roomRepository,
        ResultRepository resultRepository) {
        this.chessGameRepository = chessGameRepository;
        this.boardRepository = boardRepository;
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

        ChessGameEntity chessGameEntity = new ChessGameEntity(
            chessGame.getTurn().getName()
            , "Y"
            , userNames.get(Team.BLACK)
            , userNames.get(Team.WHITE)
            , teamScore.get(Team.BLACK)
            , teamScore.get(Team.WHITE));
        roomEntity.addGame(chessGameEntity);
        roomRepository.save(roomEntity);

        return chessGameEntity;
    }

    public void saveNewUserNames(Map<Team, String> userNames) {
        userNames.values()
            .stream()
            .filter(name -> !resultRepository.findByUserName(name).isPresent())
            .forEach(name -> resultRepository.save(new ResultEntity(name)));
    }

    public ChessGameDto move(MoveDto moveDTO) {
        Integer gameId = moveDTO.getGameId();
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        ChessGame chessGame = combineChessGame(gameId, gameInfo.getTurn());
        MoveState moveState
            = chessGame.move(new MoveInfo(moveDTO.getSource(), moveDTO.getTarget()));
        saveGameAndBoard(chessGameEntity, chessGame, moveState);

        return new ChessGameDto(gameInfo.getUserNames())
            .chessGame(chessGame)
            .moveState(moveState);
    }

    private ChessGameEntity findChessGameEntity(Integer gameId) {
        return chessGameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("gameId(" + gameId + ")가 없습니다."));
    }

    private void saveGameAndBoard(ChessGameEntity chessGameEntity, ChessGame chessGame,
        MoveState moveState) {
        String proceed = BooleanYNConverter.convertYN(moveState != MoveState.KING_CAPTURED);

        if (moveState.isSucceed()) {
            chessGameEntity.update(chessGame, proceed);
            chessGameEntity.saveBoard(chessGame);
            chessGameRepository.save(chessGameEntity);
        }
    }

    private GameInfoDto getGameInfo(ChessGameEntity chessGameEntity) {
        Team team = Team.of(chessGameEntity.getTurnName());
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, chessGameEntity.getBlackName());
        userNames.put(Team.WHITE, chessGameEntity.getWhiteName());

        Map<Team, Double> teamScores = new HashMap<>();
        teamScores.put(Team.BLACK, chessGameEntity.getBlackScore());
        teamScores.put(Team.WHITE, chessGameEntity.getWhiteScore());

        TeamScore teamScore = new TeamScore(teamScores);

        return new GameInfoDto(team, userNames, teamScore);
    }

    private ChessGame combineChessGame(Integer gameId) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        return combineChessGame(gameId, gameInfo.getTurn());
    }

    public ChessGameDto loadChessGame(Integer gameId) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        return new ChessGameDto(gameInfo.getUserNames())
            .chessGame(combineChessGame(gameId, gameInfo.getTurn()));
    }

    private ChessGame combineChessGame(Integer gameId, Team turn) {
        return ChessGameFactory.of(turn, boardRepository.findAllByGameId(gameId));
    }

    public boolean isGameProceed(Integer gameId) {
        return chessGameRepository.findById(gameId).isPresent();
    }

    public ChessGameEntity closeGame(Integer gameId) {
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        chessGameEntity.setProceeding("N");
        chessGameRepository.save(chessGameEntity);
        return chessGameEntity;
    }

    public ChessGameDto promote(PromotionTypeDto promotionTypeDTO) {
        Integer gameId = promotionTypeDTO.getGameId();
        ChessGameEntity chessGameEntity = findChessGameEntity(gameId);
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        ChessGame chessGame = combineChessGame(gameId, gameInfo.getTurn());
        MoveState moveState = chessGame.promote(Type.of(promotionTypeDTO.getPromotionType()));

        saveGameAndBoard(chessGameEntity, chessGame, moveState);

        return new ChessGameDto(gameInfo.getUserNames())
            .chessGame(chessGame)
            .moveState(moveState);
    }

    public PathDto findPath(SourceDto sourceDto) {
        ChessGame chessGame = combineChessGame(sourceDto.getGameId());
        return new PathDto(chessGame.findMovableAreas(Square.of(sourceDto.getSource())));
    }

    public Integer createBy(Integer gameId, Map<Team, String> userNames) {
        Integer roomId = chessGameRepository.findRoomIdById(gameId)
            .orElseThrow(IllegalArgumentException::new);
        return create(roomId, userNames);
    }

    public Optional<Integer> findProceedGameId(Integer roomId) {
        for (ChessGameEntity chessGameEntity : chessGameRepository.findAllByRoomId(roomId)) {
            if (chessGameEntity.isProceeding()) {
                return Optional.ofNullable(chessGameEntity.getId());
            }
        }
        return Optional.empty();
    }
}
