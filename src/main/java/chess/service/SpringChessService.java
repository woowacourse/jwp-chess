package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PieceCaptured;
import chess.domain.team.PiecePosition;
import chess.domain.team.Score;
import chess.domain.team.Team;
import chess.webdao.PiecePositionDaoConverter;
import chess.webdto.*;
import chess.webdao.SpringChessGameDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;

@Service
public class SpringChessService {
    private final SpringChessGameDao springChessGameDao;

    public SpringChessService(SpringChessGameDao springChessGameDao) {
        this.springChessGameDao = springChessGameDao;
    }

    public GameRoomDto createGameRoom(final String roomName) {
        final int roomId = springChessGameDao.createGameRoom(roomName);
        return new GameRoomDto(roomId, roomName);
    }

    public GameRoomListDto loadGameRooms() {
        final List<GameRoomDto> gameRooms = springChessGameDao.loadGameRooms();
        return new GameRoomListDto(gameRooms);
    }

    public ChessGameDto createChessGame(final int roomId) {
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());

        springChessGameDao.createChessGameInfo(roomId, WHITE_TEAM.asDaoFormat(), chessGame.isPlaying());

        springChessGameDao.createTeamInfo(roomId, WHITE_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentWhitePiecePosition()));

        springChessGameDao.createTeamInfo(roomId, BLACK_TEAM.asDaoFormat(),
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

        springChessGameDao.updateChessGameInfo(roomId, currentTurnTeamAsString(chessGame), chessGame.isPlaying());

        springChessGameDao.updateTeamInfo(roomId,WHITE_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentWhitePiecePosition()));

        springChessGameDao.updateTeamInfo(roomId, BLACK_TEAM.asDaoFormat(),
                PiecePositionDaoConverter.asDao(chessGame.currentBlackPiecePosition()));

        return ChessGameDto.of(roomId, currentTurnTeamAsString(chessGame), chessGame);
    }

    private ChessGame readChessGameByRoomId(int roomId) {
        final String blackTeamPieceInfo = springChessGameDao.readTeamInfo(roomId, BLACK_TEAM.asDaoFormat());
        final Team blackTeam = generateTeam(blackTeamPieceInfo, BLACK_TEAM.asDaoFormat());

        final String whiteTeamPieceInfo = springChessGameDao.readTeamInfo(roomId, WHITE_TEAM.asDaoFormat());
        final Team whiteTeam = generateTeam(whiteTeamPieceInfo, WHITE_TEAM.asDaoFormat());

        final ChessGameTableDto chessGameTableDto = springChessGameDao.readChessGameInfo(roomId);
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
        springChessGameDao.deleteChessGame(roomId);
    }
}
