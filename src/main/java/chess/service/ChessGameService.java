package chess.service;

import static chess.domain.piece.State.createPieceByState;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.piece.Piece;
import chess.domain.piece.State;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.domain.position.Position;
import chess.dto.ChessGameDto;
import chess.dto.CreateGameDto;
import chess.dto.PieceDto;
import chess.dto.StatusDto;
import java.util.ArrayList;
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

    public ChessGameDto createNewChessGame(final CreateGameDto createGameDto) {
        final String gameName = createGameDto.getChessGameName();
        final String password = createGameDto.getPassword();
        final boolean isDuplicate = chessGameDao.isDuplicateGameName(gameName);
        if (isDuplicate) {
            throw new IllegalArgumentException("중복된 게임 이름입니다.");
        }
        final ChessGame chessGame = initializeChessGame();
        final int chessGameId = chessGameDao.createNewChessGame(chessGame, gameName, password);
        pieceDao.savePieces(chessGame.getCurrentPlayer(), chessGameId);
        pieceDao.savePieces(chessGame.getOpponentPlayer(), chessGameId);
        return ChessGameDto.of(chessGame, gameName);
    }

    private ChessGame initializeChessGame() {
        final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
        final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);
        return new ChessGame(whitePlayer, blackPlayer);
    }

    public StatusDto findStatus(final String gameName) {
        final ChessGame chessGame = findGameByName(gameName);
        final List<GameResult> gameResult = chessGame.findGameResult();
        final GameResult whitePlayerResult = gameResult.get(0);
        final GameResult blackPlayerResult = gameResult.get(1);
        return StatusDto.of(whitePlayerResult, blackPlayerResult);
    }

    public void finishGame(final String gameName) {
        final int gameId = findGameIdByGameName(gameName);
        pieceDao.deletePieces(gameId);
        chessGameDao.deleteChessGame(gameId);
    }

    public ChessGameDto loadChessGame(final String gameName) {
        final ChessGame chessGame = findGameByName(gameName);
        return ChessGameDto.of(chessGame, gameName);
    }

    public ChessGameDto move(final String gameName, final String current, final String destination) {
        final int gameId = findGameIdByGameName(gameName);
        final ChessGame chessGame = findGameByName(gameName);
        final Player currentPlayer = chessGame.getCurrentPlayer();
        final Player opponentPlayer = chessGame.getOpponentPlayer();
        chessGame.move(currentPlayer, opponentPlayer, new Position(current), new Position(destination));
        chessGameDao.updateGameTurn(gameId, chessGame.getTurn());
        pieceDao.updatePiece(gameId, current, destination, currentPlayer.getTeamName(),
                opponentPlayer.getTeamName());
        return ChessGameDto.of(chessGame, gameName);
    }

    private ChessGame findGameByName(final String gameName) {
        final int chessGameId = findGameIdByGameName(gameName);
        final String currentTurn = chessGameDao.findCurrentTurn(chessGameId);
        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(chessGameId, Team.WHITE.getName());
        final List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(chessGameId, Team.BLACK.getName());
        final Player whitePlayer = new Player(toPieces(whitePieces), Team.WHITE);
        final Player blackPlayer = new Player(toPieces(blackPieces), Team.BLACK);

        return new ChessGame(whitePlayer, blackPlayer, Team.from(currentTurn));
    }

    private int findGameIdByGameName(final String gameName) {
        return chessGameDao.findChessGameIdByName(gameName);
    }

    private static List<Piece> toPieces(final List<PieceDto> piecesDto) {
        List<Piece> pieces = new ArrayList<>();
        for (PieceDto pieceDto : piecesDto) {
            final State state = State.from(pieceDto.getName());
            final Position position = new Position(pieceDto.getPosition());
            pieces.add(createPieceByState(state, position));
        }
        return pieces;
    }
}
