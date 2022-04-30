package chess.service;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.TurnDao;
import chess.domain.ChessRoom;
import chess.domain.ChessWebGame;
import chess.domain.Position;
import chess.domain.Result;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.piece.*;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.*;
import chess.view.ChessMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class SpringChessService {

    private final PieceDao pieceDao;
    private final TurnDao turnDao;
    private final RoomDao roomDao;

    public SpringChessService(PieceDao pieceDao, TurnDao turnDao, RoomDao roomDao) {
        this.pieceDao = pieceDao;
        this.turnDao = turnDao;
        this.roomDao = roomDao;
    }

    @Transactional(readOnly = true)
    public List<RoomDto> showRooms() {
        return roomDao.findAll();
    }

    public long makeChessRoom(final RoomRequestDto roomRequestDto) {
        return roomDao.makeRoom(roomRequestDto);
    }

    @Transactional(readOnly = true)
    public ChessMap load(final long roomId) {
        final RoomDto roomDto = roomDao.findRoomById(roomId);
        final ChessWebGame chessWebGame = new ChessWebGame(ChessRoom.from(roomDto));
        loadPieces(roomId, chessWebGame);
        loadTurn(roomId, chessWebGame);

        return chessWebGame.createMap();
    }

    private void loadPieces(final long roomId, final ChessWebGame chessWebGame) {
        final List<PieceDto> whitePiecesDto = pieceDao.findPiecesByTeam(roomId, Team.WHITE);
        final List<PieceDto> blackPiecesDto = pieceDao.findPiecesByTeam(roomId, Team.BLACK);
        final List<Piece> whitePieces = whitePiecesDto.stream()
                .map(this::findPiece)
                .collect(Collectors.toUnmodifiableList());
        final List<Piece> blackPieces = blackPiecesDto.stream()
                .map(this::findPiece)
                .collect(Collectors.toUnmodifiableList());
        chessWebGame.loadPlayers(whitePieces, blackPieces);
    }

    private void loadTurn(final long roomId, final ChessWebGame chessWebGame) {
        final TurnDto turnDto = turnDao.findTurn(roomId);
        Team turn = Team.from(turnDto.getTurn());
        chessWebGame.loadTurn(turn);
    }

    public boolean deleteChessRoom(final RoomDeleteRequestDto roomDeleteRequestDto, final long roomId) {
        RoomDto roomDto = roomDao.findRoomById(roomId);
        if (roomDeleteRequestDto.getPassword().equals(roomDto.getPassword())) {
            roomDao.deleteRoom(roomId);
            return true;
        }
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    public ChessMap initializeGame(final long roomId) {
        pieceDao.endPieces(roomId);
        pieceDao.initializePieces(roomId, new Player(new BlackGenerator(), Team.BLACK));
        pieceDao.initializePieces(roomId, new Player(new WhiteGenerator(), Team.WHITE));
        turnDao.resetTurn(roomId);

        final RoomDto roomDto = roomDao.findRoomById(roomId);
        final ChessWebGame chessWebGame = new ChessWebGame(ChessRoom.from(roomDto));
        return chessWebGame.initializeChessGame();
    }

    private Piece findPiece(final PieceDto pieceDto) {
        final char name = Character.toLowerCase(pieceDto.getName().charAt(0));
        final Position position = Position.of(pieceDto.getPosition());
        final Map<Character, Piece> pieces = new HashMap<>();
        pieces.put('p', new Pawn(position));
        pieces.put('r', new Rook(position));
        pieces.put('n', new Knight(position));
        pieces.put('b', new Bishop(position));
        pieces.put('q', new Queen(position));
        pieces.put('k', new King(position));
        return pieces.get(name);
    }

    public ChessMap move(final long roomId, final MoveDto moveDto) {
        final Position currentPosition = Position.of(moveDto.getCurrentPosition());
        final Position destinationPosition = Position.of(moveDto.getDestinationPosition());
        final TurnDto turnDto = turnDao.findTurn(roomId);

        final ChessWebGame chessWebGame = loadChessWebGame(roomId);
        chessWebGame.move(currentPosition, destinationPosition);
        chessWebGame.changeTurn();
        pieceDao.removePieceByCaptured(roomId, moveDto);
        pieceDao.updatePiece(roomId, moveDto);
        turnDao.updateTurn(roomId, turnDto.getTurn());
        return chessWebGame.createMap();
    }

    @Transactional(readOnly = true)
    public ScoreDto getStatus(final long roomId) {
        final ChessWebGame chessWebGame = loadChessWebGame(roomId);
        final Map<String, Double> scores = chessWebGame.getScoreStatus();
        final Double whiteScore = scores.get(Team.WHITE.getName());
        final Double blackScore = scores.get(Team.BLACK.getName());

        final String status = String.format("%s: %.1f\n%s: %.1f",
                Team.WHITE.getName(), whiteScore, Team.BLACK.getName(), blackScore);
        return new ScoreDto(status);
    }

    @Transactional(readOnly = true)
    public ResultDto getResult(final long roomId) {
        final ChessWebGame chessWebGame = loadChessWebGame(roomId);
        final Result result = chessWebGame.getResult();
        return new ResultDto(result.getResult());
    }

    private ChessWebGame loadChessWebGame(final long roomId) {
        final RoomDto roomDto = roomDao.findRoomById(roomId);
        final ChessWebGame chessWebGame = new ChessWebGame(ChessRoom.from(roomDto));
        loadPieces(roomId, chessWebGame);
        loadTurn(roomId, chessWebGame);
        return chessWebGame;
    }
}
