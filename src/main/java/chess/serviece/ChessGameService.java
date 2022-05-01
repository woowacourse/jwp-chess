package chess.serviece;

import chess.controller.request.RoomCreationRequest;
import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.entity.Game;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.serviece.dto.GameDto;
import chess.dto.PieceDto;
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
        long id = gameDao.save(createGame(chessGame, roomCreationRequest));
        List<PieceDto> pieceDtos = convertPieceDtos(chessGame.getPieces(), id);
        pieceDao.saveAll(pieceDtos);
        return id;
    }

    private Game createGame(ChessGame chessGame, RoomCreationRequest roomCreationRequest) {
        PieceColor turnColor = chessGame.getTurnColor();
        if (chessGame.isRunning()) {
            return new Game(roomCreationRequest.getTitle(), roomCreationRequest.getPassword(), turnColor.getName(), "playing");
        }
        return new Game(roomCreationRequest.getTitle(), roomCreationRequest.getPassword(), turnColor.getName(), "finished");
    }

    private List<PieceDto> convertPieceDtos(Map<Position, Piece> pieces, Long gameId) {
        return pieces.entrySet()
                .stream()
                .map(entry -> PieceDto.from(entry.getKey(), entry.getValue(), gameId))
                .collect(Collectors.toList());
    }

    public GameDto getGame(Long id) {
        Game game = gameDao.findGameById(id);
        return GameDto.from(game);
    }

    public List<PieceDto> getPiecesOfGame(Long gameId) {
        return pieceDao.findPiecesByGameId(gameId);
    }

    public List<GameDto> getAllGames() {
        List<Game> games = gameDao.findAll();
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
        List<PieceDto> pieceDtos = pieceDao.findPiecesByGameId(gameId);
        Game game = gameDao.findGameById(gameId);
        Map<Position, Piece> pieces = pieceDtos.stream()
                .map(PieceDto::toPieceEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ChessGame(pieces, PieceColor.find(game.getTurn()));
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