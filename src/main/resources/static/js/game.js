// draggable setting

const $squares = document.querySelectorAll("td[class=chessboard]");

for (let i = 0; i < $squares.length; i++) {
    $squares[i].addEventListener("dragstart", dragstart_handler)
    $squares[i].addEventListener("dragover", dragover_handler)
    $squares[i].addEventListener("drop", drop_handler)
}

function dragstart_handler(event) {
    event.dataTransfer.setData("text/plain", event.target.parentNode.id);
    event.dataTransfer.dropEffect = "move";
}

function dragover_handler(event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
}

function drop_handler(event) {
    event.preventDefault()
    const source = event.dataTransfer.getData("text/plain");
    const target = event.target.parentNode.id;

    const moveCommand = "move " + source + " " + target;
    sendMoveRequest(moveCommand)
}

// move, save, exit setting

const $move = document.querySelector('input[class="move"]')
const $save = document.querySelector('input[value="save"]')

$move.addEventListener('keyup', movePiece);
$save.addEventListener('click', saveGame);


function movePiece(event) {
    const moveCommand = event.target.value;
    if (event.key === "Enter" && moveCommand !== "") {
        event.target.value = "";
        const trimmedMoveCommand = moveCommand
            .replace(/\s+/g, ' ')
            .trim()
        sendMoveRequest(trimmedMoveCommand)
    }
}

async function sendMoveRequest(trimmedMoveCommand) {

    const data = {
        command: trimmedMoveCommand,
        roomNo: document.querySelector('#room').firstElementChild.className
    };
    const sourcePosition = trimmedMoveCommand.split(" ")[1]
    const targetPosition = trimmedMoveCommand.split(" ")[2]
    let response = await fetch('/game/move', {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(data)
    });
    if (response.ok) {
        response = await response.text();
        replaceComponents(response, sourcePosition, targetPosition)
    } else {
        alert(sourcePosition + "에서" + targetPosition + "으로 움직일 수 없습니다!")
    }
}

function replaceComponents(dom, sourcePosition, targetPosition) {
    let parser = new DOMParser();
    let xmlDoc = parser.parseFromString(dom, "text/html");

    let source = xmlDoc.querySelector("#" + sourcePosition);
    let target = xmlDoc.querySelector("#" + targetPosition);
    let turn = xmlDoc.querySelector("#turn")
    let score = xmlDoc.querySelector("#score")

    document.querySelector("#" + sourcePosition).innerHTML = source.innerHTML
    document.querySelector("#" + targetPosition).innerHTML = target.innerHTML
    document.querySelector("#turn").innerHTML = turn.innerHTML
    document.querySelector("#score").innerHTML = score.innerHTML

    const result = xmlDoc.querySelector("#result").textContent
    if (result !== "") {
        alert("black: " + result[0] + " / white : " + result[1])
        window.location.replace("/")
    }
}

async function saveGame() {
    const params = {
        name: document.querySelector('#room').firstElementChild.nextElementSibling.className,
        turn: document.querySelector('#turn').firstElementChild.className,
        state: createStateJson(),
    };
    let response = await fetch('/game/save', {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(params)
    });
    if (!response.ok) {
       alert('저장할 수 없습니다.');
    }
}

function createStateJson() {
    const status = document.querySelectorAll('td')
    let state = {};
    for (let i = 0; i < status.length; i++) {
        const position = status[i].id;
        const img = status[i].querySelector('img');
        const color = img.id.split("_")[0]
        const type = img.id.split("_")[1]
        state[position] = {
            color: color,
            type: type,
        }
    }
    return state
}