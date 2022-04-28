package chess.service;

import chess.entity.GameEntity;
import chess.entity.PieceEntity;
import chess.model.GameResult;
import chess.model.MoveType;
import chess.model.Turn;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.PieceDao;
import chess.model.dao.GameDao;
import chess.model.dto.*;
import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public WebBoardDto start(RoomDto roomDto) {
        long gameId = makeRoom(roomDto);
        makeBoard(gameId);

        return getBoardByGameId(gameId);
    }

    public WebBoardDto getBoardByGameId(long gameId) {
        return WebBoardDto.from(toBoard(pieceDao.findAllByGameId(gameId)));
    }

    public GameInfosDto getAllGames() {
        List<GameEntity> games = gameDao.findAll();
        return new GameInfosDto(games.stream()
                .map(GameInfoDto::from)
                .collect(Collectors.toList()));
    }

    public WebBoardDto move(MoveDto moveDto, Long id) {
        Piece sourcePiece = PieceFactory.create(pieceDao.findPieceNameByPositionAndGameId(moveDto.getSource(), id));
        Piece targetPiece = PieceFactory.create(pieceDao.findPieceNameByPositionAndGameId(moveDto.getTarget(), id));
        Turn turn = Turn.from(gameDao.findTurnById(id));
        validateCurrentTurn(turn, sourcePiece);
        try {
            movePiece(moveDto, sourcePiece, targetPiece, turn, id);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Board board = toBoard(pieceDao.findAllByGameId(id));
        if (board.isKingDead()) {
            gameDao.update(turn.finish(), id);
        }

        return WebBoardDto.from(board);
    }

    private void movePiece(MoveDto moveDto, Piece sourcePiece, Piece targetPiece, Turn turn, Long id) {
        if (canMove(moveDto, sourcePiece, targetPiece, id)) {
            pieceDao.updateByPosition(moveDto.getTarget(), PieceDao.getPieceName(sourcePiece));
            pieceDao.updateByPosition(moveDto.getSource(), "none-.");
            gameDao.update(turn.change().getThisTurn(), id);
            return;
        }
        throw new IllegalArgumentException("기물을 이동할 수 없습니다.");
    }

    public String getTurn(Long id) {
        return gameDao.findTurnById(id);
    }

    public boolean isKingDead(Long id) {
        Board board = toBoard(pieceDao.findAllByGameId(id));
        return board.isKingDead();
    }

    public GameResult getResult(Long id) {
        Board board = toBoard(pieceDao.findAllByGameId(id));
        return GameResult.from(board);
    }

    public void deleteByGameId(String confirmPassword, Long id) {
        String storedPassword = gameDao.findPwdById(id);
        Turn turn = Turn.from(gameDao.findTurnById(id));

        if (turn.isNotEnd()) {
            throw new IllegalArgumentException("진행 중인 게임은 삭제할 수 없습니다.");
        }

        if (confirmPassword.equals(storedPassword)) {
            // 생각해볼 것: 두개가 참조되어 있을 때 뭘 먼저 실행해야할지?
            pieceDao.deleteByGameId(id);
            gameDao.deleteByGameId(id);
            return;
        }
        throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }

    private long makeRoom(RoomDto roomDto) {
        return gameDao.initGame(roomDto.getRoomName(), roomDto.getPassword());
    }

    private void makeBoard(Long gameId) {
        pieceDao.init(BoardFactory.create(), gameId);
    }

    private Board toBoard(List<PieceEntity> rawBoard) {

        return new Board(rawBoard.stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> PieceFactory.create(piece.getName()))
                ));
    }

    private void validateCurrentTurn(Turn thisTurn, Piece sourcePiece) {
        if (!sourcePiece.isCurrentTurn(thisTurn)) {
            throw new IllegalArgumentException("본인의 말을 움직여야 합니다.");
        }
    }

    private boolean canMove(MoveDto moveDto, Piece sourcePiece, Piece targetPiece, Long id) {
        Position sourcePosition = Position.from(moveDto.getSource());
        Position targetPosition = Position.from(moveDto.getTarget());
        MoveType moveType = MoveType.of(sourcePiece, targetPiece);

        return sourcePiece.isMovable(sourcePosition, targetPosition, moveType)
                && !hasBlock(sourcePosition, targetPosition, sourcePiece, id);
    }

    private boolean hasBlock(Position source, Position target, Piece sourcePiece, Long id) {
        Board board = toBoard(pieceDao.findAllByGameId(id));
        List<Position> positions = sourcePiece.getIntervalPosition(source, target);
        return positions.stream()
                .anyMatch(position -> !board.get(position).equals(new Empty()));
    }
}
