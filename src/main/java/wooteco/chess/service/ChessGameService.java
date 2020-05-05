package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.db.ChessPiece;
import wooteco.chess.db.ChessPieceRepository;
import wooteco.chess.db.MoveHistory;
import wooteco.chess.db.MoveHistoryRepository;
import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.board.BoardFactory;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.piece.PieceColor;
import wooteco.chess.domains.position.Position;
import wooteco.chess.dto.GameResponseDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Todo: Move시 자신의 말 위치로 이동하면 IllgalStateException -> 체스 게임 커스텀 exception 생성하고 처리해주기~
@Service
public class ChessGameService {
    public static final int BOARD_CELLS_NUMBER = 64;
    private static final String CAN_NOT_RESUME_ERR_MSG = "해당 게임을 이어할 수 없습니다.";

    private final ChessPieceRepository chessPieceRepository;
    private final MoveHistoryRepository moveHistoryRepository;

    public ChessGameService(ChessPieceRepository chessPieceRepository, MoveHistoryRepository moveHistoryRepository) {
        this.chessPieceRepository = chessPieceRepository;
        this.moveHistoryRepository = moveHistoryRepository;
    }

    public GameResponseDto resumeGame(Long roomId) {
        int savedPiecesNumber = chessPieceRepository.countSavedPiecesByRoomId(roomId);

        if (savedPiecesNumber != BOARD_CELLS_NUMBER) {
            throw new IllegalArgumentException(CAN_NOT_RESUME_ERR_MSG);
        }

        Board savedBoard = createSavedBoard(roomId);

        return new GameResponseDto(roomId, savedBoard);
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

        return new GameResponseDto(roomId, board);
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
}
