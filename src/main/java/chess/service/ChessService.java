package chess.service;

import chess.domain.ChessGame;
import chess.domain.Score;
import chess.domain.chessboard.ChessBoard;
import chess.domain.position.Position;
import chess.domain.result.EndResult;
import chess.domain.room.Room;
import chess.dto.ChessPieceMapper;
import chess.dto.request.MoveRequestDto;
import chess.dto.response.ChessPieceDto;
import chess.repository.ChessGameRepository;
import chess.repository.RoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessGameRepository chessGameRepository;
    private final RoomRepository roomRepository;

    public ChessService(final ChessGameRepository chessGameRepository, final RoomRepository roomRepository) {
        this.chessGameRepository = chessGameRepository;
        this.roomRepository = roomRepository;
    }

    public List<ChessPieceDto> findAllPiece(final int roomId) {
        final ChessGame chessGame = chessGameRepository.get(roomId);
        final ChessBoard chessBoard = chessGame.getChessBoard();
        return chessBoard.findAllPiece()
                .entrySet()
                .stream()
                .map(it -> ChessPieceDto.of(
                        it.getKey(),
                        ChessPieceMapper.toPieceType(it.getValue()),
                        it.getValue().color()))
                .collect(Collectors.toList());
    }

    public void move(final int roomId, MoveRequestDto requestDto) {
        final Room room = roomRepository.get(roomId);
        final ChessGame chessGame = room.getChessGame();
        final Position from = Position.from(requestDto.getFrom());
        final Position to = Position.from(requestDto.getTo());

        chessGame.move(from, to);

        roomRepository.update(roomId, room);
        chessGameRepository.update(roomId, from, to);
    }

    public Score findScore(final int roomId) {
        final ChessGame chessGame = chessGameRepository.get(roomId);

        return chessGame.calculateScore();
    }

    public EndResult result(final int roomId) {
        final Room room = roomRepository.get(roomId);
        final ChessGame chessGame = room.getChessGame();

        final EndResult result = chessGame.end();
        roomRepository.update(roomId, room);

        return result;
    }
}
