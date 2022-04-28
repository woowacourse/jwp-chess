const X_AXES = ["a", "b", "c", "d", "e", "f", "g", "h"];
const Y_AXES = ["8", "7", "6", "5", "4", "3", "2", "1"];

const getColorName = (pieceColor) => {
    return pieceColor === "WHITE" ? "백" : "흑";
}

const Cell = window.styled.div`
    display: inline-flex;
    justify-content: center;
    align-items: center;
    background-color: ${props => props.alt ? "#D38948" : "#FFCDA1"};
    width: 80px;
    height: 80px;
    cursor: pointer;
    transition: all 0.2s;
    
    &:hover {
        transform: scale(1.05);
        box-shadow: 0 0 5px rgba(0, 0, 0, .5);
        z-index: 999;
    }
`;

const CellWrapper = window.styled.div`
    display: grid;
    grid-template-columns: repeat(8, 80px);
    grid-template-rows: repeat(8, 80px);
`;

const Board = ({id, selected, onCellClick}) => {
    const [board, setBoard] = React.useState();

    React.useEffect(() => {
        fetchBoard(id).then(board => setBoard(board));
    }, [selected]);

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

const CurrentTurn = ({turn}) => {
    return (
        <div> 현재 차례 : {getColorName(turn)} </div>
    );
}

const Score = ({score}) => {
    return (
        <React.Fragment>
            <div> 백: {score.whiteScore} </div>
            <div> 흑: {score.blackScore} </div>
        </React.Fragment>
    );
}

const Winner = ({winner}) => {
    if (!winner) return null;

    return (
        <div> 승자는 {getColorName(winner)} </div>
    );
}

const Game = () => {
    const {id} = parseQueryString();
    const [selected, setSelected] = React.useState();

    const [score, setScore] = React.useState({
        whiteScore: 0,
        blackScore: 0
    });

    const [winner, setWinner] = React.useState();

    const [turn, setTurn] = React.useState();

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

    return (
        <React.Fragment>
            <Board id={id} selected={selected} onCellClick={handleCellClick}/>
            <CurrentTurn id={id} turn={turn}/>
            <Score id={id} score={score}/>
            <Winner id={id} winner={winner}/>
        </React.Fragment>
    );
};

ReactDOM.render(<Game/>, document.querySelector('#root'));