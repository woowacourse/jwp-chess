package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PieceCaptured;
import chess.domain.team.PiecePosition;
import chess.domain.team.Score;
import chess.domain.team.Team;
import chess.webdao.PiecePositionDaoConverter;
import chess.webdao.ChessGameDao;
import chess.webdto.ChessGameDto;
import chess.webdto.ChessGameTableDto;
import chess.webdto.GameRoomDto;
import chess.webdto.GameRoomListDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;

@Service
@Transactional
public class ChessService {
    private final ChessGameDao chessGameDao;

    public ChessService(ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    public void createUser(final String id, final String password) {
        chessGameDao.createUserInfo(id, password);
    }

    public boolean validateUser(final String id, final String password) {
        String userPassword = chessGameDao.readUserPasswordById(id);
        return userPassword.equals(password);
    }

    public GameRoomDto createGameRoom(final String roomName) {
        final int roomId = chessGameDao.createGameRoom(roomName);
        return new GameRoomDto(roomId, roomName);
    }

    public GameRoomListDto loadGameRooms() {
        final List<GameRoomDto> gameRooms = chessGameDao.loadGameRooms();
        return new GameRoomListDto(gameRooms);
    }

    public ChessGameDto createChessGame(final int roomId) {
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());

        chessGameDao.createChessGameInfo(roomId, WHITE_TEAM.asDaoFormat(), chessGame.isPlaying());

        chessGameDao.createTeamInfo(roomId, WHITE_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentWhitePiecePosition()));

        chessGameDao.createTeamInfo(roomId, BLACK_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentBlackPiecePosition()));

        return ChessGameDto.of(roomId, currentTurnTeamAsString(chessGame), chessGame);
    }

    public ChessGameDto readChessGame(final int roomId) {
        final ChessGame chessGame = readChessGameByRoomId(roomId);
        return ChessGameDto.of(roomId, currentTurnTeamAsString(chessGame), chessGame);
    }

    public ChessGameDto move(final int roomId, final String start, final String destination) {
        final ChessGame chessGame = readChessGameByRoomId(roomId);
        chessGame.move(Position.of(start), Position.of(destination));

        chessGameDao.updateChessGameInfo(roomId, currentTurnTeamAsString(chessGame), chessGame.isPlaying());

        chessGameDao.updateTeamInfo(roomId, WHITE_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentWhitePiecePosition()));

        chessGameDao.updateTeamInfo(roomId, BLACK_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentBlackPiecePosition()));

        return ChessGameDto.of(roomId, currentTurnTeamAsString(chessGame), chessGame);
    }

    private ChessGame readChessGameByRoomId(int roomId) {
        final String blackTeamPieceInfo = chessGameDao.readTeamInfo(roomId, BLACK_TEAM.asDaoFormat());
        final Team blackTeam = generateTeam(blackTeamPieceInfo, BLACK_TEAM.asDaoFormat());

        final String whiteTeamPieceInfo = chessGameDao.readTeamInfo(roomId, WHITE_TEAM.asDaoFormat());
        final Team whiteTeam = generateTeam(whiteTeamPieceInfo, WHITE_TEAM.asDaoFormat());

        final ChessGameTableDto chessGameTableDto = chessGameDao.readChessGameInfo(roomId);
        final String currentTurnTeam = chessGameTableDto.getCurrentTurnTeam();
        final boolean isPlaying = chessGameTableDto.getIsPlaying();

        return generateChessGame(blackTeam, whiteTeam, currentTurnTeam, isPlaying);
    }

    private Team generateTeam(final String pieceInfo, final String team) {
        final Map<Position, Piece> piecePositionOfTeam = PiecePositionDaoConverter.asPiecePosition(pieceInfo, team);
        final PiecePosition PiecePosition = new PiecePosition(piecePositionOfTeam);
        return new Team(PiecePosition, new PieceCaptured(), new Score());
    }

    private ChessGame generateChessGame(final Team blackTeam, final Team whiteTeam,
                                        final String currentTurnTeam, final boolean isPlaying) {
        if (WHITE_TEAM.asDaoFormat().equals(currentTurnTeam)) {
            return new ChessGame(blackTeam, whiteTeam, whiteTeam, isPlaying);
        }
        return new ChessGame(blackTeam, whiteTeam, blackTeam, isPlaying);
    }

    private String currentTurnTeamAsString(final ChessGame chessGame) {
        if (chessGame.isWhiteTeamTurn()) {
            return WHITE_TEAM.asDaoFormat();
        }
        return BLACK_TEAM.asDaoFormat();
    }

    public void deleteChessGame(final int roomId) {
        chessGameDao.deleteChessGame(roomId);
    }
}
