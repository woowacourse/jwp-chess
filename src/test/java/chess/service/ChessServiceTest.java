package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomDeleteRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.PieceResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.entity.PieceEntity;
import chess.entity.RoomEntity;
import chess.exception.IllegalCommandException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ChessServiceTest {

    @InjectMocks
    private ChessService chessService;

    @Mock
    private RoomDao roomDao;

    @Mock
    private PieceDao pieceDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("방을 생성하고 기물들을 초기화한다.")
    void createRoom() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity(id, "다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final ChessBoard chessBoard = ChessBoardFactory.createChessBoard();
        final Map<Position, ChessPiece> pieces = chessBoard.findAllPiece();
        final RoomRequestDto roomRequestDto = new RoomRequestDto("다 드루와", "1234");

        // mocking
        given(roomDao.save(any())).willReturn(room);

        // when
        final RoomResponseDto roomResponseDto = chessService.createRoom(roomRequestDto);

        // then
        assertThat(roomResponseDto.getName()).isEqualTo(room.getName());
        verify(pieceDao).saveAllByRoomId(id, pieces);
    }

    @Test
    @DisplayName("모든 방 정보를 불러온다.")
    void loadAllRoom() {
        // given
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final List<RoomEntity> rooms = new ArrayList<>();
        rooms.add(room);

        // mocking
        given(roomDao.findAll()).willReturn(rooms);

        // when
        final List<RoomResponseDto> loadedRooms = chessService.loadAllRoom();

        // then
        assertThat(loadedRooms).hasSize(1);
        assertThat(loadedRooms.get(0).getName()).isEqualTo(room.getName());
    }

    @Test
    @DisplayName("방 정보를 불러온다.")
    void loadRoom() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());

        // mocking
        given(roomDao.findById(id)).willReturn(room);

        // when
        final RoomResponseDto loadedRoom = chessService.loadRoom(id);

        // then
        assertThat(loadedRoom.getName()).isEqualTo(room.getName());
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.END.getValue(),
                Color.WHITE.getValue());
        final RoomDeleteRequestDto roomDeleteRequestDto = new RoomDeleteRequestDto("1234");

        // mocking
        given(roomDao.findById(id)).willReturn(room);
        given(passwordEncoder.matches(room.getPassword(), roomDeleteRequestDto.getPassword())).willReturn(true);

        // when
        chessService.deleteRoom(id, roomDeleteRequestDto);

        // then
        verify(roomDao).deleteById(id);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는 경우 방을 삭제할 수 없다.")
    void deleteRoomException1() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.END.getValue(),
                Color.WHITE.getValue());
        final RoomDeleteRequestDto roomDeleteRequestDto = new RoomDeleteRequestDto("12345");

        // mocking
        given(roomDao.findById(id)).willReturn(room);
        // when & then
        assertThatThrownBy(() -> chessService.deleteRoom(id, roomDeleteRequestDto))
                .isInstanceOf(IllegalCommandException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("게임이 진행 중인 경우 방을 삭제할 수 없다.")
    void deleteRoomException2() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity(id, "다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final RoomDeleteRequestDto roomDeleteRequestDto = new RoomDeleteRequestDto("1234");

        // mocking
        given(roomDao.findById(id)).willReturn(room);
        given(passwordEncoder.matches(room.getPassword(), roomDeleteRequestDto.getPassword())).willReturn(true);

        System.out.println("1" + passwordEncoder.encode("1234"));

        // when & then
        assertThatThrownBy(() -> chessService.deleteRoom(id, roomDeleteRequestDto))
                .isInstanceOf(IllegalCommandException.class)
                .hasMessage("게임이 진행중인 방은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("방의 모든 기물 정보를 불러온다.")
    void loadAllPiece() {
        // given
        final long id = 1L;
        final List<PieceEntity> pieces = ChessBoardFactory.createChessBoard()
                .findAllPiece()
                .entrySet()
                .stream()
                .map(entry -> new PieceEntity(id, entry.getKey().getValue(),
                        ChessPieceMapper.toPieceType(entry.getValue()), entry.getValue().color().getValue()))
                .collect(Collectors.toList());

        // mocking
        given(roomDao.isExist(id)).willReturn(true);
        given(pieceDao.findAllByRoomId(id)).willReturn(pieces);

        // when
        final List<PieceResponseDto> loadedPieces = chessService.loadAllPiece(id);

        // then
        assertThat(loadedPieces).hasSize(32);
    }

    @Test
    @DisplayName("기물을 움직인다.")
    void movePiece() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final List<PieceEntity> pieces = ChessBoardFactory.createChessBoard()
                .findAllPiece()
                .entrySet()
                .stream()
                .map(entry -> new PieceEntity(id, entry.getKey().getValue(),
                        ChessPieceMapper.toPieceType(entry.getValue()), entry.getValue().color().getValue()))
                .collect(Collectors.toList());
        final MoveRequestDto moveRequestDto = new MoveRequestDto("b2", "b4");

        // mocking
        given(roomDao.isExist(id)).willReturn(true);
        given(roomDao.findById(id)).willReturn(room);
        given(pieceDao.findAllByRoomId(id)).willReturn(pieces);

        // when
        chessService.movePiece(id, moveRequestDto);

        // then
        verify(pieceDao).deleteByRoomIdAndPosition(id, moveRequestDto.getTo().getValue());
        verify(pieceDao).updatePositionByRoomId(id, moveRequestDto.getFrom().getValue(),
                moveRequestDto.getTo().getValue());
        verify(roomDao).updateStatusById(id, GameStatus.PLAYING.getValue());
        verify(roomDao).updateTurnById(id, Color.BLACK.getValue());
    }

    @Test
    @DisplayName("현재 정수를 불러온다.")
    void loadScore() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final List<PieceEntity> pieces = ChessBoardFactory.createChessBoard()
                .findAllPiece()
                .entrySet()
                .stream()
                .map(entry -> new PieceEntity(id, entry.getKey().getValue(),
                        ChessPieceMapper.toPieceType(entry.getValue()), entry.getValue().color().getValue()))
                .collect(Collectors.toList());

        // mocking
        given(roomDao.isExist(id)).willReturn(true);
        given(roomDao.findById(id)).willReturn(room);
        given(pieceDao.findAllByRoomId(id)).willReturn(pieces);

        // when
        final ScoreResponseDto scoreResponseDto = chessService.loadScore(id);

        // then
        assertAll(
                () -> assertThat(scoreResponseDto.getWhiteScore()).isEqualTo(38),
                () -> assertThat(scoreResponseDto.getBlackScore()).isEqualTo(38)
        );
    }

    @Test
    @DisplayName("게임을 종료한다.")
    void end() {
        // given
        final long id = 1L;
        final RoomEntity room = new RoomEntity("다 드루와", "1234", GameStatus.PLAYING.getValue(),
                Color.WHITE.getValue());
        final List<PieceEntity> pieces = ChessBoardFactory.createChessBoard()
                .findAllPiece()
                .entrySet()
                .stream()
                .map(entry -> new PieceEntity(id, entry.getKey().getValue(),
                        ChessPieceMapper.toPieceType(entry.getValue()), entry.getValue().color().getValue()))
                .collect(Collectors.toList());

        // mocking
        given(roomDao.isExist(id)).willReturn(true);
        given(roomDao.findById(id)).willReturn(room);
        given(pieceDao.findAllByRoomId(id)).willReturn(pieces);

        // when
        chessService.end(id);

        // then
        verify(roomDao).updateStatusById(id, GameStatus.END.getValue());
    }
}
