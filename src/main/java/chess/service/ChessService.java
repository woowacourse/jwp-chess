package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.dao.TurnDao;
import chess.domain.ChessWebGame;
import chess.domain.Position;
import chess.domain.Result;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.MoveDto;
import chess.dto.PieceDto;
import chess.dto.ResultDto;
import chess.dto.RoomDto;
import chess.dto.ScoreDto;
import chess.dto.TurnDto;
import chess.view.ChessMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final TurnDao turnDao;
    private final RoomDao roomDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, TurnDao turnDao, RoomDao roomDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.turnDao = turnDao;
        this.roomDao = roomDao;
        this.gameDao = gameDao;
    }

    public ChessMap initializeGame(int roomId) {
        pieceDao.endPieces(roomId);
        pieceDao.initializePieces(roomId, new Player(new BlackGenerator(), Team.BLACK));
        pieceDao.initializePieces(roomId, new Player(new WhiteGenerator(), Team.WHITE));
        turnDao.initializeTurn(roomId);
        gameDao.startGame(roomId);

        final ChessWebGame chessWebGame = new ChessWebGame();
        return chessWebGame.initializeChessGame();
    }

    public ChessMap load(int roomId) {
        final ChessWebGame chessWebGame = new ChessWebGame();
        loadPieces(roomId, chessWebGame);
        loadTurn(roomId, chessWebGame);

        return chessWebGame.createMap();
    }

    private void loadPieces(int roomId, final ChessWebGame chessWebGame) {
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

    private void loadTurn(final int roomId, final ChessWebGame chessWebGame) {
        final TurnDto turnDto = turnDao.findTurn(roomId);
        Team turn = Team.from(turnDto.getTurn());
        chessWebGame.loadTurn(turn);
    }

    public ChessMap move(final int roomId, final MoveDto moveDto) {
        final Position currentPosition = Position.of(moveDto.getCurrentPosition());
        final Position destinationPosition = Position.of(moveDto.getDestinationPosition());
        final TurnDto turnDto = turnDao.findTurn(roomId);

        final ChessWebGame chessWebGame = loadGame(roomId);
        chessWebGame.move(currentPosition, destinationPosition);
        chessWebGame.changeTurn();
        pieceDao.removePieceByCaptured(roomId, moveDto);
        pieceDao.updatePiece(roomId, moveDto);
        turnDao.updateTurn(roomId, turnDto.getTurn());
        return chessWebGame.createMap();
    }

    private ChessWebGame loadGame(int roomId) {
        final ChessWebGame chessWebGame = new ChessWebGame();
        loadPieces(roomId, chessWebGame);
        loadTurn(roomId, chessWebGame);
        return chessWebGame;
    }

    public ScoreDto getStatus(int roomId) {
        final ChessWebGame chessWebGame = loadGame(roomId);
        final Map<String, Double> scores = chessWebGame.getScoreStatus();
        final Double whiteScore = scores.get(Team.WHITE.getName());
        final Double blackScore = scores.get(Team.BLACK.getName());

        final String status = String.format("%s: %.1f\n%s: %.1f",
                Team.WHITE.getName(), whiteScore, Team.BLACK.getName(), blackScore);
        return new ScoreDto(status);
    }

    public ResultDto getResult(int roomId) {
        final ChessWebGame chessWebGame = loadGame(roomId);
        final Result result = chessWebGame.getResult();
        return new ResultDto(result.getResult());
    }

    public List<RoomDto> getRooms() {
        return roomDao.getRooms();
    }

    public RoomDto createRoom(RoomDto roomDto) {
        roomDao.createRoom(roomDto);
        return new RoomDto(roomDao.getRecentRoomId(), roomDto.getTitle(), roomDto.getPassword());
    }

    public void checkPassword(RoomDto roomDto) {
        if (!roomDao.matchPassword(roomDto.getId(), roomDto.getPassword())) {
            throw new IllegalArgumentException("올바르지 않은 비밀번호 입니다.");
        }
    }

    public void endGame(int roomId) {
        gameDao.endGame(roomId);
    }

    public void deleteRoom(RoomDto roomDto) {
        checkGameState(roomDto.getId());
        roomDao.deleteRoom(roomDto);
    }

    private void checkGameState(int roomId) {
        if (!gameDao.getState(roomId).equals("end")) {
            throw new IllegalArgumentException("종료된 게임만 삭제할 수 있습니다.");
        }
    }
}