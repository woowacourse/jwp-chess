const App = function App() {
    const gameId = document.querySelector("#gameId");
    const originalPosition = document.querySelector("#originalPosition");
    const targetIdPosition = document.querySelector("#targetPosition");
    const turn = document.querySelector("#now-turn");
    const whiteScore = document.querySelector("#score-white");
    const blackScore = document.querySelector("#score-black");

    const chessBoard = document.querySelector(".chessboard");

    chessBoard.addEventListener('click', (event) => moveEventListener(event));

    function moveEventListener(event) {
        const target = event.target;
        //게임이 진행중이지 않은경우
        if (!gameId.value) {
            return false;
        }
        // 클릭에 따른 아이디를 저장한다
        console.log(target);
        if (isNotCell(target.className)) {
            return false;
        }

        const position = target.id;
        if (isEmpty(originalPosition.value)) {
            originalPosition.value = position;
        } else {
            if (originalPosition.value === position) {
                originalPosition.value = "";
            } else {
                targetIdPosition.value = position;
            }
        }

        if (isEmpty(originalPosition.value) || isEmpty(targetIdPosition.value)) {
            return false;
        }

        const moveRequest = {};
        moveRequest.gameId = gameId.value;
        moveRequest.command = "move " + originalPosition.value + " " + targetIdPosition.value;

        fetch("http://localhost:8080/move", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(moveRequest)
        })
            .then(resolver)
            .then(render)
            .catch(err => alert(err.body))
            .finally(() => {
                originalPosition.value = "";
                targetIdPosition.value = "";
            })

    }

    function isEmpty(value) {
        return !value || value === "";
    }

    function isNotCell(className) {
        return !(className === "black" || className === "white");
    }

    function resolver(response) {
        return new Promise((resolve, reject) => {
            let func;
            response.status < 400 ? func = resolve : func = reject;
            response.json().then(data => func({
                "status": response.status,
                "body": data
            }));
        });
    }

    function render(data) {
        const originalCell = document.querySelector("#" + originalPosition.value);
        const targetCell = document.querySelector("#" + targetIdPosition.value);
        originalCell.innerHTML = "";
        targetCell.innerHTML = data.body.piece;
        turn.innerHTML = data.body.turn;
        whiteScore.innerHTML = data.body.score.white;
        blackScore.innerHTML = data.body.score.black;
    }
};

new App();