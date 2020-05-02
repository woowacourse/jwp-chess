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

    private final Board board;
    private final Castling castling;
    private final EnPassant enPassant;
    private Team turn;

    public ChessGame() {
        this(Board.createInitial(), Team.WHITE, Castling.createInitial(),
            new EnPassant());
    }

    public ChessGame(Board board, Team turn, Castling castling,
        EnPassant enPassant) {
        NullChecker.validateNotNull(board, turn, castling, enPassant);
        this.board = board;
        this.turn = turn;
        this.castling = castling;
        this.enPassant = enPassant;
    }

    public static ChessGame of(ChessGame chessGame) {
        return new ChessGame(Board.of(new HashMap<>(chessGame.getBoard()))
            , chessGame.turn
            , Castling.of(new HashSet<>(chessGame.getCastling()))
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
        board.move(moveInfo);
        executeEnPassant(moveInfo);
        executeCastling(moveInfo);
    }

    private void executeEnPassant(MoveInfo moveInfo) {
        if (enPassant.isEnemyPast(moveInfo.getTarget(), turn)) {
            board.removeBy(enPassant.getCurrentSquare(moveInfo.getTarget()));
        }
        enPassant.removeEnPassant(turn);
        enPassant.removeEnPassant(moveInfo);
    }

    private void executeCastling(MoveInfo moveInfo) {
        if (canCastling(moveInfo)) {
            board.move(CastlingSetting.findRookCastlingMotion(moveInfo.getTarget()));
        }
        castling.remove(moveInfo.getSource());
    }

    private boolean canCastling(MoveInfo moveInfo) {
        return castling.canCastling(moveInfo);
    }

    public Square findSquareForPromote() {
        return board.findSquareForPromote()
            .orElseThrow(() -> new IllegalArgumentException("프로모션 가능한 폰이 없습니다."));
    }

    public boolean canPromote() {
        return board.findSquareForPromote().isPresent();
    }

    public MoveState promote(Type typeToPromotion) {
        MoveState moveState = PROMOTION_MOVE_CHECKER.check(this);
        if (moveState == MoveState.NEEDS_PROMOTION) {
            board.changePiece(findSquareForPromote(), makePieceToPromotion(typeToPromotion));
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
        Piece sourcePiece = board.findPieceBy(moveInfo.getSource());
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
        Piece sourcePiece = board.findPieceBy(source);
        if (board.isNotExist(source) || sourcePiece.isNotSameTeam(turn)) {
            return new HashSet<>();
        }
        Board boardForMovable = makeChessBoardForMovable(sourcePiece);
        return sourcePiece.findMovableAreas(source,
            boardForMovable.getBoard(), castling.getCastling());
    }

    private Board makeChessBoardForMovable(Piece sourcePiece) {
        Board boardForCheck = Board.of(board);
        if (sourcePiece instanceof Pawn) {
            boardForCheck.addElements(enPassant.getEnPassantBoard(turn));
        }
        return boardForCheck;
    }

    public boolean isPawnMoveTwoRankForward(MoveInfo moveInfo) {
        if (board.isNotExist(moveInfo.getSource())) {
            return false;
        }
        Piece sourcePiece = board.findPieceBy(moveInfo.getSource());
        return EnPassant.isPawnMoveTwoRank(sourcePiece, moveInfo);
    }

    public boolean isKingCaptured() {
        return board.countPieceOfKing() != Team.values().length;
    }

    public TeamScore deriveTeamScore() {
        return board.deriveTeamScore();
    }

    public boolean isNotExistPiece(Square square) {
        return board.isNotExist(square);
    }

    public boolean isNotCorrectTurn(MoveInfo moveInfo) {
        if (board.isNotExist(moveInfo.getSource())) {
            return true;
        }
        return !board.findPieceBy(moveInfo.getSource()).isSameTeam(turn);
    }

    public Set<CastlingSetting> getCastling() {
        return castling.getCastling();
    }

    public Team getTurn() {
        return turn;
    }

    public Map<Square, Piece> getBoard() {
        return board.getBoard();
    }

    public Map<Square, Square> getEnPassants() {
        return enPassant.getEnPassants();
    }

}