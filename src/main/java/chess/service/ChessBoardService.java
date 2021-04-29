package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PiecePositions;
import chess.domain.team.Team;
import chess.webdao.BoardDao;
import chess.webdao.RoomDao;
import chess.webdto.dao.*;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ChessBoardService {
    private final BoardDao chessDao;
    private final RoomDao roomDao;


    public ChessBoardService(BoardDao chessDao, RoomDao roomDao) {
        this.chessDao = chessDao;
        this.roomDao = roomDao;
    }


    @Transactional
    public ChessGameDto startNewGame(long roomId) {
        chessDao.deleteBoardByRoomId(roomId);
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());

        insertBoardInfos(chessGame, roomId);
        return new ChessGameDto(chessGame);
    }


    private void insertBoardInfos(ChessGame chessGame, long roomId) {
        Map<Position, Piece> whites = chessGame.currentWhitePiecePosition();
        for (Map.Entry<Position, Piece> whiteInfo : whites.entrySet()) {
            chessDao.createBoard(new TeamInfoDto(TeamConstants.WHITE, whiteInfo.getKey(), whiteInfo.getValue(), roomId));
        }

        Map<Position, Piece> blacks = chessGame.currentBlackPiecePosition();
        for (Map.Entry<Position, Piece> blackInfo : blacks.entrySet()) {
            chessDao.createBoard(new TeamInfoDto(TeamConstants.BLACK, blackInfo.getKey(), blackInfo.getValue(), roomId));
        }
    }

    public ChessGameDto loadPreviousGame(long roomId) {
        TurnDto turnDto = roomDao.selectTurnByRoomId(roomId);
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(roomId);

        final ChessGame chessGame = covertToChessGame(turnDto, boardInfos);

        return new ChessGameDto(chessGame);
    }


    @Transactional
    public ChessGameDto move(MoveRequestDto moveRequestDto, long roomId) {
        TurnDto turnDto = roomDao.selectTurnByRoomId(roomId);
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(roomId);

        final ChessGame chessGame = covertToChessGame(turnDto, boardInfos);

        String startPosition = moveRequestDto.getStart();
        String destPosition = moveRequestDto.getDestination();
        chessGame.move(Position.of(startPosition), Position.of(destPosition));

        chessDao.deleteBoardByRoomId(roomId);
        roomDao.changeTurnByRoomId(TeamConstants.convert(chessGame.isWhiteTeamTurn()), chessGame.isPlaying(), roomId);
        insertBoardInfos(chessGame, roomId);

        return new ChessGameDto(chessGame);
    }

    private ChessGame covertToChessGame(TurnDto turnDto, List<BoardInfosDto> boardInfos) {
        Map<Position, Piece> whites = new HashMap<>();
        Map<Position, Piece> blacks = new HashMap<>();
        for (BoardInfosDto boardInfo : boardInfos) {
            sortBlackAndWhite(whites, blacks, boardInfo);
        }
        PiecePositions whitePiecePosition = new PiecePositions(whites);
        PiecePositions blackPiecePosition = new PiecePositions(blacks);

        Team blackTeam = new Team(blackPiecePosition);
        Team whiteTeam = new Team(whitePiecePosition);

        return new ChessGame(blackTeam, whiteTeam, currentTurn(blackTeam, whiteTeam, turnDto.getTurn()), turnDto.getIsPlaying());
    }

    private void sortBlackAndWhite(Map<Position, Piece> whites, Map<Position, Piece> blacks, BoardInfosDto boardInfo) {
        if (boardInfo.isWhite()) {
            whites.put(Position.of(boardInfo.getPosition()), DaoToPiece.generatePiece(boardInfo.getTeam(), boardInfo.getPiece(), boardInfo.getIsFirstMoved()));
        }

        if (boardInfo.isBlack()) {
            blacks.put(Position.of(boardInfo.getPosition()), DaoToPiece.generatePiece(boardInfo.getTeam(), boardInfo.getPiece(), boardInfo.getIsFirstMoved()));
        }
    }

    private Team currentTurn(Team black, Team white, String currentTurnTeam) {
        if (currentTurnTeam.equals("white")) {
            return white;
        }
        return black;
    }


}
