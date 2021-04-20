package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdao.ChessDao;
import chess.webdao.MysqlChessDao;
import chess.webdto.ChessGameDto;
import chess.webdto.PieceDto;
import chess.webdto.ScoreDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static chess.webdto.TeamDto.BLACK_TEAM;
import static chess.webdto.TeamDto.WHITE_TEAM;

@Service
public class SpringChessService {
    private final ChessDao chessDao;

    public SpringChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public ChessGameDto startNewGame() {
        chessDao.deleteChessGame();
        ChessGame chessGame = chessDao.createChessGame();

        return generateChessGameDto(chessGame);
    }

    public ChessGameDto loadPreviousGame() {
        final ChessGame chessGame = chessDao.readChessGame();

        return generateChessGameDto(chessGame);
    }

    public ChessGameDto move(final String start, final String destination) {
        final ChessGame chessGame = chessDao.readChessGame();

        chessGame.move(Position.of(start), Position.of(destination));
        chessDao.updateChessGame(chessGame, currentTurnTeamToString(chessGame));

        return generateChessGameDto(chessGame);
    }


    private ChessGameDto generateChessGameDto(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePositionToString
                = generatePiecePositionToString(chessGame);
        final String currentTurnTeam = currentTurnTeamToString(chessGame);
        final ScoreDto teamScore = new ScoreDto(chessGame.calculateWhiteTeamScore(), chessGame.calculateBlackTeamScore());

        final boolean isPlaying = chessGame.isPlaying();

        return new ChessGameDto(piecePositionToString, currentTurnTeam, teamScore, isPlaying);
    }

    private Map<String, Map<String, String>> generatePiecePositionToString(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePosition = new HashMap<>();
        piecePosition.put(WHITE_TEAM.team(), generatePiecePositionByTeamToString(chessGame.currentWhitePiecePosition()));
        piecePosition.put(BLACK_TEAM.team(), generatePiecePositionByTeamToString(chessGame.currentBlackPiecePosition()));
        return piecePosition;
    }

    private Map<String, String> generatePiecePositionByTeamToString(final Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        for (Position position : piecePosition.keySet()) {
            final String positionInitial = position.getPositionInitial();
            final Piece chosenPiece = piecePosition.get(position);
            final String pieceString = PieceDto.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceString);
        }
        return piecePositionConverted;
    }

    private String currentTurnTeamToString(final ChessGame chessGame) {
        if (chessGame.isWhiteTeamTurn()) {
            return WHITE_TEAM.team();
        }
        return BLACK_TEAM.team();
    }
}
