package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PiecePositions;
import chess.domain.team.Team;
import chess.webdao.MysqlChessDao;
import chess.webdto.dao.DaoToPiece;
import chess.webdto.dao.TeamConstants;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TeamInfoDto;
import chess.webdto.dao.TurnDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SpringChessService {
    private final MysqlChessDao chessDao;

    public SpringChessService(MysqlChessDao chessDao) {
        this.chessDao = chessDao;
    }


    @Transactional
    public ChessGameDto startNewGame() {
        chessDao.deleteBoardByRoomId(1);
        chessDao.deleteRoomByRoomId(1);

        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        chessDao.createRoom(TeamConstants.convert(chessGame.isWhiteTeamTurn()), chessGame.isPlaying());

        long roomId = 1;
        insertBoardInfos(chessGame, roomId);
        return new ChessGameDto(chessGame);
    }

    private void insertBoardInfos(ChessGame chessGame, long roomId) {
        Map<Position, Piece> whites = chessGame.currentWhitePiecePosition();
        for (Map.Entry<Position, Piece> whiteInfo : whites.entrySet()) {
            chessDao.createBoard(new TeamInfoDto(TeamConstants.WHITE, whiteInfo, roomId));
        }

        Map<Position, Piece> blacks = chessGame.currentBlackPiecePosition();
        for (Map.Entry<Position, Piece> blackInfo : blacks.entrySet()) {
            chessDao.createBoard(new TeamInfoDto(TeamConstants.BLACK, blackInfo, roomId));
        }
    }

    public ChessGameDto loadPreviousGame() {
        TurnDto turnDto = chessDao.selectTurnByRoomId(1);
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(1);

        final ChessGame chessGame = covertToChessGame(turnDto, boardInfos);

        return new ChessGameDto(chessGame);
    }


    @Transactional
    public ChessGameDto move(MoveRequestDto moveRequestDto) {
        TurnDto turnDto = chessDao.selectTurnByRoomId(1);
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(1);

        final ChessGame chessGame = covertToChessGame(turnDto, boardInfos);

        String startPosition = moveRequestDto.getStart();
        String destPosition = moveRequestDto.getDestination();
        chessGame.move(Position.of(startPosition), Position.of(destPosition));

        chessDao.deleteBoardByRoomId(1);
        chessDao.changeTurnByRoomId(TeamConstants.convert(chessGame.isWhiteTeamTurn()), chessGame.isPlaying(), 1);
        insertBoardInfos(chessGame, 1);

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
