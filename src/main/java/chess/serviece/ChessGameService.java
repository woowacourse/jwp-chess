package chess.serviece;

import chess.controller.request.RoomCreationRequest;
import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.entity.GameEntity;
import chess.dao.entity.PieceEntity;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.serviece.dto.GameDto;
import chess.serviece.dto.PieceDto;
import chess.serviece.dto.PasswordDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessGameService {

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public ChessGameService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public Long addGame(RoomCreationRequest roomCreationRequest) {
        ChessGame chessGame = ChessGame.initGame();
        Long id = gameDao.save(createGame(chessGame, roomCreationRequest));
        List<PieceEntity> pieces = convertPieceEntities(chessGame.getPieces(), id);
        pieceDao.saveAll(pieces);
        return id;
    }

    private List<PieceEntity> convertPieceEntities(Map<Position, Piece> pieces, Long gameId) {
        return pieces.entrySet()
                .stream()
                .map(entry -> {
                    Position position = entry.getKey();
                    PieceType type = entry.getValue().getType();
                    PieceColor color = entry.getValue().getColor();
                    return new PieceEntity(position.getName(), type.getName(), color.getName(), gameId);
                })
                .collect(Collectors.toList());
    }

    private GameEntity createGame(ChessGame chessGame, RoomCreationRequest roomCreationRequest) {
        PieceColor turnColor = chessGame.getTurnColor();
        if (chessGame.isRunning()) {
            return new GameEntity(roomCreationRequest.getTitle(), roomCreationRequest.getPassword(), turnColor.getName(), "playing");
        }
        return new GameEntity(roomCreationRequest.getTitle(), roomCreationRequest.getPassword(), turnColor.getName(), "finished");
    }

    public GameDto getGame(Long id) {
        GameEntity game = gameDao.findGameById(id);
        return GameDto.from(game);
    }

    public List<PieceDto> getPiecesOfGame(Long gameId) {
        List<PieceEntity> pieceEntities = pieceDao.findPiecesByGameId(gameId);
        return convertToPieceDtos(pieceEntities);
    }

    private List<PieceDto> convertToPieceDtos(List<PieceEntity> pieceEntities) {
        return pieceEntities.stream()
                .map(pieceEntity -> new PieceDto(pieceEntity.getPosition(), pieceEntity.getColor(), pieceEntity.getType()))
                .collect(Collectors.toList());
    }

    public List<GameDto> getAllGames() {
        List<GameEntity> games = gameDao.findAll();
        return games.stream()
                .map(GameDto::from)
                .collect(Collectors.toList());
    }

    public void deleteGame(Long id, PasswordDto passwordDto) {
        String password = gameDao.findPasswordById(id);
        if (!password.equals(passwordDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        GameStatus gameStatus = gameDao.findStatusById(id);
        if (gameStatus == GameStatus.PLAYING) {
            throw new IllegalArgumentException("게임이 진행중입니다. 삭제할 수 없습니다.");
        }
        gameDao.removeById(id);
    }

    public void movePiece(Long gameId, MoveCommand moveCommand) {
        ChessGame game = createGame(gameId);
        game.proceedWith(moveCommand);
        pieceDao.removeByPosition(gameId, moveCommand.to());
        pieceDao.updatePosition(gameId, moveCommand.from(), moveCommand.to());
        GameStatus gameStatus = GameStatus.FINISHED;
        if (game.isRunning()) {
            gameStatus = GameStatus.PLAYING;
        }
        gameDao.updateGame(gameId, game.getTurnColor().getName(), gameStatus.getName());
    }

    private ChessGame createGame(Long gameId) {
        List<PieceEntity> pieceEntities = pieceDao.findPiecesByGameId(gameId);
        GameEntity game = gameDao.findGameById(gameId);
        Map<Position, Piece> pieces = convertToPieces(pieceEntities);
        return new ChessGame(pieces, PieceColor.find(game.getTurn()));
    }

    private Map<Position, Piece> convertToPieces(List<PieceEntity> pieceEntities) {
        return pieceEntities.stream()
                .collect(Collectors.toMap(
                        pieceEntity -> Position.of(pieceEntity.getPosition()),
                        pieceEntity -> PieceFactory.find(pieceEntity.getType(), pieceEntity.getColor()),
                        (piece, piece2) -> piece)
                );
    }


    public Map<PieceColor, Score> getScore(Long gameId) {
        ChessGame game = createGame(gameId);
        return game.calculateScoreByColor();
    }

    public Map<PieceColor, Score> finishGame(Long gameId) {
        gameDao.updateStatus(gameId, GameStatus.FINISHED);
        return getScore(gameId);
    }
}
