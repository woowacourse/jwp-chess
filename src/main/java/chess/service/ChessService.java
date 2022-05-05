package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.state.Running;
import chess.domain.state.State;
import chess.domain.state.StateFactory;
import chess.dto.ChessGameDto;
import chess.dto.DeleteRequestDto;
import chess.dto.DeleteResponseDto;
import chess.dto.RoomDto;
import chess.exception.WrongPasswordException;
import chess.repository.GameRepository;
import chess.repository.PieceRepository;
import chess.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class ChessService {

    public static final String FIRST_TURN = "WHITE";
    public static final String PLAY_STATUS = "PLAY";
    public static final String STOP_STATUS = "STOP";
    private final GameRepository gameRepository;
    private final RoomRepository roomRepository;
    private final PieceRepository pieceRepository;

    public ChessService(GameRepository gameRepository, RoomRepository roomRepository, PieceRepository pieceRepository) {
        this.gameRepository = gameRepository;
        this.roomRepository = roomRepository;
        this.pieceRepository = pieceRepository;
    }

    public void createRoom(String roomName, String password) {
        int gameId = gameRepository.saveGameGetKey(FIRST_TURN);
        RoomDto roomDto = new RoomDto(gameId, roomName, password, STOP_STATUS);
        roomRepository.save(roomDto);
        pieceRepository.saveAllPiece(gameId, new BoardInitializer().init());
    }

    public List<RoomDto> loadRooms() {
        return roomRepository.findAll();
    }

    public ChessGameDto newGame(int gameId) {
        initGame(gameId);
        State state = new Running(getColor(gameId), getBoard(gameId));
        ChessGame chessGame = new ChessGame(state);
        return new ChessGameDto(pieceRepository.findAll(gameId), chessGame.status());
    }

    public ChessGameDto loadGame(int gameId) {
        ChessGame chessGame = new ChessGame(StateFactory.of(getColor(gameId),
                getBoard(gameId)));
        return new ChessGameDto(pieceRepository.findAll(gameId), chessGame.status());
    }

    public ChessGameDto move(int gameId, final String from, final String to) {
        ChessGame chessGame = new ChessGame(StateFactory.of(getColor(gameId),
                getBoard(gameId)));
        chessGame.move(Position.from(from), Position.from(to));
        final var nextColor = getColor(gameId).next();
        updateBoard(from, to, gameId, nextColor.name());
        return new ChessGameDto(pieceRepository.findAll(gameId), chessGame.status());
    }

    public void endGame(int gameId) {
        pieceRepository.deleteAllPiece(gameId);
        roomRepository.updateStatus(gameId, STOP_STATUS);
    }

    public DeleteResponseDto deleteGame(DeleteRequestDto deleteRequestDto) {
        String password = roomRepository.findPasswordById(deleteRequestDto.getId());
        if (password.equals(deleteRequestDto.getPassword())) {
            roomRepository.deleteRoom(deleteRequestDto.getId());
            gameRepository.deleteGame(deleteRequestDto.getId());
            return DeleteResponseDto.success();
        }
        throw new WrongPasswordException();
    }

    private void updateBoard(String from, String to, int gameId, String turn) {
        pieceRepository.deletePiece(gameId, to);
        pieceRepository.updatePiecePosition(gameId, from, to);
        gameRepository.update(gameId, turn);
    }

    private void initGame(int gameId) {
        pieceRepository.deleteAllPiece(gameId);
        pieceRepository.saveAllPiece(gameId, new BoardInitializer().init());
        gameRepository.update(gameId, FIRST_TURN);
        roomRepository.updateStatus(gameId, PLAY_STATUS);
    }

    private Board getBoard(int gameId) {
        return pieceRepository.getBoard(gameId);
    }

    private Color getColor(int gameId) {
        return gameRepository.getColor(gameId);
    }
}
