package chess.service;

import chess.domain.dao.BoardDao;
import chess.domain.dao.GameDao;
import chess.domain.dto.GameDto;
import chess.domain.dto.PieceDto;
import chess.domain.dto.ResponseDto;
import chess.domain.game.Color;
import chess.domain.game.board.ChessBoard;
import chess.domain.game.board.ChessBoardFactory;
import chess.domain.piece.ChessPiece;
import chess.domain.piece.Type;
import chess.domain.position.Position;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessService {

    private ChessBoard chessBoard = null;
    private GameDao gameDao = new GameDao();
    private BoardDao boardDao = new BoardDao();

    public void start() throws SQLException {
        if(isNotSaved()){
            chessBoard = ChessBoardFactory.initBoard();
            chessBoard.start();
            return;
        }
        loadLastGame();
    }

    private boolean isNotSaved() throws SQLException {
        return gameDao.findLastGameId() == 0;
    }

    private void loadLastGame() throws SQLException {
        HashMap<Position, ChessPiece> board = new HashMap<>();
        for (PieceDto pieceDto : boardDao.findByGameId(gameDao.findLastGameId())) {
            ChessPiece piece = makePiece(pieceDto);
            board.put(new Position(pieceDto.getPosition()), piece);
        }
        GameDto game = gameDao.findById(gameDao.findLastGameId());
        chessBoard = new ChessBoard(board, game.getStatus(), game.getTurn());
    }

    private ChessPiece makePiece(PieceDto pieceDto) {
        return Type.from(pieceDto.getPiece()).createPiece(getPieceColor(pieceDto));
    }

    private Color getPieceColor(PieceDto pieceDto) {
        return Color.from(pieceDto.getColor());
    }

    public void end() throws SQLException {
        chessBoard.end();
        boardDao.delete(gameDao.findLastGameId());
        gameDao.delete();
    }

    public String move(String source, String target){
        try{
            if(chessBoard.isPlaying()){
                chessBoard.move(new Position(source), new Position(target));
            }
        }catch (IllegalArgumentException e){
            return new ResponseDto(500, e.getMessage()).toString();
        }
        return new ResponseDto(200, null).toString();
    }

    public Map<String, Double> status(){
        return chessBoard.calculateScore().entrySet().stream()
                .collect(Collectors.toMap(m -> m.getKey().toString(), Map.Entry::getValue));
    }

    public String findWinner() {
        return chessBoard.decideWinner().name();
    }

    public boolean isEnd(){
        return chessBoard.isEnd();
    }

    public Map<String, ChessPiece> getCurrentBoard() {
        return chessBoard.convertToMap();
    }

    public boolean isPlaying() {
        return chessBoard.isPlaying();
    }

    public void save() {
        int gameId = gameDao.save(chessBoard);
        for (Map.Entry<String, ChessPiece> entry : chessBoard.convertToMap().entrySet()) {
            boardDao.save(gameId, entry.getKey(), entry.getValue().getName(), entry.getValue().getColor().name());
        }
    }
}
