package wooteco.chess.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class PieceInfoRepositoryTest {

    @Autowired
    private PieceInfoRepository pieceInfoRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;

    @DisplayName("체스 말 정보 저장")
    @Test
    void save() {
        RoomInfo roomInfo = roomInfoRepository.save(new RoomInfo("white", false));
        pieceInfoRepository.save(new PieceInfo("whiterook", "a1", roomInfo.getRoomNumber()));
//        pieceInfoRepository.save(new PieceInfo("whitePawn", "a2", roomInfo.getRoomNumber()));
//        pieceInfoRepository.save(new PieceInfo("whitePawn", "a3", roomInfo.getRoomNumber()));
//        pieceInfoRepository.save(new PieceInfo("whitePawn", "a4", roomInfo.getRoomNumber()));
//        pieceInfoRepository.save(new PieceInfo("whitePawn", "a5", roomInfo.getRoomNumber()));
//        pieceInfoRepository.findAll();
//        RoomInfo a = roomInfoRepository
//                .findById(Long.valueOf(1))
//                .orElseThrow(RuntimeException::new);
//        System.out.println(a.getPosition() + " <<<");
//        List<PieceInfo> chessPieces = StreamSupport.stream(pieceInfoRepository
//                .findAll()
//                .spliterator(), false)
//                .filter(chessPiece -> chessPiece.getRoomInfoRef() == roomInfo.getId())
//                .collect(Collectors.toList());
//
//        assertThat(chessPieces.size()).isEqualTo(64);
    }
}
