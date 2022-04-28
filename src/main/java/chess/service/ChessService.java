package chess.service;

import chess.entity.PieceEntity;
import chess.model.GameResult;
import chess.model.MoveType;
import chess.model.Turn;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public Map<Long, String> getAllGames() {
        Map<Long, String> games = new LinkedHashMap<>();
        for (Long gameId : gameDao.findAllGameId()) {
            games.put(gameId, gameDao.findTitleByGameId(gameId));
        }

        return games;
    }

    public Long createGame(String title, String password) {
        Long gameId = gameDao.saveGame(title, password);
        pieceDao.savePieces(BoardFactory.create(), gameId);

        return gameId;
    }

    public WebBoardDto continueGame(Long gameId) {
        List<PieceEntity> pieceEntities = pieceDao.findAllPiecesByGameId(gameId);
        Board board = toBoard(pieceEntities);

        return WebBoardDto.from(board);
    }

    private Board toBoard(List<PieceEntity> rawBoard) {
        return new Board(rawBoard.stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> PieceFactory.create(piece.getName()))
                ));
    }

    public WebBoardDto move(Long gameId, MoveDto moveDto) {
        String srcPosition = moveDto.getSource();
        String dstPosition = moveDto.getTarget();
        Piece sourcePiece = PieceFactory.create(pieceDao.findNameByPositionAndGameId(srcPosition, gameId));
        Piece targetPiece = PieceFactory.create(pieceDao.findNameByPositionAndGameId(dstPosition, gameId));

        return move(gameId, srcPosition, dstPosition, sourcePiece, targetPiece);
    }

    public WebBoardDto move(Long gameId, String srcPosition, String dstPosition, Piece sourcePiece, Piece targetPiece) {
        Turn turn = Turn.from(getTurn(gameId));
        validateCurrentTurn(turn, sourcePiece);

        movePiece(gameId, srcPosition, dstPosition, sourcePiece, targetPiece, turn);

        Board board = toBoard(pieceDao.findAllPiecesByGameId(gameId));
        checkKingDead(gameId, turn, board);

        return WebBoardDto.from(board);
    }

    public String getTurn(Long gameId) {
        String turn = gameDao.findTurnByGameId(gameId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 ID 값입니다."));
        return turn;
    }

    private void validateCurrentTurn(Turn thisTurn, Piece sourcePiece) {
        if (!sourcePiece.isCurrentTurn(thisTurn)) {
            throw new IllegalArgumentException("본인의 말을 움직여야 합니다.");
        }
    }

    private void movePiece(Long gameId, String srcPosition, String dstPosition,
                           Piece sourcePiece, Piece targetPiece, Turn turn) {
        if (canMove(gameId, srcPosition, dstPosition, sourcePiece, targetPiece)) {
            pieceDao.updateByPositionAndGameId(PieceDao.getPieceName(sourcePiece), dstPosition, gameId);
            pieceDao.updateByPositionAndGameId("none-.", srcPosition, gameId);
            gameDao.updateTurnByGameId(gameId, turn.change().getThisTurn());
            return;
        }
        throw new IllegalArgumentException("기물을 이동할 수 없습니다.");
    }

    private boolean canMove(Long gameId, String srcPosition, String dstPosition, Piece sourcePiece, Piece targetPiece) {
        Position sourcePosition = Position.from(srcPosition);
        Position targetPosition = Position.from(dstPosition);
        MoveType moveType = MoveType.of(sourcePiece, targetPiece);

        return sourcePiece.isMovable(sourcePosition, targetPosition, moveType)
                && !hasBlock(gameId, sourcePosition, targetPosition, sourcePiece);
    }

    private boolean hasBlock(Long gameId, Position sourcePosition, Position targetPosition, Piece sourcePiece) {
        Board board = toBoard(pieceDao.findAllPiecesByGameId(gameId));
        List<Position> positions = sourcePiece.getIntervalPosition(sourcePosition, targetPosition);

        return positions.stream()
                .anyMatch(position -> !board.get(position).equals(new Empty()));
    }

    private void checkKingDead(Long gameId, Turn turn, Board board) {
        if (board.isKingDead()) {
            gameDao.updateTurnByGameId(gameId, "end");
        }
    }

    public boolean isKingDead(Long gameId) {
        Board board = toBoard(pieceDao.findAllPiecesByGameId(gameId));
        return board.isKingDead();
    }

    public GameResult getResult(Long gameId) {
        Board board = toBoard(pieceDao.findAllPiecesByGameId(gameId));
        return GameResult.from(board);
    }

    public void exitGame(Long gameId) {
        gameDao.updateTurnByGameId(gameId, "end");
    }

    public void restartGame(Long gameId) {
        pieceDao.deleteByGameId(gameId);
        pieceDao.savePieces(BoardFactory.create(), gameId);
        gameDao.updateTurnByGameId(gameId, "white");
    }

    public void deleteGame(Long gameId, String password) {
        if (canDeleteGame(gameId, password)) {
            pieceDao.deleteByGameId(gameId);
            gameDao.deleteByGameId(gameId);
            return;
        }
        throw new IllegalArgumentException("방 비밀번호가 맞지 않습니다.");
    }

    private boolean canDeleteGame(Long gameId, String password) {
        return getTurn(gameId).equals("end") && gameDao.findPasswordByGameId(gameId).equals(password);
    }
}