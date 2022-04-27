const gameUri = window.location.href;
let source = "";
let target = "";

async function displayBoard() {
    Array.from(pieces).forEach(
        function (element) {
            let position = document.getElementById(element.position);
            if (position.hasChildNodes()) {
                position.removeChild(document.getElementById("piece_img"));
            }
            const imgTeg = document.createElement("img");
            imgTeg.setAttribute("id", "piece_img");
            const imgPath = `/images/${element.color}_${element.name}.png`;
            imgTeg.setAttribute("src", imgPath);
            document.getElementById(element.position).appendChild(imgTeg);
        }
    )
}

async function promotionButton() {
    const promotionPiece = document.getElementById("promotion").value;
    const promotion = {
        promotionValue: promotionPiece
    }

    await fetch(gameUri + "/promotion", {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(promotion)
    }).then(response => handlingException(response))
        .then(reload)
        .catch(error => {
            alert(error.message);
        });
    document.getElementById("promotion").value = "";
}

async function clickMove(id) {
    if (source === "") {
        source = id;
        document.getElementById(source).style.backgroundColor = 'yellow';
        return;
    }
    if (source === id) {
        document.getElementById(source).style.backgroundColor = '';
        source = "";
        return;
    }
    target = id;
    console.log(target)
    document.getElementById(source).style.backgroundColor = '';
    await moveButton()
        .then(resetPosition);
}

async function moveButton() {
    const move = {
        source: source,
        target: target
    }
    fetch(gameUri + "/move", {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(move)
    }).then(response => handlingException(response))
        .then(checkEndGame)
        .then(reload)
        .catch(error => {
            resetPosition();
            alert(error.message);
        });
}

function resetPosition() {
    source = "";
    target = "";
}

async function reload() {
    location.reload();
}

async function checkEndGame() {
    await fetch(gameUri + "/status")
        .then(response => handlingException(response))
        .then(response => response.json())
        .then(response => displayWinner(response))
        .catch(error => {
            alert(error.message);
        });
}

function displayWinner(response) {
    if (response.end) {
        fetch(gameUri + "/winner")
            .then(response => handlingException(response))
            .then(response => response.json())
            .then(response => alert(`우승자는 ${response.winner}입니다.`))
            .catch(error => {
                alert(error.message);
            });
    }
}

async function scoreButton() {
    const value = await fetch(gameUri + "/score")
        .then(response => handlingException(response))
        .catch(error => {
            alert(error.message);
        });
    alert(`${value[0].color}의 점수는 ${value[0].score}\n${value[1].color}의 점수는 ${value[1].score}`);
}

async function handlingException(response) {
    if (response.ok) {
        return response;
    }
    if (response.status === 404) {
        throw Error("게임이 존재하지 않아 찾을 수 없습니다.");
    }
    const error = await response.json();
    throw Error(error.message);
}
