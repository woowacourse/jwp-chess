package wooteco.chess.service;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import chess.dto.TurnDto;
import org.springframework.stereotype.Service;
import wooteco.chess.entity.PieceEntity;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.TurnRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ChessGameService {
    private BoardRepository boardRepository;
    private TurnRepository turnRepository;

    public ChessGameService(BoardRepository boardRepository, TurnRepository turnRepository) {
        this.boardRepository = boardRepository;
        this.turnRepository = turnRepository;
    }

    public ChessBoard loadBoard() {
        return createBoardFromDb();
//        return createModel(chessBoard);
    }

    public ChessBoard createNewChessGame() {
        ChessBoard chessBoard = new ChessBoard();
        Map<Position, Piece> board = chessBoard.getBoard();

        this.turnRepository.deleteAll();
        for (Position position : board.keySet()) {
            this.boardRepository.insert(position.getName(), board.get(position).getName());
        }
        this.turnRepository.insert(TurnDto.from(chessBoard).getCurrentTeam());

        return chessBoard;
    }

    public ChessBoard movePiece(String source, String target) {
        ChessBoard chessBoard = createBoardFromDb();
        Map<Position, Piece> board = chessBoard.getBoard();
        chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

        this.boardRepository.deleteAll();
        for (Position position : board.keySet()) {
            this.boardRepository.insert(position.getName(), board.get(position).getName());
        }
        this.turnRepository.update(TurnDto.from(chessBoard).getCurrentTeam());

        return chessBoard;
    }

    private void endGame() {
        this.boardRepository.deleteAll();
        this.turnRepository.deleteAll();
    }

    public GameResult findWinner() {
        ChessBoard chessBoard = createBoardFromDb();
        GameResult gameResult = chessBoard.createGameResult();
        endGame();
        return gameResult;
    }

    public boolean isGameOver() {
        ChessBoard chessBoard = createBoardFromDb();
        return chessBoard.isGameOver();
    }

    public boolean isNotGameOver() {
        return !isGameOver();
    }

    private ChessBoard createBoardFromDb() {
        List<PieceEntity> pieceEntities = this.boardRepository.findAll();
        if (pieceEntities.isEmpty()) {
            throw new NoSuchElementException("테이블의 행이 비었습니다!!");
        }
        BoardDto boardDto = new BoardDto(pieceEntities);

        TurnDto turnDto = TurnDto.from(this.turnRepository.findFirst());

        return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
    }
}
