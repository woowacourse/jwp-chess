package spring.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import spring.entity.repository.ChessGameRepository;

// TODO : Test 어노테이션 공부하자~
@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class ChessServiceTest {

    @Autowired
    public ChessGameRepository chessGameRepository;

}