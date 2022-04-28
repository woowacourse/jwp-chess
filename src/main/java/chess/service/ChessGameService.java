package chess.service;

import chess.dao.RoomDao;
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
import chess.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessGameService {

    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    public ChessGameService(RoomDao roomDao, PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    public Long createNewRoom(final NewRoomInfo newRoomInfo) {
        validatePassword(newRoomInfo);
        String roomName = newRoomInfo.getName();
        if (roomDao.findRoomIdByName(roomName).isPresent()) {
            throw new IllegalArgumentException("중복된 이름의 방이 존재합니다.");
        }
        final Player whitePlayer = new Player(new WhiteGenerator(), Team.WHITE);
        final Player blackPlayer = new Player(new BlackGenerator(), Team.BLACK);
        final ChessGame chessGame = new ChessGame(whitePlayer, blackPlayer);
        return saveNewRoom(chessGame, newRoomInfo.getPassword(), roomName);
    }

    private void validatePassword(NewRoomInfo newRoomInfo) {
        String password = newRoomInfo.getPassword();
        String confirmPassword = newRoomInfo.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("입력하신 두 비밀번호가 일치하지 않습니다.");
        }
    }

    private long saveNewRoom(final ChessGame chessGame, final String password, final String roomName) {
        roomDao.save(roomName, password, chessGame.getTurn());
        final long roomId = findRoomIdByName(roomName);
        pieceDao.saveAllPieces(chessGame.getCurrentPlayer(), roomId);
        pieceDao.saveAllPieces(chessGame.getOpponentPlayer(), roomId);
        return roomId;
    }

    public List<RoomInfoDto> loadRoomList() {
        return roomDao.findAll();
    }

    public void deleteRoom(final long roomId, final String inputPassword) {
        final ChessGame chessGame = findChessGameById(roomId);
        if (chessGame.isRunning()) {
            throw new IllegalArgumentException("끝나지 않은 게임은 삭제가 불가능합니다.");
        }

        final String roomPassword = roomDao.findRoomPasswordById(roomId);
        if (!inputPassword.equals(roomPassword)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        roomDao.delete(roomId);
    }

    public StatusDto findStatus(final long roomId) {
        final ChessGame chessGame = findChessGameById(roomId);
        final List<GameResult> gameResult = chessGame.findGameResult();
        final GameResult whitePlayerResult = gameResult.get(0);
        final GameResult blackPlayerResult = gameResult.get(1);
        return StatusDto.of(whitePlayerResult, blackPlayerResult);
    }

    public ChessGameDto loadRoom(final long roomId) {
        final GameNameAndTurnDto gameNameAndTurn = roomDao.findNameAndTurnById(roomId);
        List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(roomId, Team.WHITE.getName());
        List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(roomId, Team.BLACK.getName());
        final Player whitePlayer = new Player(toPieces(whitePieces), Team.WHITE);
        final Player blackPlayer = new Player(toPieces(blackPieces), Team.BLACK);
        final Team turn = Team.from(gameNameAndTurn.getTurn());
        ChessGame game = new ChessGame(whitePlayer, blackPlayer, turn);

        return ChessGameDto.of(roomId, game, gameNameAndTurn.getName());
    }

    public ChessGameDto move(final long roomId, final String current, final String destination) {
        final ChessGame chessGame = findChessGameById(roomId);
        final Player currentPlayer = chessGame.getCurrentPlayer();
        final Player opponentPlayer = chessGame.getOpponentPlayer();
        chessGame.move(currentPlayer, opponentPlayer, new Position(current), new Position(destination));
        roomDao.updateTurn(roomId, chessGame.getTurn());
        updatePiece(roomId, current, destination, currentPlayer.getTeamName(),
                opponentPlayer.getTeamName());
        return ChessGameDto.of(roomId, chessGame);
    }

    public void updatePiece(final long roomId, final String current, final String destination,
                            final String currentTeam, final String opponentTeam) {
        pieceDao.deletePieceByRoomIdAndPositionAndTeam(roomId, destination, opponentTeam);
        pieceDao.updatePiecePositionByRoomIdAndTeam(roomId, current, destination, currentTeam);
    }

    private long findRoomIdByName(String name) {
        return roomDao.findRoomIdByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
    }

    private ChessGame findChessGameById(final long roomId) {
        final String turn = roomDao.findTurn(roomId);
        final List<PieceDto> whitePieces = pieceDao.findAllPieceByIdAndTeam(roomId, Team.WHITE.getName());
        final List<PieceDto> blackPieces = pieceDao.findAllPieceByIdAndTeam(roomId, Team.BLACK.getName());

        final Player whitePlayer = new Player(toPieces(whitePieces), Team.WHITE);
        final Player blackPlayer = new Player(toPieces(blackPieces), Team.BLACK);
        final Team gameTurn = Team.from(turn);
        return new ChessGame(whitePlayer, blackPlayer, gameTurn);
    }

    private List<Piece> toPieces(final List<PieceDto> piecesDto) {
        List<Piece> pieces = new ArrayList<>();
        for (PieceDto pieceDto : piecesDto) {
            final Position position = new Position(pieceDto.getPosition());
            pieces.add(State.createPieceByState(pieceDto.getName(), position));
        }
        return pieces;
    }
}
