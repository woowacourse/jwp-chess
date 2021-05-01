package chess.domain.game;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.dto.GameDto;
import chess.dao.dto.PieceDto;
import chess.dao.dto.RoomDto;
import chess.domain.game.board.Board;
import chess.domain.game.board.piece.Piece;
import chess.domain.game.team.Team;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final GameDao gameDao;
    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    public GameRepository(final GameDao gameDao, final PieceDao pieceDao, final RoomDao roomDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    public long add(final String name, final long hostId) {
        final long gameId = gameDao.insert();
        roomDao.insert(RoomDto.of(gameId, hostId, name));
        pieceDao.insertAll(gameId, Board.createWithInitialLocation().toList());
        return gameId;
    }

    public Game findById(final long gameId) {
        final GameDto gameDto = gameDao.findById(gameId);
        final RoomDto roomDto = roomDao.findByGameId(gameId);
        final List<PieceDto> pieceDtos = pieceDao.selectAll(gameId);
        return GameFactory.of(gameDto, pieceDtos, roomDto);
    }

    public void update(final Game game) {
        final long gameId = game.getId();
        updatePieces(gameId, game);
        updateGameStatus(gameId, game.isFinished());
        updateTurn(gameId, game.getTurn());
    }

    private void updatePieces(final long gameId, final Game modifiedGame) {
        deletePieces(gameId, modifiedGame);
        modifyPieces(gameId, modifiedGame);
    }

    private void deletePieces(final long gameId, final Game modifiedGame) {
        final List<Long> ids = findById(gameId).toPieceIds();
        final List<Long> modifiedIds = modifiedGame.toPieceIds();

        ids.removeAll(modifiedIds);
        pieceDao.deleteBatchByIds(ids);
    }

    private void modifyPieces(final long gameId, final Game modifiedGame) {
        final Game game = findById(gameId);
        final List<Piece> pieces = game.bringMovedPieces(modifiedGame);
        final List<PieceDto> pieceDtos = pieces.stream()
            .map(piece -> PieceDto.of(gameId, piece))
            .collect(Collectors.toList());

        pieceDao.updateBatch(pieceDtos);
    }

    private void updateGameStatus(final long gameId, boolean isFinished) {
        gameDao.updateGameStatus(gameId, isFinished);
    }

    private void updateTurn(final long gameId, Team turn) {
        gameDao.updateTurn(gameId, turn.getValue());
    }

}
