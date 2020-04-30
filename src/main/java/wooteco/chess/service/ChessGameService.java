package wooteco.chess.service;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import chess.dto.TurnDto;
import org.springframework.stereotype.Service;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.entity.TurnEntity;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.repository.TurnRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ChessGameService {
    private RoomRepository roomRepository;
    private BoardRepository boardRepository;
    private TurnRepository turnRepository;

    public ChessGameService(RoomRepository roomRepository, BoardRepository boardRepository,
                            TurnRepository turnRepository) {
        this.roomRepository = roomRepository;
        this.boardRepository = boardRepository;
        this.turnRepository = turnRepository;
    }

    public RoomEntity createRoom(String name) {
        return roomRepository.save(new RoomEntity(name));
    }

    public List<RoomEntity> loadRooms() {
        return roomRepository.findAll();
    }

    public ChessBoard loadBoard() {
        return createBoardFromDb();
    }

    public ChessBoard createNewChessGame(Long roomId) {
        ChessBoard chessBoard = new ChessBoard();
        Map<Position, Piece> board = chessBoard.getBoard();
        Optional<RoomEntity> maybeRoom = roomRepository.findById(roomId);
        RoomEntity emptyRoomEntity = maybeRoom.orElseThrow(() -> new IllegalArgumentException(String.format("방 번호가 %d인 방이 " +
                                                                                                                    "없습니다" +
                                                                                                                    ".",
                                                                                                            roomId)));

        for (Position position : board.keySet()) {
            emptyRoomEntity.addBoard(new BoardEntity(position.getName(), board.get(position).getName()));
        }
        String currentTeam = TurnDto.from(chessBoard).getCurrentTeam();
        RoomEntity roomEntity = new RoomEntity(emptyRoomEntity, new TurnEntity(currentTeam));
        roomRepository.save(roomEntity);

        return chessBoard;
    }

    public ChessBoard movePiece(String source, String target) {
        ChessBoard chessBoard = createBoardFromDb();
        Map<Position, Piece> board = chessBoard.getBoard();
        chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

        this.boardRepository.deleteAll();
        for (Position position : board.keySet()) {
            this.boardRepository.save(
                    new BoardEntity(position.getName(), board.get(position).getName())
            );
        }

        TurnEntity first = this.turnRepository.findFirst();

        this.turnRepository.save(
                new TurnEntity(
                        first.getId(), TurnDto.from(chessBoard).getCurrentTeam()
                )
        );

        return chessBoard;
    }

    private void endGame() {
        this.boardRepository.deleteAll();
        this.turnRepository.deleteAll();
    }

    public GameResult findWinner() {
        ChessBoard chessBoard = createBoardFromDb();
        GameResult gameResult = chessBoard.createGameResult();
        endGame();
        return gameResult;
    }

    public boolean isGameOver() {
        ChessBoard chessBoard = createBoardFromDb();
        return chessBoard.isGameOver();
    }

    public boolean isNotGameOver() {
        return !isGameOver();
    }

    private ChessBoard createBoardFromDb() {
        List<BoardEntity> boardEntities = this.boardRepository.findAll();
        if (boardEntities.isEmpty()) {
            throw new NoSuchElementException("테이블의 행이 비었습니다!!");
        }
        BoardDto boardDto = new BoardDto(boardEntities);

        TurnDto turnDto = TurnDto.from(this.turnRepository.findFirst().getTeamName());

        return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
    }
}
