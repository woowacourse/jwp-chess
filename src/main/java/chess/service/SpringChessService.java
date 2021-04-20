package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.CapturedPieces;
import chess.domain.team.PiecePositions;
import chess.domain.team.Score;
import chess.domain.team.Team;
import chess.webdao.ChessDao;
import chess.webdao.PiecePositionDaoConverter;
import chess.webdto.*;
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

        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        chessDao.createChessGame(chessGame.isPlaying());
        chessDao.createTeamInfo(WHITE_TEAM.team(), chessGame.currentWhitePiecePosition());
        chessDao.createTeamInfo(BLACK_TEAM.team(), chessGame.currentBlackPiecePosition());

        return generateChessGameDto(chessGame);
    }

    public ChessGameDto loadPreviousGame() {
        ChessGame chessGame = generateChessGame();
        return generateChessGameDto(chessGame);
    }

    private ChessGame generateChessGame() {
        final String blackPieces = chessDao.readTeamInfo(BLACK_TEAM.team());
        final String whitePieces = chessDao.readTeamInfo(WHITE_TEAM.team());
        final Team blackTeam = generateTeam(blackPieces, BLACK_TEAM.team());
        final Team whiteTeam = generateTeam(whitePieces, WHITE_TEAM.team());
        TurnDto turnDto = chessDao.readTurn();

        ChessGame chessGame = generateChessGameAccordingToDB(blackTeam, whiteTeam, turnDto.getCurrentTurnTeam(), turnDto.getIsPlaying());
        return chessGame;
    }

    private ChessGame generateChessGameAccordingToDB(final Team blackTeam, final Team whiteTeam,
                                                     final String currentTurnTeam, final boolean isPlaying) {
        if (WHITE_TEAM.team().equals(currentTurnTeam)) {
            return new ChessGame(blackTeam, whiteTeam, whiteTeam, isPlaying);
        }
        return new ChessGame(blackTeam, whiteTeam, blackTeam, isPlaying);
    }

    private Team generateTeam(final String teamPieceInfo, final String team) {
        Map<Position, Piece> piecePosition;
        piecePosition = PiecePositionDaoConverter.asPiecePosition(teamPieceInfo, team);
        final PiecePositions piecePositionsByTeam = new PiecePositions(piecePosition);
        return new Team(piecePositionsByTeam, new CapturedPieces(), new Score());
    }

    public ChessGameDto move(final String start, final String destination) {
        final ChessGame chessGame = generateChessGame();
        chessGame.move(Position.of(start), Position.of(destination));
        chessDao.updateTeamInfo(chessGame.currentWhitePiecePosition(), WHITE_TEAM.team());
        chessDao.updateTeamInfo(chessGame.currentBlackPiecePosition(), BLACK_TEAM.team());
        chessDao.updateChessGame(chessGame, currentTurnTeamToString(chessGame));

        return generateChessGameDto(chessGame);
    }


    private ChessGameDto generateChessGameDto(final ChessGame chessGame) {
        final TeamPiecesDto piecePositionToString
                = new TeamPiecesDto(
                generatePiecePositionByTeamToString(chessGame.currentWhitePiecePosition()),
                generatePiecePositionByTeamToString(chessGame.currentBlackPiecePosition())
        );
        final String currentTurnTeam = currentTurnTeamToString(chessGame);
        final ScoreDto teamScore = new ScoreDto(chessGame.calculateWhiteTeamScore(), chessGame.calculateBlackTeamScore());
        final boolean isPlaying = chessGame.isPlaying();

        return new ChessGameDto(piecePositionToString, currentTurnTeam, teamScore, isPlaying);
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
