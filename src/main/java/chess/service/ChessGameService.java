package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.domain.position.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessGameUpdateDto;
import chess.dto.PieceDto;
import chess.dto.StatusDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessGameService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public ChessGameDto createNewChessGame(final String gameName) {
        final boolean isDuplicate = chessGameDao.isDuplicateGameName(gameName);
        if (isDuplicate) {
            throw new IllegalArgumentException("중복된 게임 이름입니다.");
        }
        final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
        final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);
        final ChessGame chessGame = new ChessGame(whitePlayer, blackPlayer);
        saveNewChessGame(chessGame, gameName);
        return ChessGameDto.of(chessGame, gameName);
    }

    private void saveNewChessGame(final ChessGame chessGame, final String gameName) {
        chessGameDao.saveChessGame(gameName, chessGame.getTurn());
        final int chessGameId = chessGameDao.findChessGameIdByName(gameName);
        pieceDao.savePieces(chessGame.getCurrentPlayer(), chessGameId);
        pieceDao.savePieces(chessGame.getOpponentPlayer(), chessGameId);
    }

    public StatusDto findStatus(final String gameName) {
        final ChessGame chessGame = findGameByName(gameName);
        final List<GameResult> gameResult = chessGame.findGameResult();
        final GameResult whitePlayerResult = gameResult.get(0);
        final GameResult blackPlayerResult = gameResult.get(1);
        return StatusDto.of(whitePlayerResult, blackPlayerResult);
    }

    public StatusDto finishGame(final String gameName) {
        final StatusDto status = findStatus(gameName);
        final int gameId = chessGameDao.findChessGameIdByName(gameName);
        pieceDao.deletePieces(gameId);
        chessGameDao.deleteChessGame(gameId);
        return status;
    }

    public ChessGameDto loadChessGame(final String gameName) {
        final ChessGame chessGame = findGameByName(gameName);
        return ChessGameDto.of(chessGame, gameName);
    }

    public ChessGameDto move(final String gameName, final String current, final String destination) {
        final int gameId = chessGameDao.findChessGameIdByName(gameName);
        final ChessGame chessGame = findGameByName(gameName);
        final Player currentPlayer = chessGame.getCurrentPlayer();
        final Player opponentPlayer = chessGame.getOpponentPlayer();
        chessGame.move(currentPlayer, opponentPlayer, new Position(current), new Position(destination));
        chessGameDao.updateGameTurn(gameId, chessGame.getTurn());
        updatePiece(gameId, current, destination, currentPlayer.getTeamName(),
                opponentPlayer.getTeamName());
        return ChessGameDto.of(chessGame, gameName);
    }

    public void updatePiece(final int gameId, final String current, final String destination,
                            final String currentTeam, final String opponentTeam) {
        pieceDao.deletePieceByGameIdAndPositionAndTeam(gameId, destination, opponentTeam);
        pieceDao.updatePiecePositionByGameId(gameId, current, destination, currentTeam);
    }

    private ChessGame findGameByName(final String gameName) {
        final int chessGameId = chessGameDao.findChessGameIdByName(gameName);
        final ChessGameUpdateDto gameUpdateDto = findChessGame(chessGameId);
        return ChessGame.of(gameUpdateDto);
    }

    public ChessGameUpdateDto findChessGame(final int chessGameId) {
        final String turn = chessGameDao.findCurrentTurn(chessGameId);
        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(chessGameId, Team.WHITE.getName());
        final List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(chessGameId, Team.BLACK.getName());
        return new ChessGameUpdateDto(turn, whitePieces, blackPieces);
    }
}
