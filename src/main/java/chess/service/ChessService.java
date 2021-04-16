package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.TurnDto;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

import java.util.Map;

import static chess.domain.board.Position.convertStringToPosition;

@Service
public class ChessService {
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public BoardDto resetBoard() {
        Board initiatedBoard = BoardFactory.create();
        chessDao.resetTurnOwner();
        chessDao.resetBoard(initiatedBoard);
        return BoardDto.of(initiatedBoard);
    }

    public BoardDto getSavedBoardInfo() {
        return chessDao.getSavedBoardInfo();
    }

    public String score() {
        BoardDto boardDto = getSavedBoardInfo();
        Board board = BoardFactory.loadSavedBoardInfo(boardDto.getBoardInfo());
        double whiteScore = board.calculateScore(Team.WHITE);
        double blackScore = board.calculateScore(Team.BLACK);
        return "백 : " + whiteScore + "  흑 : " + blackScore;
    }

    public BoardDto move(MoveInfoDto moveInfoDto) {
        BoardDto boardDto = getSavedBoardInfo();
        Board board = BoardFactory.loadSavedBoardInfo(boardDto.getBoardInfo());

        Position target = convertStringToPosition(moveInfoDto.getTarget());

        Piece targetPiece = board.getBoard().get(target);
        TurnDto savedTurnOwner = chessDao.getSavedTurnOwner();
        Team savedTurnOwner2 = Team.convertStringToTeam(savedTurnOwner.getTurn());

        Team turnOwner = chessGameMove(board, savedTurnOwner2, moveInfoDto.getTarget(), moveInfoDto.getDestination());

        chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece);
        chessDao.renewTurnOwnerAfterMove(turnOwner);
        return BoardDto.of(board);
    }

    public Team chessGameMove(Board board, Team turnOwner, String target, String destination) {
        return board.movePiece(convertStringToPosition(target),
                convertStringToPosition(destination), turnOwner);
    }
}
