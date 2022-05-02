package chess.service;

import static chess.domain.piece.State.createPieceByState;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.ChessMap;
import chess.domain.GameResult;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.piece.Piece;
import chess.domain.piece.State;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import chess.dto.request.CreateGameDto;
import chess.dto.request.DeleteGameDto;
import chess.dto.response.ChessGameDto;
import chess.dto.response.ChessGameStatusDto;
import chess.dto.response.PlayerScoreDto;
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

    public ChessGameStatusDto createNewChessGame(final CreateGameDto createGameDto) {
        validatePassword(createGameDto.getPassword(), createGameDto.getPasswordCheck());
        validateDuplicateName(createGameDto.getChessGameName());

        final String gameName = createGameDto.getChessGameName();
        final String password = createGameDto.getPassword();
        return saveChessGame(gameName, password);
    }

    private void validatePassword(final String password, final String passwordCheck) {
        boolean isSame = password.equals(passwordCheck);
        if (!isSame) {
            throw new IllegalArgumentException("입력한 비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateDuplicateName(final String gameName) {
        final int count = chessGameDao.findChessGameCountByName(gameName);
        if (count > 0) {
            throw new IllegalArgumentException("중복된 게임 이름입니다.");
        }
    }

    private ChessGameStatusDto saveChessGame(String gameName, String password) {
        final ChessGame chessGame = initializeChessGame();

        chessGameDao.saveChessGame(gameName, password, chessGame.getTurn().getName());
        final int gameId = chessGameDao.findChessGameIdByName(gameName);
        pieceDao.savePieces(chessGame.getCurrentPlayer(), gameId);
        pieceDao.savePieces(chessGame.getOpponentPlayer(), gameId);
        return new ChessGameStatusDto(gameId, gameName, chessGame.getTurn().getName());
    }

    private ChessGame initializeChessGame() {
        final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
        final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);
        return new ChessGame(whitePlayer, blackPlayer);
    }

    public void deleteGame(final DeleteGameDto deleteGameDto) {
        int gameId = deleteGameDto.getId();
        validateDeleteGame(gameId, deleteGameDto.getPassword());
        pieceDao.deletePieces(gameId);
        chessGameDao.deleteChessGame(gameId);
    }

    private void validateDeleteGame(final int gameId, final String password) {
        boolean isRunning = chessGameDao.findChessGame(gameId).isRunning();
        if (isRunning) {
            throw new IllegalArgumentException("진행 중인 게임은 삭제할 수 없습니다.");
        }

        final int passwordsMatchCount = chessGameDao.findChessGameByIdAndPassword(gameId, password);
        final boolean isWrongPassword = passwordsMatchCount == 0;
        if (isWrongPassword) {
            throw new IllegalArgumentException("암호가 다릅니다.");
        }
    }

    public ChessMap loadChessMap(final int gameId) {
        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, Team.WHITE.getName());
        final List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(gameId, Team.BLACK.getName());
        return ChessMap.of(toPieces(whitePieces), toPieces(blackPieces));
    }

    public ChessGameDto move(final int gameId, final String gameName, final String current, final String destination) {
        final ChessGame chessGame = findChessGame(gameId);
        final Player currentPlayer = chessGame.getCurrentPlayer();
        final Player opponentPlayer = chessGame.getOpponentPlayer();
        chessGame.move(currentPlayer, opponentPlayer, new Position(current), new Position(destination));
        chessGameDao.updateGameTurn(gameId, chessGame.getTurn());
        pieceDao.updatePiece(gameId, current, destination, currentPlayer.getTeamName(),
                opponentPlayer.getTeamName());
        return ChessGameDto.of(chessGame, gameName);
    }

    public ChessGame findChessGame(final int gameId) {
        final String currentTurn = chessGameDao.findCurrentTurn(gameId);
        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(gameId, Team.WHITE.getName());
        final List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(gameId, Team.BLACK.getName());
        final Player whitePlayer = new Player(toPieces(whitePieces), Team.WHITE);
        final Player blackPlayer = new Player(toPieces(blackPieces), Team.BLACK);

        return new ChessGame(whitePlayer, blackPlayer, Team.from(currentTurn));
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

    public PlayerScoreDto findStatus(final int gameId) {
        final ChessGame chessGame = findChessGame(gameId);
        final List<GameResult> gameResult = chessGame.findGameResult();
        final GameResult whitePlayerResult = gameResult.get(0);
        final GameResult blackPlayerResult = gameResult.get(1);
        return PlayerScoreDto.of(whitePlayerResult, blackPlayerResult);
    }

    public List<ChessGameStatusDto> findAllChessGame() {
        return chessGameDao.findAllChessGame();
    }

    public ChessGameStatusDto findGameInfoById(final int gameId) {
        return chessGameDao.findChessGame(gameId);
    }
}
