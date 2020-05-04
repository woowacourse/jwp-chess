package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.db.*;
import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.board.BoardFactory;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.piece.PieceColor;
import wooteco.chess.domains.position.Position;
import wooteco.chess.dto.GameResponseDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// Todo: Move시 자신의 말 위치로 이동하면 IllgalStateException -> 체스 게임 커스텀 exception 생성하고 처리해주기~
@Service
public class SpringChessService {
    public static final int BOARD_CELLS_NUMBER = 64;
    private static final String CAN_NOT_RESUME_ERR_MSG = "해당 게임을 이어할 수 없습니다.";
    private static final String CAN_NOT_FIND_GAME_ROOM_ERR_MSG = "해당 게임을 찾을 수 없습니다.";
    private static final String TURN_MSG_FORMAT = "%s의 순서입니다.";
    private static final String WINNING_MSG_FORMAT = "%s이/가 이겼습니다.";

    private final RoomRepository roomRepository;
    private final ChessPieceRepository chessPieceRepository;
    private final MoveHistoryRepository moveHistoryRepository;

    public SpringChessService(RoomRepository roomRepository, ChessPieceRepository chessPieceRepository,
                              MoveHistoryRepository moveHistoryRepository) {
        this.roomRepository = roomRepository;
        this.chessPieceRepository = chessPieceRepository;
        this.moveHistoryRepository = moveHistoryRepository;
    }

    public List<Room> findRooms() {
        return roomRepository.findAll();
    }

    public GameResponseDto startNewGame(String roomName) {
        Room room = new Room(roomName);
        Board board = new Board();

        Set<ChessPiece> chessPieces = Position.stream()
                .map(position -> new ChessPiece(position.name(), board.getPieceByPosition(position).name()))
                .collect(Collectors.toSet());

        room.addChessPieces(chessPieces);
        Room savedRoom = roomRepository.save(room);

        return createGameResponseDto(savedRoom.getId(), board);
    }

    public GameResponseDto resumeGame(Long roomId) {
        int savedPiecesNumber = chessPieceRepository.countSavedPiecesByRoomId(roomId);

        if (savedPiecesNumber != BOARD_CELLS_NUMBER) {
            throw new IllegalArgumentException(CAN_NOT_RESUME_ERR_MSG);
        }

        Board savedBoard = createSavedBoard(roomId);

        return createGameResponseDto(roomId, savedBoard);
    }

    // Todo: room으로 save?
    public GameResponseDto move(Long roomId, MoveHistory moveHistory) {
        Board board = createSavedBoard(roomId);
        PieceColor currentTeam = board.getTeamColor();

        Position source = Position.ofPositionName(moveHistory.getSourcePosition());
        Position target = Position.ofPositionName(moveHistory.getTargetPosition());

        board.move(source, target);

        chessPieceRepository.updatePiece(roomId, source.name(), board.getPieceByPosition(source).name());
        chessPieceRepository.updatePiece(roomId, target.name(), board.getPieceByPosition(target).name());
        moveHistoryRepository.addMoveHistory(roomId, currentTeam.name(), source.name(), target.name());

        if (board.isGameOver()) {
            roomRepository.deleteById(roomId);
        }

        return createGameResponseDto(roomId, board);
    }

    // Todo: piece을 symbol로 저장? -> convertPieces 삭제, recoverBoard 메서드 사용하지 않고, 보드 생성하지 않고 진행하게

    private Board createSavedBoard(Long roomId) {
        Board board = new Board();
        List<ChessPiece> chessPieces = chessPieceRepository.findByRoomId(roomId);
        Optional<String> lastTurn = moveHistoryRepository.findLastTurn(roomId);

        Map<Position, Piece> savedBoard = chessPieces.stream()
                .collect(Collectors.toMap(
                        piece -> Position.ofPositionName(piece.getPosition()),
                        piece -> BoardFactory.findPieceByPieceName(piece.getPiece())
                ));
        board.recoverBoard(savedBoard, lastTurn);
        return board;
    }

    private GameResponseDto createGameResponseDto(Long roomId, Board board) {
        List<String> pieces = convertPieces(board);

        String turn = turnMsg(board);

        double whiteScore = board.calculateScore(PieceColor.WHITE);
        double blackScore = board.calculateScore(PieceColor.BLACK);

        String end = null;
        if (board.isGameOver()) {
            end = winningMsg(board);
        }

        return new GameResponseDto(roomId, pieces, turn, whiteScore, blackScore, end);
    }


    private List<String> convertPieces(Board board) {
        return board.showBoard()
                .stream()
                .map(Piece::symbol)
                .collect(Collectors.toList());
    }

    private String turnMsg(Board board) {
        PieceColor team = board.getTeamColor();
        return String.format(TURN_MSG_FORMAT, team.name());
    }

    private String winningMsg(Board board) {
        PieceColor winner = board.getTeamColor().changeTeam();
        return String.format(WINNING_MSG_FORMAT, winner);
    }
}
