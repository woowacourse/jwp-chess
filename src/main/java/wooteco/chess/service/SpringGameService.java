package wooteco.chess.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.db.BoardMapper;
import wooteco.chess.db.entity.BoardEntity;
import wooteco.chess.db.entity.RoomEntity;
import wooteco.chess.db.repository.BoardRepository;
import wooteco.chess.db.repository.PlayerRepository;
import wooteco.chess.db.repository.RoomRepository;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.state.BoardFactory;
import wooteco.chess.domain.state.Playing;
import wooteco.chess.dto.req.MoveRequestDto;
import wooteco.chess.dto.res.GameDto;

@Service
public class SpringGameService {
    private final BoardRepository boardRepository;
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;

    public SpringGameService(BoardRepository boardRepository, RoomRepository roomRepository,
        PlayerRepository playerRepository) {
        this.boardRepository = boardRepository;
        this.roomRepository = roomRepository;
        this.playerRepository = playerRepository;
    }

    public Board createBoard(long roomId) {
        Board board = BoardFactory.create();
        List<BoardEntity> boardEntities = BoardMapper.createMappers(board);
        for (BoardEntity boardEntity : boardEntities) {
            boardEntity.setRoomId(roomId);
            boardRepository.save(boardEntity);
        }
        return board;
    }

    public GameDto load(long roomId) {
        List<BoardEntity> boardEntity = boardRepository.findByRoomId(roomId);
        Board board = BoardMapper.create(boardEntity);
        RoomEntity room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방 번호입니다."));
        Turn turn = Turn.from(playerRepository.findById(room.getTurnId()).get().getTeam());
        ChessGame chessGame = new ChessGame(new Playing(board, turn));
        return new GameDto(board.getBoard(), turn, chessGame.status());
    }

    @Transactional
    public GameDto move(long roomId, MoveRequestDto requestDto) {
        Board board = BoardMapper.create(boardRepository.findByRoomId(roomId));
        RoomEntity room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방 번호입니다."));
        Turn turn = Turn.from(playerRepository.findById(room.getTurnId()).get().getTeam());
        ChessGame game = new ChessGame(new Playing(board, turn));

        Position source = Position.of(requestDto.getSourceX(), requestDto.getSourceY());
        Position target = Position.of(requestDto.getTargetX(), requestDto.getTargetY());
        game.move(source, target);

        Piece sourcePiece = board.getBoard().get(source);
        Piece targetPiece = board.getBoard().get(target);

        boardRepository.updatePiece(roomId, source.getName(), sourcePiece.getTeam().name(),
            sourcePiece.getSymbol());
        boardRepository.updatePiece(roomId, target.getName(), targetPiece.getTeam().name(),
            targetPiece.getSymbol());

        updateTurn(roomId, room);
        return new GameDto(game.board().getBoard(), turn, game.status());
    }

    private void updateTurn(long roomId, RoomEntity room) {
        Long turnId = room.getTurnId();
        if (turnId.equals(room.getPlayer1Id())) {
            turnId = room.getPlayer2Id();
        } else {
            turnId = room.getPlayer1Id();
        }
        roomRepository.updateTurn(roomId, turnId);
    }

    public long createRoom(long player1Id, long player2Id) {
        RoomEntity room = roomRepository.save(new RoomEntity(player1Id, player1Id, player2Id));
        return room.getId();
    }
}
