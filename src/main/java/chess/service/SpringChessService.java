package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.webdao.ChessDao;
import chess.webdto.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chess.webdto.TeamDto.BLACK_TEAM;
import static chess.webdto.TeamDto.WHITE_TEAM;

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
        return generateChessGameDto(chessGame);
    }


    public ChessGameDto loadPreviousGame() {
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());

        List<MoveRequestDto> moves = chessDao.selectAllMovesByRoomId(1);

        for (int i = 0; i < moves.size(); i++) {
            String startPosition = moves.get(i).getStart();
            String destPosition = moves.get(i).getDestination();
            chessGame.move(Position.of(startPosition), Position.of(destPosition));
        }

        return generateChessGameDto(chessGame);
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

        return generateChessGameDto(chessGame);
    }


    //todo: 로직정리 json 형식 정리하는 방법 생각...
    private ChessGameDto generateChessGameDto(final ChessGame chessGame) {
        final TeamPiecesDto piecePositionToString
                = new TeamPiecesDto(
                generatePiecePositionByTeamToString(chessGame.currentWhitePiecePosition()),
                generatePiecePositionByTeamToString(chessGame.currentBlackPiecePosition())
        );
        final String currentTurnTeam = currentTurnTeamToString(chessGame);
        final ScoreDto teamScore = new ScoreDto(chessGame.calculateWhiteTeamScore(), chessGame.calculateBlackTeamScore());
        final boolean isPlaying = chessGame.isPlaying();

        return new ChessGameDto(piecePositionToString, currentTurnTeam, teamScore, isPlaying);
    }

    private Map<String, String> generatePiecePositionByTeamToString(final Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        for (Position position : piecePosition.keySet()) {
            final String positionInitial = position.getPositionInitial();
            final Piece chosenPiece = piecePosition.get(position);
            final String pieceString = PieceDto.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceString);
        }
        return piecePositionConverted;
    }

    private String currentTurnTeamToString(final ChessGame chessGame) {
        if (chessGame.isWhiteTeamTurn()) {
            return WHITE_TEAM.team();
        }
        return BLACK_TEAM.team();
    }
}
