package chess.service;

import chess.model.domain.board.CastlingElement;
import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.ChessBoard;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.EnPassant;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import chess.model.dto.ChessGameDto;
import chess.model.dto.GameInfoDto;
import chess.model.dto.MoveDto;
import chess.model.dto.PathDto;
import chess.model.dto.PromotionTypeDto;
import chess.model.dto.SourceDto;
import chess.model.repository.BoardEntity;
import chess.model.repository.BoardRepository;
import chess.model.repository.ChessGameEntity;
import chess.model.repository.ChessGameRepository;
import chess.model.repository.ResultEntity;
import chess.model.repository.ResultRepository;
import chess.model.repository.RoomEntity;
import chess.model.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        ChessGame chessGame = new ChessGame();
        ChessGameEntity chessGameEntity = saveGame(userNames, roomId, chessGame);
        saveBoard(chessGame, chessGameEntity);
        return chessGameEntity.getId();
    }

    private ChessGameEntity saveGame(Map<Team, String> userNames, Integer roomId,
        ChessGame chessGame) {
        RoomEntity roomEntity = roomRepository.findById(roomId)
                .orElseThrow(IllegalArgumentException::new);
        TeamScore teamScore = chessGame.deriveTeamScore();
        Map<Team, Double> teamScores = teamScore.getTeamScore();

        ChessGameEntity chessGameEntity = new ChessGameEntity(
                chessGame.getTurnName()
                , "Y"
                , userNames.get(Team.BLACK)
                , userNames.get(Team.WHITE)
                , teamScores.get(Team.BLACK)
                , teamScores.get(Team.WHITE));
        roomEntity.addGame(chessGameEntity);
        roomRepository.save(roomEntity);

        return chessGameEntity;
    }

    private void saveBoard(ChessGame chessGame, ChessGameEntity chessGameEntity) {
        Map<Square, Square> enPassants = makeEnPassants(chessGame);
        Map<Square, Boolean> castlingElements = makeCastlingElements(chessGame.getChessBoard(),
            chessGame.getCastlingElements());
        Map<Square, Piece> chessBoard = chessGame.getChessBoard();

        for (Square square : chessBoard.keySet()) {
            chessGameEntity.addBoard(new BoardEntity(
                square.getName(),
                PieceFactory.getName(chessBoard.get(square)),
                convertYN(castlingElements.get(square)),
                enPassants.keySet().stream()
                    .filter(key -> enPassants.containsKey(square))
                    .map(enSquare -> enPassants.get(square).getName())
                    .findFirst()
                    .orElse(null)));
        }

        chessGameRepository.save(chessGameEntity);
    }

    public void saveNewUserNames(Map<Team, String> userNames) {
        userNames.values()
            .stream()
            .filter(name -> !resultRepository.findByUserName(name).isPresent())
            .forEach(name -> resultRepository.save(new ResultEntity(name)));
    }

    public ChessGameDto move(MoveDto moveDTO) {
        Integer gameId = moveDTO.getGameId();
        ChessGameEntity chessGameEntity = chessGameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("gameId(" + gameId + ")가 없습니다."));
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        ChessGame chessGame = combineChessGame(gameId, gameInfo.getTurn());
        MoveState moveState
            = chessGame.move(new MoveInfo(moveDTO.getSource(), moveDTO.getTarget()));
        Map<Team, String> userNames = gameInfo.getUserNames();

        String proceed = convertYN(moveState != MoveState.KING_CAPTURED);

        if (moveState.isSucceed()) {
            chessGameEntity.update(chessGame, proceed);
            chessGameEntity.clearBoard();
            saveBoard(chessGame, chessGameEntity);
        }
        return new ChessGameDto(chessGame, moveState, chessGame.deriveTeamScore(), userNames);
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

    private Map<Square, Square> makeEnPassants(ChessGame chessGame) {
        return chessGame.getEnPassants().entrySet().stream()
            .collect(Collectors.toMap(Entry::getValue, Entry::getKey));
    }

    private Map<Square, Boolean> makeCastlingElements(Map<Square, Piece> chessBoard,
        Set<CastlingSetting> castlingElements) {
        return chessBoard.keySet().stream()
            .collect(Collectors.toMap(square -> square,
                square -> makeCastlingElements(square, chessBoard.get(square), castlingElements)));
    }

    private boolean makeCastlingElements(Square square, Piece piece,
        Set<CastlingSetting> castlingElements) {
        return castlingElements.stream()
            .anyMatch(castlingSetting -> castlingSetting.isCastlingBefore(square, piece));
    }

    private ChessGame combineChessGame(Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("gameId(" + gameId + ")가 없습니다."));
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        return combineChessGame(gameId, gameInfo.getTurn());
    }

    public ChessGameDto loadChessGame(Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("gameId(" + gameId + ")가 없습니다."));
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        return new ChessGameDto(combineChessGame(gameId, gameInfo.getTurn()),
                gameInfo.getUserNames());
    }

    private ChessGame combineChessGame(Integer gameId, Team turn) {
        Map<Square, Piece> chessBoard = new HashMap<>();
        Set<CastlingSetting> castlingElements = new HashSet<>();
        Map<Square, Square> enPassants = new HashMap<>();

        for (BoardEntity boardEntity : boardRepository.findAllByGameId(gameId)) {
            chessBoard.put(Square.of(boardEntity.getSquareName()),
                    PieceFactory.getPiece(boardEntity.getPieceName()));
            addCastlingElements(castlingElements, boardEntity);
            addEnPassants(enPassants, boardEntity);
        }

        return new ChessGame(ChessBoard.of(chessBoard), turn, CastlingElement.of(castlingElements),
                new EnPassant(enPassants));
    }

    private void addCastlingElements(final Set<CastlingSetting> castlingElements, final BoardEntity boardEntity) {
        if (boardEntity.isExistCastlingElement()) {
            castlingElements
                    .add(CastlingSetting.of(Square.of(boardEntity.getSquareName()),
                            PieceFactory.getPiece(boardEntity.getPieceName())));
        }
    }

    private void addEnPassants(final Map<Square, Square> enPassants, final BoardEntity boardEntity) {
        if (boardEntity.isEnPassantNameNotNull()) {
            enPassants.put(Square.of(boardEntity.getEnPassantName()),
                    Square.of(boardEntity.getSquareName()));
        }
    }

    public boolean isGameProceed(Integer gameId) {
        return chessGameRepository.findById(gameId).isPresent();
    }

    public ChessGameEntity closeGame(Integer gameId) {
        ChessGameEntity chessGameEntity = chessGameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("gameId(" + gameId + ")가 없습니다."));

        chessGameEntity.setProceeding("N");
        chessGameRepository.save(chessGameEntity);
        return chessGameEntity;

    }

    public ChessGameDto promote(PromotionTypeDto promotionTypeDTO) {
        Integer gameId = promotionTypeDTO.getGameId();
        ChessGameEntity chessGameEntity = chessGameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("gameId(" + gameId + ")가 없습니다."));
        GameInfoDto gameInfo = getGameInfo(chessGameEntity);
        ChessGame chessGame = combineChessGame(gameId, gameInfo.getTurn());
        MoveState moveState = chessGame.promote(Type.of(promotionTypeDTO.getPromotionType()));

        if (moveState.isSucceed()) {
            chessGameEntity.update(chessGame, "Y");
            chessGameEntity.clearBoard();
            saveBoard(chessGame, chessGameEntity);
        }

        return new ChessGameDto(chessGame, moveState, chessGame.deriveTeamScore(),
            gameInfo.getUserNames());
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

    public static String convertYN(boolean changer) {
        return changer ? "Y" : "N";
    }
}
