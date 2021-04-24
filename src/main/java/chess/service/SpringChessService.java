package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.*;
import chess.webdao.SpringChessGameDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public GameRoomListDto loadGameRooms() {
        final List<GameRoomDto> gameRooms = springChessGameDao.loadGameRooms();
        return new GameRoomListDto(gameRooms);
    }

    public GameRoomDto createGameRoom(final String roomName) {
        final int roomId = springChessGameDao.createGameRoom(roomName);
        return new GameRoomDto(roomId, roomName);
    }

    public ChessGameDto startNewGame(final int gameId) {
        ChessGame chessGame = springChessGameDao.createChessGame(gameId);
        return generateChessGameDto(chessGame, gameId);
    }

    public ChessGameDto loadSavedGame(final int gameId) {
        final ChessGame chessGame = springChessGameDao.readChessGame(gameId);
        return generateChessGameDto(chessGame, gameId);
    }

    public ChessGameDto move(final int gameId, final String start, final String destination) {
        final ChessGame chessGame = springChessGameDao.readChessGame(gameId);
        chessGame.move(Position.of(start), Position.of(destination));
        springChessGameDao.updateChessGame(gameId, chessGame, currentTurnTeamAsString(chessGame));
        return generateChessGameDto(chessGame, gameId);
    }

    private ChessGameDto generateChessGameDto(final ChessGame chessGame, final int gameId) {
        final Map<String, Map<String, String>> piecePositionAsString = generatePiecePositionAsString(chessGame);

        final Map<String, Double> teamScore = new HashMap<>();
        teamScore.put(WHITE_TEAM.asDtoFormat(), chessGame.calculateWhiteTeamScore());
        teamScore.put(BLACK_TEAM.asDtoFormat(), chessGame.calculateBlackTeamScore());

        return new ChessGameDto(piecePositionAsString, currentTurnTeamAsString(chessGame), teamScore, chessGame.isPlaying(), gameId);
    }

    private Map<String, Map<String, String>> generatePiecePositionAsString(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePosition = new HashMap<>();
        piecePosition.put(WHITE_TEAM.asDtoFormat(), generateTeamPiecePositionAsString(chessGame.currentWhitePiecePosition()));
        piecePosition.put(BLACK_TEAM.asDtoFormat(), generateTeamPiecePositionAsString(chessGame.currentBlackPiecePosition()));
        return piecePosition;
    }

    private Map<String, String> generateTeamPiecePositionAsString(final Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        piecePosition.forEach((position, chosenPiece) -> {
            final String positionInitial = position.getPositionInitial();
            final String pieceAsString = PieceDtoFormat.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceAsString);
        });
        return piecePositionConverted;
    }

    private String currentTurnTeamAsString(final ChessGame chessGame) {
        if (chessGame.isWhiteTeamTurn()) {
            return WHITE_TEAM.asDaoFormat();
        }
        return BLACK_TEAM.asDaoFormat();
    }
}
