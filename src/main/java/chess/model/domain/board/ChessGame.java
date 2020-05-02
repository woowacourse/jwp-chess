package chess.model.domain.board;

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
import chess.model.domain.state.MoveState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import util.NullChecker;

public class ChessGame {

    private static final MoveStateChecker BEFORE_MOVE_CHECKER
        = new MoveStateChecker(new MoveStateBefore());
    private static final MoveStateChecker AFTER_MOVE_CHECKER
        = new MoveStateChecker(new MoveStateAfter());
    private static final MoveStateChecker PROMOTION_MOVE_CHECKER
        = new MoveStateChecker(new MoveStatePromotion());

    private final ChessBoard chessBoard;
    private final CastlingElement castlingElements;
    private Team turn;
    private final EnPassant enPassant;

    public ChessGame() {
        this(ChessBoard.createInitial(), Team.WHITE, CastlingElement.createInitial(),
            new EnPassant());
    }

    public ChessGame(ChessBoard chessBoard, Team turn, CastlingElement castlingElements,
        EnPassant enPassant) {
        NullChecker.validateNotNull(chessBoard, turn, castlingElements, enPassant);
        this.chessBoard = chessBoard;
        this.turn = turn;
        this.castlingElements = castlingElements;
        this.enPassant = enPassant;
    }

    public static ChessGame of(ChessGame chessGame) {
        return new ChessGame(ChessBoard.of(new HashMap<>(chessGame.getChessBoard()))
            , chessGame.turn
            , CastlingElement.of(new HashSet<>(chessGame.getCastlingElements()))
            , new EnPassant(new HashMap<>(chessGame.getEnPassants()))
        );
    }

    public MoveState move(MoveInfo moveInfo) {
        MoveState moveState = BEFORE_MOVE_CHECKER.check(this, moveInfo);
        moveState = moveByMoveState(moveInfo, moveState);
        turn = moveState.turnTeam(turn);
        return moveState;
    }

    private MoveState moveByMoveState(MoveInfo moveInfo, MoveState moveState) {
        if (moveState.isReady()) {
            movePiece(moveInfo);
            return AFTER_MOVE_CHECKER.check(this, moveInfo);
        }
        return moveState;
    }

    private void movePiece(MoveInfo moveInfo) {
        chessBoard.move(moveInfo);
        executeEnPassant(moveInfo);
        executeCastling(moveInfo);
    }

    private void executeEnPassant(MoveInfo moveInfo) {
        if (enPassant.isEnemyPast(moveInfo.getTarget(), turn)) {
            chessBoard.removeBy(enPassant.getCurrentSquare(moveInfo.getTarget()));
        }
        enPassant.removeEnPassant(turn);
        enPassant.removeEnPassant(moveInfo);
    }

    private void executeCastling(MoveInfo moveInfo) {
        if (canCastling(moveInfo)) {
            chessBoard.move(CastlingSetting.findRookCastlingMotion(moveInfo.getTarget()));
        }
        castlingElements.remove(moveInfo.getSource());
    }

    public boolean canCastling(MoveInfo moveInfo) {
        return castlingElements.canCastling(moveInfo);
    }

    public Square findSquareForPromote() {
        return chessBoard.findSquareForPromote()
            .orElseThrow(() -> new IllegalArgumentException("프로모션 가능한 폰이 없습니다."));
    }

    public boolean canPromote() {
        return chessBoard.findSquareForPromote().isPresent();
    }

    public MoveState promote(Type typeToPromotion) {
        MoveState moveState = PROMOTION_MOVE_CHECKER.check(this);
        if (moveState == MoveState.NEEDS_PROMOTION) {
            chessBoard.changePiece(findSquareForPromote(), makePieceToPromotion(typeToPromotion));
            moveState = MoveState.SUCCESS_PROMOTION;
            turn = moveState.turnTeam(turn);
        }
        return moveState;
    }

    public Piece makePieceToPromotion(Type typeToChange) {
        if (typeToChange.canPromote()) {
            return PieceFactory.getPiece(turn, typeToChange);
        }
        throw new IllegalArgumentException(typeToChange + "은 프로모션 할 수 있는 타입이 아닙니다.");
    }

    public boolean isNotMovable(MoveInfo moveInfo) {
        return !isMovable(moveInfo);
    }

    public boolean isMovable(MoveInfo moveInfo) {
        Piece sourcePiece = findPiece(moveInfo.getSource());
        Set<Square> movableArea = findMovableAreas(moveInfo.getSource());
        if (movableArea.isEmpty()) {
            return false;
        }
        addEnPassant(moveInfo, sourcePiece);
        return movableArea.contains(moveInfo.getTarget());
    }

    private void addEnPassant(MoveInfo moveInfo, Piece sourcePiece) {
        if (isPawnMoveTwoRankForward(moveInfo)) {
            enPassant.add(sourcePiece, moveInfo);
        }
    }

    public Set<Square> findMovableAreas(Square source) {
        Piece sourcePiece = findPiece(source);
        if (chessBoard.isNotExist(source) || sourcePiece.isNotSameTeam(turn)) {
            return new HashSet<>();
        }
        ChessBoard chessBoardForMovable = makeChessBoardForMovable(sourcePiece);
        return sourcePiece.findMovableAreas(source,
            chessBoardForMovable.getChessBoard(), castlingElements.getCastlingElements());
    }

    private ChessBoard makeChessBoardForMovable(Piece sourcePiece) {
        ChessBoard chessBoardForCheck = ChessBoard.of(chessBoard);
        if (sourcePiece instanceof Pawn) {
            chessBoardForCheck.addElements(enPassant.getEnPassantBoard(turn));
        }
        return chessBoardForCheck;
    }

    public boolean isPawnMoveTwoRankForward(MoveInfo moveInfo) {
        if (chessBoard.isNotExist(moveInfo.getSource())) {
            return false;
        }
        Piece sourcePiece = chessBoard.findPieceBy(moveInfo.getSource());
        return EnPassant.isPawnMoveTwoRank(sourcePiece, moveInfo);
    }

    public boolean isKingCaptured() {
        return chessBoard.countPieceOfKing() != Team.values().length;
    }

    public TeamScore deriveTeamScore() {
        return chessBoard.deriveTeamScore();
    }

    public boolean isNotExistPiece(Square square) {
        return chessBoard.isNotExist(square);
    }

    public boolean isNotCorrectTurn(MoveInfo moveInfo) {
        if (chessBoard.isNotExist(moveInfo.getSource())) {
            return true;
        }
        return !chessBoard.findPieceBy(moveInfo.getSource()).isSameTeam(turn);
    }

    public Piece findPiece(Square square) {
        return chessBoard.findPieceBy(square);
    }

    public Set<CastlingSetting> getCastlingElements() {
        return castlingElements.getCastlingElements();
    }

    public Team getTurn() {
        return turn;
    }

    public Map<Square, Piece> getChessBoard() {
        return chessBoard.getChessBoard();
    }

    public Map<Square, Square> getEnPassants() {
        return enPassant.getEnPassants();
    }

}