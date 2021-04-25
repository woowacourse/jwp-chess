package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.webdao.ChessDao;
import chess.webdto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SpringChessService {
    private final ChessDao chessDao;

    public SpringChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }


    @Transactional
    public ChessGameDto startNewGame() {
        chessDao.deleteMovesByRoomId(1);
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        return new ChessGameDto(chessGame);
    }


    public ChessGameDto loadPreviousGame() {
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());

        List<MoveRequestDto> moves = chessDao.selectAllMovesByRoomId(1);

        for (int i = 0; i < moves.size(); i++) {
            String startPosition = moves.get(i).getStart();
            String destPosition = moves.get(i).getDestination();
            chessGame.move(Position.of(startPosition), Position.of(destPosition));
        }

        return new ChessGameDto(chessGame);
    }


    @Transactional
    public ChessGameDto move(final String start, final String destination) {
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());

        chessDao.insertMove(start, destination);
        List<MoveRequestDto> moves = chessDao.selectAllMovesByRoomId(1);

        for (int i = 0; i < moves.size(); i++) {
            String startPosition = moves.get(i).getStart();
            String destPosition = moves.get(i).getDestination();
            chessGame.move(Position.of(startPosition), Position.of(destPosition));
        }

        return new ChessGameDto(chessGame);
    }

}
