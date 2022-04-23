package chess.service;

import chess.dao.WebChessBoardDao;
import chess.dao.WebChessMemberDao;
import chess.dao.WebChessPieceDao;
import chess.dao.WebChessPositionDao;
import chess.domain.game.ChessBoard;
import chess.domain.game.ConsoleBoard;
import chess.domain.game.Initializer;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.dto.StatusDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GameService {

    @Autowired
    private WebChessBoardDao boardDao;

    @Autowired
    private WebChessPositionDao positionDao;

    @Autowired
    private WebChessPieceDao pieceDao;

    @Autowired
    private WebChessMemberDao memberDao;

    public ChessBoard saveBoard(final ChessBoard board, final Initializer initializer) {
        final ChessBoard savedBoard = boardDao.save(board);
        final Map<Position, Piece> initialize = initializer.initialize();
        positionDao.saveAll(savedBoard.getId());
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.getIdByColumnAndRowAndBoardId(position.getColumn(), position.getRow(),
                    savedBoard.getId());
            final Piece piece = initialize.get(position);
            pieceDao.save(new Piece(piece.getColor(), piece.getType(), lastPositionId));
        }
        memberDao.saveAll(board.getMembers(), savedBoard.getId());
        return savedBoard;
    }

    public void move(final int roomId, final Position sourceRawPosition, final Position targetRawPosition) {
        Position sourcePosition = positionDao.getByColumnAndRowAndBoardId(sourceRawPosition.getColumn(),
                sourceRawPosition.getRow(), roomId);
        Position targetPosition = positionDao.getByColumnAndRowAndBoardId(targetRawPosition.getColumn(),
                targetRawPosition.getRow(), roomId);
        ConsoleBoard consoleBoard = new ConsoleBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        consoleBoard.validateMovement(sourcePosition, targetPosition);
        validateTurn(roomId, sourcePosition);
        updateMovingPiecePosition(sourcePosition, targetPosition, consoleBoard.piece(targetPosition));
        changeTurn(roomId);
    }

    private void validateTurn(final int roomId, final Position sourcePosition) {
        final Optional<Piece> wrappedPiece = pieceDao.findByPositionId(
                positionDao.getIdByColumnAndRowAndBoardId(sourcePosition.getColumn(), sourcePosition.getRow(), roomId));
        wrappedPiece.ifPresent(piece -> validateCorrectTurn(roomId, piece));
    }

    private void validateCorrectTurn(int roomId, Piece piece) {
        final Color turn = boardDao.getById(roomId).getTurn();
        if (!piece.isSameColor(turn)) {
            throw new IllegalArgumentException("지금은 " + turn.value() + "의 턴입니다.");
        }
    }

    private void updateMovingPiecePosition(Position sourcePosition, Position targetPosition,
                                           Optional<Piece> targetPiece) {
        if (targetPiece.isPresent()) {
            pieceDao.deleteByPositionId(targetPosition.getId());
        }
        pieceDao.updatePositionId(sourcePosition.getId(), targetPosition.getId());
    }

    private void changeTurn(int roomId) {
        boardDao.updateTurn(Color.opposite(boardDao.getById(roomId).getTurn()), roomId);
    }

    public BoardDto getBoard(int roomId) {
        final ChessBoard board = boardDao.getById(roomId);
        final Map<Position, Piece> allPositionsAndPieces = positionDao.findAllPositionsAndPieces(roomId);
        Map<String, Piece> pieces = mapPositionToString(allPositionsAndPieces);
        return BoardDto.of(pieces, board.getRoomTitle(), board.getMembers().get(0), board.getMembers().get(1));
    }

    private Map<String, Piece> mapPositionToString(Map<Position, Piece> allPositionsAndPieces) {
        return allPositionsAndPieces.keySet().stream()
                .collect(Collectors.toMap(
                        position -> position.getRow().value() + position.getColumn().name(),
                        allPositionsAndPieces::get));
    }

    public boolean isEnd(int roomId) {
        ConsoleBoard consoleBoard = new ConsoleBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        final boolean kingDead = consoleBoard.isEnd();
        if (kingDead) {
            boardDao.deleteById(roomId);
        }
        return kingDead;
    }

    public StatusDto status(int roomId) {
        return new StatusDto(Arrays.stream(Color.values())
                .collect(Collectors.toMap(Enum::name, color -> calculateScore(roomId, color))));
    }

    public double calculateScore(int roomId, final Color color) {
        ConsoleBoard consoleBoard = new ConsoleBoard(() -> positionDao.findAllPositionsAndPieces(roomId));
        return consoleBoard.calculateScore(color);
    }

    public void end(int roomId) {
        boardDao.deleteById(roomId);
    }

    public RoomsDto getRooms() {
        List<RoomDto> boardsDto = new ArrayList<>();
        List<ChessBoard> boards = boardDao.findAll();
        for (ChessBoard board : boards) {
            boardsDto.add(new RoomDto(board.getId(), board.getRoomTitle(), board.getMembers().get(0),
                    board.getMembers().get(1)));
        }
        return new RoomsDto(boardsDto);
    }
}
