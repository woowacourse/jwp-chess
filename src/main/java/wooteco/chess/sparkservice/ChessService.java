package wooteco.chess.sparkservice;

import wooteco.chess.dao.BoardDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.judge.Judge;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.BoardDTO;
import wooteco.chess.dto.GameStatusDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessService {
    private static ChessService chessService = new ChessService();

    private ChessService() {
    }

    public static ChessService getInstance() {
        return chessService;
    }

    public Board newGame() throws SQLException {
        Board board = BoardFactory.create();

        writeWholeBoard(board);
        writeCurrentTurn(Team.WHITE);

        return board;
    }

    private void writeWholeBoard(Board board) throws SQLException {
        BoardDAO boardDAO = BoardDAO.getInstance();

        BoardDTO boardDTO = new BoardDTO();
        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);

            boardDTO.setPosition(position.toString());
            boardDTO.setPiece(piece.toString());

            boardDAO.placePieceOn(boardDTO);
        }
    }

    private void writeCurrentTurn(Team turn) throws SQLException {
        BoardDAO boardDAO = BoardDAO.getInstance();

        GameStatusDTO gameStatusDTO = new GameStatusDTO();
        gameStatusDTO.setCurrentTeam(turn.toString());

        boardDAO.initializeTurn();
        boardDAO.updateTurn(gameStatusDTO);
    }

    public void move(Position start, Position end) throws SQLException {
        checkGameOver();
        Board board = readBoard();
        board.move(start, end);

        writeWholeBoard(board);
        writeCurrentTurn(board.getCurrentTurn());
    }

    public Board readBoard() throws SQLException {
        BoardDAO boardDAO = BoardDAO.getInstance();
        List<BoardDTO> boardDTOs = boardDAO.findAllPieces();
        GameStatusDTO gameStatusDTO = boardDAO.readCurrentTurn();

        Team currentTurn = Team.of(gameStatusDTO.getCurrentTeam());

        return new Board(parse(boardDTOs), currentTurn);
    }

    private Map<Position, Piece> parse(List<BoardDTO> boardDTOs) {
        return boardDTOs.stream()
                .collect(Collectors.toMap(dto -> Position.of(dto.getPosition()), dto -> Piece.of(dto.getPiece())));
    }

    public double calculateScore(Team team) throws SQLException {
        Board board = readBoard();
        Judge judge = new Judge();
        return judge.getScoreByTeam(board, team);
    }

    public List<Position> findMovablePlaces(Position start) throws SQLException {
        checkGameOver();
        Board board = readBoard();
        return board.findMovablePositions(start);
    }

    private void checkGameOver() throws SQLException {
        Board board = readBoard();
        Judge judge = new Judge();

        if (judge.findWinner(board).isPresent()) {
            throw new IllegalArgumentException();
        }
    }
}
