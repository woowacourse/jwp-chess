package chess.serviece;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.dto.GameDto;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.ChessResponseDto;
import chess.dto.PieceDto;
import chess.dto.ScoresDto;
import chess.serviece.dto.GameCreationDto;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public Long addGame(GameCreationDto gameCreationDto) {
        ChessGame chessGame = ChessGame.initGame();
        long id = gameDao.save(createGameDto(chessGame, gameCreationDto));
        List<PieceDto> pieceDtos = convertPieceDtos(chessGame.getPieces(), id);
        pieceDao.saveAll(pieceDtos);
        return id;
    }

    private GameDto createGameDto(ChessGame chessGame, GameCreationDto gameCreationDto) {
        PieceColor turnColor = chessGame.getTurnColor();
        if (chessGame.isRunning()) {
            return new GameDto(gameCreationDto.getTitle(), gameCreationDto.getPassword(), turnColor.getName(), "playing");
        }
        return new GameDto(gameCreationDto.getTitle(), gameCreationDto.getPassword(), turnColor.getName(), "finished");
    }

    private List<PieceDto> convertPieceDtos(Map<Position, Piece> pieces, long gameId) {
        return pieces.entrySet()
                .stream()
                .map(entry -> PieceDto.from(entry.getKey(), entry.getValue(), gameId))
                .collect(Collectors.toList());
    }

    public GameDto getGame(Long id, GameDto gameDto) {
        String password = gameDao.findPasswordById(id);
        if (!password.equals(gameDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return gameDao.findGameById(id);
    }

    public List<GameDto> getAllGames() {
        return gameDao.findAll();
    }

    public void removeGame(Long id, GameDto gameDto) {
        String password = gameDao.findPasswordById(id);
        if (!password.equals(gameDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        GameStatus gameStatus = gameDao.findStatusById(id);
        if (gameStatus == GameStatus.PLAYING) {
            throw new IllegalArgumentException("게임이 진행중입니다. 삭제할 수 없습니다.");
        }
        gameDao.removeById(id);
    }

    public ChessResponseDto getChessGame(Long gameId) {
        try {
            List<PieceDto> pieceDtos = pieceDao.findPiecesByGameId(gameId);
            GameDto gameDto = gameDao.findGameById(gameId);
            return new ChessResponseDto(gameId, pieceDtos, gameDto.getTurn(), gameDto.getStatus());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("체스 정보를 읽어올 수 없습니다.");
        }
    }

    public ChessResponseDto movePiece(Long gameId, MoveCommand moveCommand) {
        try {
            ChessGame game = createGame(gameId);
            game.proceedWith(moveCommand);
            pieceDao.removeByPosition(gameId, moveCommand.to());
            pieceDao.updatePosition(gameId, moveCommand.from(), moveCommand.to());
            GameStatus gameStatus = GameStatus.FINISHED;
            if (game.isRunning()) {
                gameStatus = GameStatus.PLAYING;
            }
            gameDao.updateGame(gameId, game.getTurnColor().getName(), gameStatus.getName());
            return getChessGame(gameId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("기물을 움직일 수 없습니다.");
        }
    }

    private ChessGame createGame(Long gameId) {
        try {
            List<PieceDto> pieceDtos = pieceDao.findPiecesByGameId(gameId);
            GameDto gameDto = gameDao.findGameById(gameId);
            Map<Position, Piece> pieces = pieceDtos.stream()
                    .map(PieceDto::toPieceEntry)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new ChessGame(pieces, PieceColor.find(gameDto.getTurn()));
        } catch (Exception e) {
            throw new IllegalArgumentException("게임 정보를 불러올 수 없습니다.");
        }
    }

    public ScoresDto getScore(Long gameId) {
        ChessGame game = createGame(gameId);
        Map<PieceColor, Score> scoresByColor = game.calculateScoreByColor();
        return ScoresDto.of(scoresByColor);
    }

    public ScoresDto finishGame(Long gameId) {
        try {
            gameDao.updateStatus(gameId, GameStatus.FINISHED);
            return getScore(gameId);
        } catch (Exception e) {
            throw new IllegalArgumentException("게임을 종료시킬 수 없습니다.");
        }
    }
}
