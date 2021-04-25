package chess.webdto.converter;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PiecePositions;
import chess.domain.team.Team;
import chess.webdto.dao.BoardInfosDto;
import chess.webdto.dao.TurnDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoToChessGame {
    private TurnDto turnDto;
    private List<BoardInfosDto> boardInfos;

    public DaoToChessGame(TurnDto turnDto, List<BoardInfosDto> boardInfos) {
        this.turnDto = turnDto;
        this.boardInfos = boardInfos;
    }

    public ChessGame covertToChessGame(){
        Map<Position, Piece> whites = new HashMap<>();
        Map<Position, Piece> blacks = new HashMap<>();
        for (BoardInfosDto boardInfo : boardInfos) {
            if (boardInfo.getTeam().equals("white")) {
                whites.put(Position.of(boardInfo.getPosition()), DaoToPiece.generatePiece(boardInfo.getTeam(), boardInfo.getPiece(), boardInfo.getIsFirstMoved()));
            }

            if (boardInfo.getTeam().equals("black")) {
                blacks.put(Position.of(boardInfo.getPosition()), DaoToPiece.generatePiece(boardInfo.getTeam(), boardInfo.getPiece(), boardInfo.getIsFirstMoved()));
            }
        }
        PiecePositions whitePiecePosition = new PiecePositions(whites);
        PiecePositions blackPiecePosition = new PiecePositions(blacks);

        Team blackTeam = new Team(blackPiecePosition);
        Team whiteTeam = new Team(whitePiecePosition);

        return new ChessGame(blackTeam, whiteTeam, currentTurn(blackTeam, whiteTeam, turnDto.getTurn()), turnDto.getIsPlaying());
    }

    private Team currentTurn(Team black, Team white, String currentTurnTeam) {
        if (currentTurnTeam.equals("white")) {
            return white;
        }
        return black;
    }

}
