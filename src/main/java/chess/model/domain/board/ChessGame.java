package chess.model.domain.board;

import chess.model.domain.move.MoveStateAfter;
import chess.model.domain.move.MoveStateBefore;
import chess.model.domain.move.MoveStateChecker;
import chess.model.domain.move.MoveStatePromotion;
import chess.model.domain.piece.King;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveOrder;
import chess.model.domain.state.MoveState;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private Map<Square, Piece> chessBoard;
    private Set<CastlingSetting> castlingElements;
    private Team gameTurn;
    private EnPassant enPassant;

    public ChessGame() {
        this(new BoardInitialDefault(), Team.WHITE, CastlingSetting.getCastlingElements(), new EnPassant());
    }

    public ChessGame(BoardInitialization chessBoard, Team gameTurn,
        Set<CastlingSetting> castlingElements, EnPassant enPassant) {
        NullChecker.validateNotNull(chessBoard, gameTurn, castlingElements, enPassant);
        this.chessBoard = chessBoard.getInitialize();
        this.gameTurn = gameTurn;
        this.castlingElements = castlingElements;
        this.enPassant = enPassant;
    }

    public static boolean isInitialPoint(Square square, Piece piece) {
        return (piece instanceof Pawn)
            && (square.isSameRank(Rank.SEVENTH) || square.isSameRank(Rank.SECOND));
    }

    public Map<Square, Piece> getChessBoard() {
        return chessBoard;
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

    public Optional<Square> getFinishPawnBoard() {
        return chessBoard.keySet().stream()
            .filter(boardSquare -> chessBoard.get(boardSquare) instanceof Pawn)
            .filter(Square::isLastRank)
            .findFirst();
    }

    public boolean isNeedPromotion() {
        return chessBoard.keySet().stream()
            .filter(boardSquare -> chessBoard.get(boardSquare) instanceof Pawn)
            .anyMatch(Square::isLastRank);
    }

    public MoveState promotion(Type hopeType) {
        MoveState moveState = PROMOTION_MOVE_CHECKER.check(this);
        if (moveState == MoveState.NEEDS_PROMOTION) {
            chessBoard.put(getFinishPawnBoard().orElseThrow(IllegalAccessError::new),
                getHopePiece(hopeType));
            moveState = MoveState.SUCCESS_PROMOTION;
            gameTurn = moveState.turnTeam(gameTurn);
        }
        return moveState;
    }

    public Piece getHopePiece(Type hopeType) {
        return Arrays.stream(CastlingSetting.values())
            .map(CastlingSetting::getPiece)
            .filter(piece -> piece.isSameType(hopeType))
            .filter(piece -> piece.isSameTeam(gameTurn))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public boolean canMove(MoveInfo moveInfo) {
        Square moveInfoBefore = moveInfo.get(MoveOrder.FROM);
        Square moveInfoAfter = moveInfo.get(MoveOrder.TO);
        Piece movePieceBefore = whoMovePiece(moveInfo);
        if (!chessBoard.containsKey(moveInfoBefore) || !movePieceBefore.isSameTeam(gameTurn)) {
            return false;
        }
        if (isPawnJump(moveInfo)) {
            enPassant.addIfPawnJump(movePieceBefore, moveInfo);
        }
        Map<Square, Piece> board = new HashMap<>(chessBoard);
        if (movePieceBefore instanceof Pawn) {
            board.putAll(enPassant.getEnPassantBoard(gameTurn));
        }
        return movePieceBefore.getMovableArea(moveInfoBefore, board, castlingElements)
            .contains(moveInfoAfter);
    }

    public Set<Square> getMovableArea(Square beforeSquare) {
        Piece beforePiece = chessBoard.get(beforeSquare);
        if (!chessBoard.containsKey(beforeSquare) || !beforePiece.isSameTeam(gameTurn)) {
            return new HashSet<>();
        }
        Map<Square, Piece> board = new HashMap<>();
        board.putAll(chessBoard);
        board.putAll(enPassant.getEnPassantBoard(gameTurn));
        return beforePiece.getMovableArea(beforeSquare, board, castlingElements);
    }

    public boolean isPawnJump(MoveInfo moveInfo) {
        Piece movePieceBefore = chessBoard.get(moveInfo.get(MoveOrder.FROM));
        return EnPassant.isPawnJump(movePieceBefore, moveInfo);
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
            MoveInfo moveInfoRook = CastlingSetting.getMoveCastlingRook(moveInfo);
            Piece currentPiece = chessBoard.remove(moveInfoRook.get(MoveOrder.FROM));
            chessBoard.put(moveInfoRook.get(MoveOrder.TO), currentPiece);
        }
    }

    public boolean isKingCaptured() {
        return chessBoard.values().stream()
            .filter(piece -> piece instanceof King)
            .count() != Team.values().length;
    }

    public TeamScore getTeamScore() {
        return new TeamScore(chessBoard.values(), countPawnSameFileByTeam());
    }

    public Team getWinnerTurn() {
        return gameTurn.previousTurnIfEmptyMySelf();
    }

    private Map<Team, Integer> countPawnSameFileByTeam() {
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        for (Team team : Team.values()) {
            List<Square> pawnSquare = getSquareIfSameTeamPawn(team);
            pawnSameFileCountByTeam.put(team, getCountSameFile(pawnSquare));
        }
        return pawnSameFileCountByTeam;
    }

    private int getCountSameFile(List<Square> pawnSquare) {
        int count = 0;
        for (Square boardSquare : pawnSquare) {
            count += pawnSquare.stream()
                .filter(square -> boardSquare.isSameFile(square) && boardSquare != square)
                .count();
        }
        return count;
    }

    private List<Square> getSquareIfSameTeamPawn(Team team) {
        return chessBoard.keySet().stream()
            .filter(square -> chessBoard.get(square) == Pawn.getPieceInstance(team))
            .collect(Collectors.toList());
    }

    public boolean isNoPiece(MoveInfo moveInfo) {
        return !chessBoard.containsKey(moveInfo.get(MoveOrder.FROM));
    }

    public Team getGameTurn() {
        return gameTurn;
    }

    public boolean isNotMyTurn(MoveInfo moveInfo) {
        if (isNoPiece(moveInfo)) {
            return true;
        }
        return !chessBoard.get(moveInfo.get(MoveOrder.FROM)).isSameTeam(gameTurn);
    }

    public Set<CastlingSetting> getCastlingElements() {
        return castlingElements;
    }

    public Piece whoMovePiece(MoveInfo moveInfo) {
        return chessBoard.get(moveInfo.get(MoveOrder.FROM));
    }
}