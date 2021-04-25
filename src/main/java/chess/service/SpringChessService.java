package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.webdao.ChessDao;
import chess.webdto.converter.DaoToChessGame;
import chess.webdto.converter.TeamConstants;
import chess.webdto.converter.TeamInfoToDto;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TurnDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SpringChessService {
    private final ChessDao chessDao;

    public SpringChessService(ChessDao chessDao) {
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
            chessDao.createBoard(new TeamInfoToDto(TeamConstants.WHITE, whiteInfo, roomId).convertToTeamInfoDto());
        }

        Map<Position, Piece> blacks = chessGame.currentBlackPiecePosition();
        for (Map.Entry<Position, Piece> blackInfo : blacks.entrySet()) {
            chessDao.createBoard(new TeamInfoToDto(TeamConstants.BLACK, blackInfo, roomId).convertToTeamInfoDto());
        }
    }

    public ChessGameDto loadPreviousGame() {
        TurnDto turnDto = chessDao.selectTurnByRoomId(1);
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(1);

        final ChessGame chessGame = new DaoToChessGame(turnDto, boardInfos).covertToChessGame();

        return new ChessGameDto(chessGame);
    }


    @Transactional
    public ChessGameDto move(MoveRequestDto moveRequestDto) {
        TurnDto turnDto = chessDao.selectTurnByRoomId(1);
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(1);

        final ChessGame chessGame = new DaoToChessGame(turnDto, boardInfos).covertToChessGame();
        String startPosition = moveRequestDto.getStart();
        String destPosition = moveRequestDto.getDestination();
        chessGame.move(Position.of(startPosition), Position.of(destPosition));

        chessDao.deleteBoardByRoomId(1);
        chessDao.changeTurnByRoomId(TeamConstants.convert(chessGame.isWhiteTeamTurn()), chessGame.isPlaying(), 1);
        insertBoardInfos(chessGame, 1);

        return new ChessGameDto(chessGame);
    }


}
