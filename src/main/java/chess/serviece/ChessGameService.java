package chess.serviece;

import chess.dao.PieceDao;
import chess.dao.SpringGameDao;
import chess.dao.dto.GameDto;
import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.domain.GameStatus;
import chess.serviece.dto.GameCreationDto;
import chess.dto.PieceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessGameService {

    private final SpringGameDao gameDao;
    private final PieceDao pieceDao;

    public ChessGameService(SpringGameDao gameDao, PieceDao pieceDao) {
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
        return gameDao.findById(id);
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
}
