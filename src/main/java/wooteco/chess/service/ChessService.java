package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.judge.Judge;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.BoardDTO;
import wooteco.chess.dto.GameStatusDTO;
import wooteco.chess.repository.BoardDAO;
import wooteco.chess.webutil.ModelParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    @Autowired
    BoardDAO boardDAO;

    public Map<String, Object> loadInitBoard() {
        Map<String, Object> model = ModelParser.parseBlankBoard();
        model.putAll(ModelParser.parseMovablePlaces(new ArrayList<>()));
        return model;
    }

    public Board newGame() throws SQLException {
        Board board = BoardFactory.create();

        writeWholeBoard(board);
        writeCurrentTurn(Team.WHITE);

        return board;
    }

    private void writeWholeBoard(final Board board) throws SQLException {
        BoardDTO boardDTO = new BoardDTO();
        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);

            boardDTO.setPosition(position.toString());
            boardDTO.setPiece(piece.toString());

            boardDAO.placePieceOn(boardDTO);
        }
    }

    private void writeCurrentTurn(final Team turn) throws SQLException {
        GameStatusDTO gameStatusDTO = new GameStatusDTO();
        gameStatusDTO.setCurrentTeam(turn.toString());

        boardDAO.updateTurn(gameStatusDTO);
    }

    public void move(String start, String end) throws SQLException {
        checkGameOver();

        Board board = readBoard();
        try {
            board.move(Position.of(start), Position.of(end));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        writeWholeBoard(board);
        writeCurrentTurn(board.getCurrentTurn());
    }

    public Board readBoard() throws SQLException {
        List<BoardDTO> boardDTOs = boardDAO.findAllPieces();
        GameStatusDTO gameStatusDTO = boardDAO.readCurrentTurn();

        Team currentTurn = Team.of(gameStatusDTO.getCurrentTeam());
        return new Board(parsePieceInformation(boardDTOs), currentTurn);
    }

    public Map<String, Object> readBoardWithScore() throws SQLException {
        Board board = readBoard();

        Map<String, Object> model = ModelParser.parseBoard(board);
        model.put("player1_info", "WHITE: " + calculateScore(Team.WHITE));
        model.put("player2_info", "BLACK: " + calculateScore(Team.BLACK));
        return model;
    }

    private Map<Position, Piece> parsePieceInformation(final List<BoardDTO> boardDTOs) {
        return boardDTOs.stream()
                .collect(Collectors.toMap(dto -> Position.of(dto.getPosition()), dto -> Piece.of(dto.getPiece())));
    }

    private double calculateScore(final Team team) throws SQLException {
        Judge judge = new Judge();
        return Judge.getScoreByTeam(readBoard(), team);
    }

    public Map<String, Object> loadMovable(String startName) throws SQLException {
        Position start = Position.of(startName);
        List<Position> movablePositions = findMovablePlaces(start);
        Map<String, Object> model = ModelParser.parseBoard(readBoard(), movablePositions);
        if (movablePositions.size() != 0) {
            model.put("start", startName);
        }
        return model;
    }

    private List<Position> findMovablePlaces(final Position start) throws SQLException {
        checkGameOver();
        return readBoard().findMovablePositions(start);
    }

    private void checkGameOver() throws SQLException {
        if (Judge.findWinner(readBoard()).isPresent()) {
            throw new IllegalArgumentException();
        }
    }
}
