package chess.service;

import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardRequestDto;
import chess.dto.PieceResponseDto;
import chess.dto.PiecesResponseDto;
import chess.dto.ScoreResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    public PiecesResponseDto getPieces(ChessGame chessGame) {
        List<PieceResponseDto> alivePieces = new ArrayList<>();
        for(Entry<Position, Piece> entry: chessGame.pieces().entrySet()) {
            if(entry.getValue().isEmpty()) {
                continue;
            }
            alivePieces.add(new PieceResponseDto(entry.getKey(), entry.getValue()));
        }
        return new PiecesResponseDto(alivePieces);
    }

    public PiecesResponseDto putBoard(BoardRequestDto boardRequestDto, ChessGame chessGame) {
        chessGame.move(new Position(boardRequestDto.getSource()), new Position(boardRequestDto.getTarget()));
        return getPieces(chessGame);
    }

    public ScoreResponseDto getScore(String colorName, ChessGame chessGame) {
        return new ScoreResponseDto(chessGame.score(Color.valueOf(colorName)));
    }
}
