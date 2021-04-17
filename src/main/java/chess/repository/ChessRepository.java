package chess.repository;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.TeamDao;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.Room;
import chess.domain.piece.Piece;
import chess.domain.team.BlackTeam;
import chess.domain.team.Team;
import chess.domain.team.WhiteTeam;
import dto.MoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChessRepository {
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private PieceDao pieceDao;

    public void createRoom(final ChessGame chessGame, final Room room) {
        Long gameId = gameDao.create(chessGame);

        WhiteTeam whiteTeam = chessGame.getWhiteTeam();
        teamDao.create(whiteTeam, gameId);
        pieceDao.create(whiteTeam.getPiecePosition(), whiteTeam.getName(), gameId);

        BlackTeam blackTeam = chessGame.getBlackTeam();
        teamDao.create(blackTeam, gameId);
        pieceDao.create(blackTeam.getPiecePosition(), blackTeam.getName(), gameId);

        room.setGameId(gameId);
        roomDao.create(room);
    }

    public Room loadRoom(Long roomId) {
        return roomDao.load(roomId);
    }

    public List<Room> loadAllRoom() {
        return roomDao.loadAll();
    }

    public ChessGame loadGame(final Long roomId) {
        Room room = roomDao.load(roomId);
        Long gameId = room.getGameId();
        boolean isEnd = gameDao.isEnd(gameId);

        Team team = teamDao.load(gameId, WhiteTeam.DEFAULT_NAME);
        final Map<Position, Piece> whitePieces = pieceDao.load(gameId, WhiteTeam.DEFAULT_NAME);
        WhiteTeam whiteTeam = new WhiteTeam(team.getName(), team.isCurrentTurn(), whitePieces);

        team = teamDao.load(gameId, BlackTeam.DEFAULT_NAME);
        final Map<Position, Piece> blackPieces = pieceDao.load(gameId, BlackTeam.DEFAULT_NAME);
        BlackTeam blackTeam = new BlackTeam(team.getName(), team.isCurrentTurn(), blackPieces);

        whiteTeam.setEnemy(blackTeam);
        blackTeam.setEnemy(whiteTeam);

        return new ChessGame(whiteTeam, blackTeam, isEnd);
    }

    public void saveGame(final Long roomId, final ChessGame chessGame, MoveDto moveDto) {
        Room room = roomDao.load(roomId);
        Long gameId = room.getGameId();

        boolean isEnd = chessGame.isEnd();
        if (isEnd) {
            gameDao.update(gameId, isEnd);
        }

        WhiteTeam whiteTeam = chessGame.getWhiteTeam();
        BlackTeam blackTeam = chessGame.getBlackTeam();

        teamDao.update(gameId, whiteTeam);
        teamDao.update(gameId, blackTeam);

        pieceDao.delete(gameId, moveDto);
        pieceDao.update(gameId, moveDto);

    }
}
