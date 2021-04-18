package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdao.SpringChessGameDao;
import chess.webdto.ChessGameDto;
import chess.webdto.PieceDtoFormat;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;

@Service
public class SpringChessService {
    private final SpringChessGameDao springChessGameDao;

    public SpringChessService(SpringChessGameDao springChessGameDao) {
        this.springChessGameDao = springChessGameDao;
    }

    public ChessGameDto startNewGame() {
        springChessGameDao.deleteChessGame();
        ChessGame chessGame = springChessGameDao.createChessGame();
        return generateChessGameDto(chessGame);
    }

    public ChessGameDto loadSavedGame() {
        final ChessGame chessGame = springChessGameDao.readChessGame();
        return generateChessGameDto(chessGame);
    }

    public ChessGameDto move(final String start, final String destination) {
        final ChessGame chessGame = springChessGameDao.readChessGame();
        chessGame.move(Position.of(start), Position.of(destination));
        springChessGameDao.updateChessGame(chessGame, currentTurnTeamToString(chessGame));
        return generateChessGameDto(chessGame);
    }

    private ChessGameDto generateChessGameDto(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePositionToString = generatePiecePositionToString(chessGame);
        final String currentTurnTeam = currentTurnTeamToString(chessGame);
        final Map<String, Double> teamScore = new HashMap<>();
        teamScore.put(WHITE_TEAM.asDtoFormat(), chessGame.calculateWhiteTeamScore());
        teamScore.put(BLACK_TEAM.asDtoFormat(), chessGame.calculateBlackTeamScore());
        final boolean isPlaying = chessGame.isPlaying();
        return new ChessGameDto(piecePositionToString, currentTurnTeam, teamScore, isPlaying);
    }


    private Map<String, Map<String, String>> generatePiecePositionToString(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePosition = new HashMap<>();
        piecePosition.put(WHITE_TEAM.asDtoFormat(), generatePiecePositionByTeamToString(chessGame.currentWhitePiecePosition()));
        piecePosition.put(BLACK_TEAM.asDtoFormat(), generatePiecePositionByTeamToString(chessGame.currentBlackPiecePosition()));
        return piecePosition;
    }

    private Map<String, String> generatePiecePositionByTeamToString(final Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        for (Position position : piecePosition.keySet()) {
            final String positionInitial = position.getPositionInitial();
            final Piece chosenPiece = piecePosition.get(position);
            final String pieceString = PieceDtoFormat.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceString);
        }
        return piecePositionConverted;
    }

    private String currentTurnTeamToString(final ChessGame chessGame) {
        if (chessGame.isWhiteTeamTurn()) {
            return WHITE_TEAM.asDaoFormat();
        }
        return BLACK_TEAM.asDaoFormat();
    }
}
