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
        springChessGameDao.updateChessGame(chessGame, currentTurnTeamAsString(chessGame));
        return generateChessGameDto(chessGame);
    }

    private ChessGameDto generateChessGameDto(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePositionAsString = generatePiecePositionAsString(chessGame);

        final Map<String, Double> teamScore = new HashMap<>();
        teamScore.put(WHITE_TEAM.asDtoFormat(), chessGame.calculateWhiteTeamScore());
        teamScore.put(BLACK_TEAM.asDtoFormat(), chessGame.calculateBlackTeamScore());

        return new ChessGameDto(piecePositionAsString, currentTurnTeamAsString(chessGame), teamScore, chessGame.isPlaying());
    }

    private Map<String, Map<String, String>> generatePiecePositionAsString(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePosition = new HashMap<>();
        final Map<Position, Piece> whitePiecePosition = chessGame.currentWhitePiecePosition();
        piecePosition.put(WHITE_TEAM.asDtoFormat(), generateTeamPiecePositionAsString(whitePiecePosition));
        final Map<Position, Piece> blackPiecePosition = chessGame.currentBlackPiecePosition();
        piecePosition.put(BLACK_TEAM.asDtoFormat(), generateTeamPiecePositionAsString(blackPiecePosition));
        return piecePosition;
    }

    private Map<String, String> generateTeamPiecePositionAsString(final Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        for (Position position : piecePosition.keySet()) {
            final String positionInitial = position.getPositionInitial();
            final Piece chosenPiece = piecePosition.get(position);
            final String pieceString = PieceDtoFormat.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceString);
        }
        return piecePositionConverted;
    }

    private String currentTurnTeamAsString(final ChessGame chessGame) {
        if (chessGame.isWhiteTeamTurn()) {
            return WHITE_TEAM.asDaoFormat();
        }
        return BLACK_TEAM.asDaoFormat();
    }
}
