package service;

import chess.board.ChessBoard;
import chess.command.MoveCommand;
import chess.game.ChessGame;
import chess.location.Location;
import chess.piece.type.Piece;
import chess.progress.Progress;
import chess.team.Team;
import com.google.gson.Gson;
import converter.ChessGameConverter;
import dao.BoardDao;
import dao.ChessGameDao;
import dao.ChessGamesDao;
import dao.PieceDao;
import dto.*;
import vo.PieceVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessService {
    private static final ChessGamesDao chessGamesDao = new ChessGamesDao();
    private static final PieceDao pieceDao = new PieceDao();
    private static final ChessGameDao chessGameDao = new ChessGameDao();
    private static final BoardDao boardDao = new BoardDao();
    private static final Gson GSON = new Gson();
    private static final int GAME_ID = 1;

    public String findAllBoards() throws SQLException {
        ArrayList<ChessGameDto> all = chessGamesDao.findAll();
        ChessGamesDto chessGamesDto = new ChessGamesDto(all);
        return GSON.toJson(chessGamesDto);
    }

    public String findBoard(int boardId) throws SQLException {
        List<PieceVo> pieceVos = pieceDao.findAll(boardId);

        ChessGameDto chessGameDto = chessGameDao.findChessGameBy(boardId);

        if (pieceVos == null) {
            BoardDto boardDto = new BoardDto(new ChessGame().getChessBoard());
            return GSON.toJson(boardDto);
        }

        ChessGame chessGame = ChessGameConverter.convert(pieceVos, chessGameDto);
        BoardDto boardDto = new BoardDto(chessGame.getChessBoard());
        return GSON.toJson(boardDto);
    }

    public String move(LocationDto nowDto,LocationDto destinationDto, int gameId) throws SQLException {
        Location now = nowDto.toEntity();
        Location destination = destinationDto.toEntity();
        ChessGame chessGame = makeGameByDB(gameId);

        MoveCommand move = MoveCommand.of(now, destination, chessGame);

        Piece nowPiece = chessGame.getPiece(now);

        Progress progress = chessGame.doOneCommand(move);

        if (!progress.isError()) {
            updatePiece(nowPiece, now, destination, gameId);
            chessGame.changeTurn();
            chessGameDao.updateTurn(chessGame.getTurn(), gameId);
        }

        ChessMoveDto chessMoveDto = new ChessMoveDto(
                new ChessGameScoresDto(chessGame.calculateScores())
                , progress
                , chessGame.getTurn());

        return GSON.toJson(chessMoveDto);
    }

    private void updatePiece(Piece nowPiece, Location now, Location destination, int gameId) throws SQLException {
        pieceDao.update(now, destination, nowPiece, gameId);
    }

    public String findWinner(ChessGame chessGame) {
        ChessResultDto chessResultDto = new ChessResultDto(chessGame.findWinner());
        return GSON.toJson(chessResultDto);
    }

    public void insertChessBoard(ChessGame chessGame) throws SQLException {
        boardDao.addBoard(chessGame.getChessBoard(), GAME_ID);
    }

    public ChessGame makeGameByDB(int gameId) throws SQLException {
        ChessGameDto chessGameDto = chessGameDao.findChessGameBy(gameId);
        List<PieceVo> pieceDto = ChessService.pieceDao.findAll(gameId);
        Team team = Team.of(chessGameDto.isTurnBlack());
        ChessBoard chessBoard = makeChessBoard(pieceDto);
        return new ChessGame(chessBoard, team);
    }

    private ChessBoard makeChessBoard(List<PieceVo> pieceDto) {
        Map<Location, Piece> board = new HashMap<>();
        for (PieceVo pieceVo : pieceDto) {
            Location location = toLocation(pieceVo);
            Piece piece = pieceVo.toPiece();
            board.put(location, piece);
        }

        return new ChessBoard(board);
    }

    private Location toLocation(PieceVo pieceVo) {
        int row = pieceVo.getRow();
        char col = pieceVo.getCol().charAt(0);
        return new Location(row, col);
    }
}
