package chess.service;

import chess.dao.PieceDao;
import chess.dao.exceptions.DaoNoneSelectedException;
import chess.domain.coordinate.Coordinate;
import chess.domain.pieces.Piece;
import chess.domain.pieces.PieceType;
import chess.domain.pieces.Pieces;
import chess.domain.pieces.StartPieces;
import chess.domain.state.State;
import chess.domain.state.StateType;
import chess.domain.team.Team;
import chess.dto.PieceDto;
import chess.dto.StateDto;
import chess.repository.AnnouncementRepository;
import chess.repository.StateRepository;
import chess.repository.StatusRecordRepository;
import chess.view.Announcement;
import chess.view.BoardToHtml;
import chess.view.board.Board;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChessRoomService {
	private final PieceDao pieceDao;
	private final AnnouncementRepository announcementRepository;
	private final StatusRecordRepository statusRecordRepository;
	private final StateRepository stateRepository;

	public ChessRoomService(final PieceDao pieceDao
			, final AnnouncementRepository announcementRepository, final StatusRecordRepository statusRecordRepository, final StateRepository stateRepository) {
		this.pieceDao = pieceDao;
		this.announcementRepository = announcementRepository;
		this.statusRecordRepository = statusRecordRepository;
		this.stateRepository = stateRepository;
	}

	public String loadBoardHtml(final int roomId) throws SQLException {
		final State state = loadState(roomId);

		return createBoardHtml(state);
	}

	private Set<Piece> createPieceSet(final List<PieceDto> pieceDtos) {
		return pieceDtos.stream()
				.map(daoPieceDto -> PieceType.getFactoryOfName(daoPieceDto.getPieceType()).apply(
						Team.of(daoPieceDto.getTeam()), Coordinate.of(daoPieceDto.getCoordinate())))
				.collect(Collectors.toSet());
	}

	private State loadState(final int roomId) throws SQLException {
		final StateDto stateDto = stateRepository.findByRoomId(roomId).orElseThrow(DaoNoneSelectedException::new);
		final List<PieceDto> pieceDtos = pieceDao.findPiecesByRoomId(roomId);

		final Set<Piece> pieces = createPieceSet(pieceDtos);
		return StateType.getFactory(stateDto.getState()).apply(
				new Pieces(pieces));
	}

	private String createBoardHtml(final State state) {
		final List<List<String>> board = Board.of(state.getSet()).getLists();
		return BoardToHtml.of(board).getHtml();
	}

	public void saveNewState(final int roomId) {
		stateRepository.save("ended", roomId);
	}

	public void saveNewPieces(final int roomId) throws SQLException {
		final Set<Piece> pieces = new StartPieces().getInstance();
		for (Piece piece : pieces) {
			pieceDao.addPiece(piece.getPieceTypeName(), piece.getTeamName(),
					piece.getCoordinateRepresentation(), roomId);
		}
	}

	public void updateRoom(final int roomId, final String command) throws SQLException {
		final State before = loadState(roomId);
		final State after = before.pushCommand(command);
		stateRepository.setByRoomId(after.getStateName(), roomId);
		pieceDao.deletePieces(roomId);
		savePiecesFromState(roomId, after);
		saveStatusRecordIfEnded(roomId, after);
		announcementRepository.setByRoomId(roomId, createRightAnnouncement(after));
	}

	private void savePiecesFromState(final int roomId, final State after) throws SQLException {
		for (final Piece piece : after.getSet()) {
			pieceDao.addPiece(piece.getPieceTypeName(), piece.getTeamName(),
					piece.getCoordinateRepresentation(), roomId);
		}
	}

	private void saveStatusRecordIfEnded(final int roomId, final State after) {
		if (after.isEnded()) {
			statusRecordRepository.save(Announcement.ofStatus(after.getPieces()).getString(), roomId);
		}
	}

	private static String createRightAnnouncement(State state) {
		if (state.isPlaying()) {
			return Announcement.ofPlaying().getString();
		}
		if (state.isReported()) {
			return Announcement.ofStatus(state.getPieces()).getString();
		}
		return Announcement.ofEnd().getString();
	}

	public String loadAnnouncementMessage(int roomId) {
		return announcementRepository.findByRoomId(roomId).orElseThrow(DaoNoneSelectedException::new)
				.getMessage();
	}

	public void saveNewAnnouncementMessage(int roomId) {
		announcementRepository.save(Announcement.ofFirst().getString(), roomId);
	}

	public void saveAnnouncementMessage(final int roomId, final String message) {
		announcementRepository.setByRoomId(roomId, message);
	}
}
