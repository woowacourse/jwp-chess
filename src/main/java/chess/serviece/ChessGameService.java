package chess.serviece;

import chess.dao.PieceDao;
import chess.dao.SpringGameDao;
import chess.dao.dto.GameDto;
import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
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

    public long addGame(GameCreationDto gameCreationDto) {
        ChessGame chessGame = ChessGame.initGame();
        long id = gameDao.save(createGameDto(chessGame, gameCreationDto));
        List<PieceDto> pieceDtos = convertPieceDtos(chessGame.getPieces(), id);
        pieceDao.saveAll(pieceDtos);
        return id;
    }

    public GameDto createGameDto(ChessGame chessGame, GameCreationDto gameCreationDto) {
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

}
