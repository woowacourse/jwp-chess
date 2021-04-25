package chess.service;

import chess.webdto.converter.DaoToChessGame;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.PieceDto;
import chess.webdto.dao.TurnDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import chess.webdao.ChessDao;
import chess.webdto.*;
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
        chessDao.createRoom(WordConstants.convert(chessGame.isWhiteTeamTurn()), chessGame.isPlaying());
        long roomId = 1;
        insertBoardInfos(chessGame, roomId);
        return new ChessGameDto(chessGame);
    }

    private void insertBoardInfos(ChessGame chessGame, long roomId) {
        Map<Position, Piece> whites = chessGame.currentWhitePiecePosition();
        for (Map.Entry<Position, Piece> whiteInfo : whites.entrySet()) {
            // todo: dto로 넘기기~~
            chessDao.createBoard(WordConstants.WHITE, whiteInfo.getKey().getPositionInitial(),
                    convert(whiteInfo.getValue()), whiteInfo.getValue().isFirstMove(), roomId);
        }

        Map<Position, Piece> blacks = chessGame.currentBlackPiecePosition();
        for (Map.Entry<Position, Piece> blackInfo : blacks.entrySet()) {
            chessDao.createBoard(WordConstants.BLACK, blackInfo.getKey().getPositionInitial(), convert(blackInfo.getValue()), blackInfo.getValue().isFirstMove(), roomId);
        }

    }

    private String convert(Piece value) {
        return PieceDto.convert(value);
    }

    public ChessGameDto loadPreviousGame() {
        // 턴 가져오기
        TurnDto turnDto = chessDao.selectTurnByRoomId(1);
        // 기존정보 가져오기
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(1);

        // 기존 정보로 체스보드 만들기
        final ChessGame chessGame = new DaoToChessGame(turnDto, boardInfos).covertToChessGame();


        return new ChessGameDto(chessGame);
    }


    @Transactional
    public ChessGameDto move(MoveRequestDto moveRequestDto) {
        // 턴 가져오기
        TurnDto turnDto = chessDao.selectTurnByRoomId(1);
        // 기존정보 가져오기
        List<BoardInfosDto> boardInfos = chessDao.selectBoardInfosByRoomId(1);

        // 기존 정보로 체스보드 만들기
        final ChessGame chessGame = new DaoToChessGame(turnDto, boardInfos).covertToChessGame();

        // 기존정보에 움직이기
        String startPosition = moveRequestDto.getStart();
        String destPosition = moveRequestDto.getDestination();
        chessGame.move(Position.of(startPosition), Position.of(destPosition));

        // 기존정보 삭제하기
        chessDao.deleteBoardByRoomId(1);
        // 새로운 정보 넣어주기
        chessDao.changeTurnByRoomId(WordConstants.convert(chessGame.isWhiteTeamTurn()), chessGame.isPlaying(), 1);
        insertBoardInfos(chessGame, 1);
        return new ChessGameDto(chessGame);
    }


}
