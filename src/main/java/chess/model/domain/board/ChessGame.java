package chess.model.domain.board;

import chess.model.domain.exception.ChessException;
import chess.model.domain.move.MoveStateAfter;
import chess.model.domain.move.MoveStateBefore;
import chess.model.domain.move.MoveStateChecker;
import chess.model.domain.move.MoveStatePromotion;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveOrder;
import chess.model.domain.state.MoveState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import util.NullChecker;

public class ChessGame {

    private static final MoveStateChecker BEFORE_MOVE_CHECKER
        = new MoveStateChecker(new MoveStateBefore());
    private static final MoveStateChecker AFTER_MOVE_CHECKER
        = new MoveStateChecker(new MoveStateAfter());
    private static final MoveStateChecker PROMOTION_MOVE_CHECKER
        = new MoveStateChecker(new MoveStatePromotion());

    private ChessBoard chessBoard;
    private Set<CastlingSetting> castlingElements;
    private Team gameTurn;
    private EnPassant enPassant;

    public ChessGame() {
        this(ChessBoard.create(), Team.WHITE,
            CastlingSetting.getCastlingElements(), new EnPassant());
    }

    public ChessGame(ChessBoard chessBoard, Team gameTurn, Set<CastlingSetting> castlingElements,
        EnPassant enPassant) {
        NullChecker.validateNotNull(chessBoard, gameTurn, castlingElements, enPassant);
        this.chessBoard = chessBoard;
        this.gameTurn = gameTurn;
        this.castlingElements = castlingElements;
        this.enPassant = enPassant;
    }

    public MoveState movePieceWhenCanMove(MoveInfo moveInfo) {
        MoveState moveState = BEFORE_MOVE_CHECKER.check(this, moveInfo);
        moveState = moveIfReady(moveInfo, moveState);
        gameTurn = moveState.turnTeam(gameTurn);
        return moveState;
    }

    private MoveState moveIfReady(MoveInfo moveInfo, MoveState moveState) {
        if (moveState.isReady()) {
            movePiece(moveInfo);
            moveState = AFTER_MOVE_CHECKER.check(this, moveInfo);
        }
        return moveState;
    }

    public Square findSquareForPromote() {
        return chessBoard.findSquareForPromote()
            .orElseThrow(() -> new ChessException("프로모션 가능한 폰이 없습니다."));
    }

    public boolean canPromote() {
        return chessBoard.findSquareForPromote().isPresent();
    }

    public MoveState promotion(Type typeToChange) {
        MoveState moveState = PROMOTION_MOVE_CHECKER.check(this);
        if (moveState == MoveState.NEEDS_PROMOTION) {
            chessBoard.put(findSquareForPromote(), makePieceToChange(typeToChange));
            moveState = MoveState.SUCCESS_PROMOTION;
            gameTurn = moveState.turnTeam(gameTurn);
        }
        return moveState;
    }

    public Piece makePieceToChange(Type typeToChange) {
        if (typeToChange.canChangeFromPawn()) {
            return PieceFactory.of(gameTurn, typeToChange);
        }
        throw new ChessException(typeToChange + "은 프로모션 할 수 있는 타입이 아닙니다.");
    }

    public boolean isNotMovable(MoveInfo moveInfo) {
        return !isMovable(moveInfo);
    }

    public boolean isMovable(MoveInfo moveInfo) {
        Square moveInfoBefore = moveInfo.get(MoveOrder.FROM);
        Square moveInfoAfter = moveInfo.get(MoveOrder.TO);
        Piece movePieceBefore = findPieceToMove(moveInfo);
        if (chessBoard.hasNot(moveInfoBefore) || !movePieceBefore.isSameTeam(gameTurn)) {
            return false;
        }
        if (isPawnMoveTwoRankForward(moveInfo)) {
            enPassant.add(movePieceBefore, moveInfo);
        }
        Map<Square, Piece> board = new HashMap<>(chessBoard.getChessBoard());
        if (movePieceBefore instanceof Pawn) {
            board.putAll(enPassant.getEnPassantBoard(gameTurn));
        }
        return movePieceBefore.getMovableArea(moveInfoBefore, board, castlingElements)
            .contains(moveInfoAfter);
    }

    public Set<Square> getMovableArea(Square beforeSquare) {
        Piece beforePiece = chessBoard.get(beforeSquare);
        if (chessBoard.hasNot(beforeSquare) || !beforePiece.isSameTeam(gameTurn)) {
            return new HashSet<>();
        }
        Map<Square, Piece> board = new HashMap<>();
        board.putAll(chessBoard.getChessBoard());
        board.putAll(enPassant.getEnPassantBoard(gameTurn));
        return beforePiece.getMovableArea(beforeSquare, board, castlingElements);
    }

    public boolean isPawnMoveTwoRankForward(MoveInfo moveInfo) {
        Piece movePieceBefore = chessBoard.get(moveInfo.get(MoveOrder.FROM));
        return EnPassant.isPawnMoveTwoRank(movePieceBefore, moveInfo);
    }

    private void movePiece(MoveInfo moveInfo) {
        Square moveInfoBefore = moveInfo.get(MoveOrder.FROM);
        Square moveInfoAfter = moveInfo.get(MoveOrder.TO);
        if (enPassant.hasOtherEnpassant(moveInfoAfter, gameTurn)) {
            chessBoard.remove(enPassant.getAfterSquare(moveInfoAfter));
        }
        Piece currentPiece = chessBoard.remove(moveInfoBefore);
        chessBoard.put(moveInfoAfter, currentPiece);
        enPassant.removeEnPassant(moveInfo);
        if (canCastling(moveInfo)) {
            castlingRook(moveInfo);
        }
        castlingElements.removeAll(castlingElements.stream()
            .filter(element -> element.isContains(moveInfoBefore))
            .collect(Collectors.toSet()));
    }

    public boolean canCastling(MoveInfo moveInfo) {
        return CastlingSetting.canCastling(castlingElements, moveInfo);
    }

    private void castlingRook(MoveInfo moveInfo) {
        Set<CastlingSetting> removeCastlingElements = castlingElements.stream()
            .filter(
                castlingElement -> castlingElement.isEqualSquare(moveInfo.get(MoveOrder.FROM)))
            .collect(Collectors.toSet());
        if (castlingElements.removeAll(removeCastlingElements)) {
            MoveInfo moveInfoOfRook = CastlingSetting.findMoveInfoOfRook(moveInfo);
            Piece currentPiece = chessBoard.remove(moveInfoOfRook.get(MoveOrder.FROM));
            chessBoard.put(moveInfoOfRook.get(MoveOrder.TO), currentPiece);
        }
    }

    public boolean isKingCaptured() {
        return chessBoard.countPieceOfKing() != Team.values().length;
    }

    public TeamScore getTeamScore() {
        return chessBoard.getTeamScore();
    }

    public boolean isEmptySquare(MoveInfo moveInfo) {
        return chessBoard.hasNot(moveInfo.get(MoveOrder.FROM));
    }

    public boolean isNotMyTurn(MoveInfo moveInfo) {
        if (isEmptySquare(moveInfo)) {
            return true;
        }
        return !chessBoard.get(moveInfo.get(MoveOrder.FROM)).isSameTeam(gameTurn);
    }

    public Piece findPieceToMove(MoveInfo moveInfo) {
        return chessBoard.get(moveInfo.get(MoveOrder.FROM));
    }

    public Set<CastlingSetting> getCastlingElements() {
        return castlingElements;
    }

    public Team getGameTurn() {
        return gameTurn;
    }

    public Map<Square, Piece> getChessBoard() {
        return chessBoard.getChessBoard();
    }
}