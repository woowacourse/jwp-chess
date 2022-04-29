const X_AXES = ["a", "b", "c", "d", "e", "f", "g", "h"];
const Y_AXES = ["8", "7", "6", "5", "4", "3", "2", "1"];

const getColorName = (pieceColor) => {
    return pieceColor === "WHITE" ? "백" : "흑";
}

const GamePageWrapper = styled.div`
    margin: 0 auto;
    margin-top: 100px;
    max-width: 600px;
`

const Cell = window.styled.div`
    display: inline-flex;
    justify-content: center;
    align-items: center;
    background-color: ${props => props.alt ? "#D38948" : "#FFCDA1"};
    cursor: pointer;
    aspect-ratio: 1 / 1;
    transition: all 0.2s;
    
    &:hover {
        transform: scale(1.05);
        box-shadow: 0 0 5px rgba(0, 0, 0, .5);
        z-index: 999;
    }
`;

const CellWrapper = window.styled.div`
    display: grid;
    grid-template-columns: repeat(8, 1fr);
    grid-template-rows: repeat(8, 1fr);
`;

const Board = ({board, onCellClick}) => {
    if (!board) return null;

    return (
        <CellWrapper>
            {
                Y_AXES.map((yAxis, j) =>
                    X_AXES.map((xAxis, i) => {
                        const isOddRow = j % 2 !== 0;
                        const isOddCol = i % 2 !== 0;

                        const coordinate = xAxis + yAxis;
                        const pieceName = board[coordinate];

                        return (
                            <Cell key={coordinate} alt={isOddRow !== isOddCol} onClick={() => onCellClick(coordinate)}>
                                {pieceName && <img alt="chess-piece" src={`/images/${pieceName.toLowerCase()}.svg`}/>}
                            </Cell>
                        );
                    })
                )
            }
        </CellWrapper>
    );
}

const Status = styled.div`
    margin-top: 20px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 30px;
`

const CurrentTurn = ({turn}) => {
    return (
        <div> {getColorName(turn)}의 차례 </div>
    );
}

const Score = ({score}) => {
    return (
        <div>
            <div> 백: {score.whiteScore}점</div>
            <div> 흑: {score.blackScore}점</div>
        </div>
    );
}

const WinnerWrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.8);
    color: #ffffff;
    backdrop-filter: blur(5px);
    
    & > div:nth-child(1) {
        margin-bottom: 5px;
        font-size: 30px;
    }
    
    & > div:nth-child(2) {
        margin-bottom: 15px;
        font-size: 100px;
    }
    
    & > div:nth-child(3) {
        font-size: 30px;
    }
`;

const Winner = ({winner, score}) => {
    if (!winner) return null;

    return (
        <WinnerWrapper>
            <div>게임이 끝났습니다</div>
            <div>승자는 {getColorName(winner)}!</div>
            <div>백은 {score.whiteScore}점, 흑은 {score.blackScore}점</div>
        </WinnerWrapper>
    );
}

const Game = () => {
    const {id} = parseQueryString();
    const [selected, setSelected] = React.useState();
    const [board, setBoard] = React.useState();
    const [score, setScore] = React.useState({
        whiteScore: 0,
        blackScore: 0
    });
    const [winner, setWinner] = React.useState();
    const [turn, setTurn] = React.useState();

    React.useEffect(() => {
        fetchBoard(id)
            .then(board => setBoard(board))
            .catch(e => {
                alert(e.message);
                location.href = "/";
            });
    }, [selected]);

    React.useEffect(() => {
        fetchScore(id).then(score => {
            setScore({
                whiteScore: score.whiteScore,
                blackScore: score.blackScore
            })
        });
    }, [selected]);

    React.useEffect(() => {
        fetchWinner(id).then(winner => {
            setWinner(winner.pieceColor);
        });
    }, [selected]);

    React.useEffect(() => {
        fetchCurrentTurn(id).then(currentTurn => setTurn(currentTurn.pieceColor));
    }, [selected]);

    const handleCellClick = async (coordinate) => {
        if (!selected) {
            setSelected(coordinate);
            return;
        }

        try {
            await move(id, selected, coordinate);
        } catch (e) {
            alert(e.message);
        }

        setSelected(null);
    }

    const handleHomeClick = () => {
        location.href = "/";
    }

    return (
        <GamePageWrapper>
            <Board id={id} board={board} onCellClick={handleCellClick}/>

            <Status>
                <Score id={id} score={score}/>
                <CurrentTurn id={id} turn={turn}/>
            </Status>

            <Button onClick={handleHomeClick}> 홈으로 </Button>

            <Winner id={id} winner={winner} score={score}/>
        </GamePageWrapper>
    );
};

ReactDOM.render(<Game/>, document.querySelector('#root'));