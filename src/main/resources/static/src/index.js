// -------- init start ---------
function setUpIndex() {
    const createForm = document.getElementById("create_form");
    createForm.addEventListener("submit", e => {
        e.preventDefault();
        send("/create", {
            method: 'post',
            body: toJSON(createForm),
            headers: new Headers({'Content-Type': 'application/json'})
        }, relocate);
    })

    const rooms = document.getElementById("enter_button");
    rooms.addEventListener("click", e => {
        send("/rooms", {method: 'get'}, drawRooms);
    })

    // const enterForm = document.getElementById("enter_form");
    // enterForm.addEventListener("submit", e => {
    //     e.preventDefault();
    //     let roomName = new FormData(enterForm).get("room_name");
    //     send("/enter?room_name=" + roomName, {
    //         method: 'get'
    //     }, relocate);
    // })

    console.log("setupIndex done")
}

function setUpState(state, forms) {
    const clickable = 'clickable';
    const nonClickable = 'non-clickable';

    function toggle(formObject) {
        const object = formObject.getElementsByTagName('input')[0];
        console.log("toggling object = ", object);
        if (object.classList.contains(clickable)) {
            object.classList.replace(clickable, nonClickable);
        } else if (object.classList.contains(nonClickable)) {
            object.classList.replace(nonClickable, clickable);
        }
    }

    const chessBoard = document.getElementById('chess-board');
    chessBoard.className = "";
    for (let key in forms) {
        const inputButton = forms[key].getElementsByTagName('input')[0];
        inputButton.classList.add(nonClickable);
    }

    if (state === "READY") {
        chessBoard.classList.add(nonClickable);
        toggle(forms['start']);
    }
    if (state === "RUNNING") {
        chessBoard.classList.add(clickable);
        toggle(forms['status']);
        toggle(forms['end']);
    }
    if (state === "FINISHED") {
        chessBoard.classList.add(nonClickable);
    }

}

function setUpMain(state) {
    console.log('setting up main', state);

    const statusForm = document.getElementById("status_form");
    statusForm.addEventListener("submit", e => {
        e.preventDefault();
        const roomId = getLastPath();

        send("/status/" + roomId, {
            method: 'get'
        }, showStatus);
    });

    const startForm = document.getElementById("start_form");
    startForm.addEventListener("submit", e => {
        e.preventDefault();
        const roomId = getLastPath();
        send("/start/" + roomId, {
            method: 'PATCH',
            body: JSON.stringify({}),
            headers: new Headers({'Content-Type': 'application/json'})
        }, relocate);
    });

    const endForm = document.getElementById("end_form");
    endForm.addEventListener("submit", e => {
        e.preventDefault();
        const roomId = getLastPath();
        send("/end/" + roomId, {
            method: 'PATCH'
        }, relocate);
    });

    setUpState(state, {'status': statusForm, 'start': startForm, 'end': endForm});
    console.log("setup done")
}

// -------- init end ---------


// --------- draw start ---------


let source = null;
let destination = null;

function handleMoveCounter(td) {
    if (source === null) {
        source = td;
    } else if (destination === null) {
        destination = td;
        moveByClick(source, destination);
        source = null;
        destination = null;
    }
}

function moveByClick(source, destination) {
    source.classList.remove("selected");
    destination.classList.remove("selected");
    if (source.id === destination.id) {
        return false;
    }
    console.log('move by click called', source, destination);

    const roomId = getLastPath();
    send("/move/" + roomId, {
        method: 'PATCH',
        body: JSON.stringify({'source': source.id, 'destination': destination.id}),
        headers: new Headers({'Content-Type': 'application/json'})
    }, drawBoardByResponse);
}

function toAlphabet(index) {
    return String.fromCharCode(96 + index);
}

function loadBoardBackground() {
    let isBlack = false;
    const board = document.getElementById("chess-board");
    for (var vertical = 8; vertical >= 1; vertical--) {
        const tr = document.createElement("tr");
        for (let horizontal = 1; horizontal <= 8; horizontal++) {
            isBlack = toggle(isBlack);
            const td = document.createElement("td");
            td.id = `${toAlphabet(horizontal)}${vertical}`
            td.addEventListener("click", e => {
                if (td.classList.contains('selected')) {
                    td.classList.remove('selected');
                } else {
                    td.classList.add('selected');
                }
                handleMoveCounter(td)
            })
            if (isBlack) {
                td.classList.add("black");
            } else {
                td.classList.add("white");
            }
            tr.appendChild(td);
        }
        isBlack = toggle(isBlack);
        board.appendChild(tr);
    }
}

function toggle(isBlack) {
    return !isBlack;
}

function drawTurn(color) {
    const turnText = document.getElementById("turn");
    turnText.textContent = color + " TURN";
}

function drawBoardByResponse(responseJson) {
    drawBoard(responseJson['board']);
    drawTurn(responseJson['color']);
    if (responseJson['state'] === "FINISHED") {
        alert("킹이 잡혔습니다.");
        relocate({'url': "/"})
    }
}

function drawBoard(board) {
    const responses = board['pieceResponses']
    for (let index in responses) {
        const piece = responses[index]
        drawPiece(piece['horizontalIndex'], piece['verticalIndex'], piece['type'], piece['color']);
    }
}

function drawPiece(horizontal, vertical, type, color) {
    const board = document.getElementById("chess-board");
    const image = document.createElement("img")
    image.setAttribute("src", `/images/${color}_${type}.png`);
    const point = board.rows[8 - vertical].cells[horizontal - 1]
    point.innerHTML = '';
    board.rows[8 - vertical].cells[horizontal - 1].appendChild(image);
}

function drawRooms(responseJson) {
    const roomDiv = document.getElementById("rooms");
    roomDiv.innerHTML = "";
    console.log("darwRoom responseJson =", responseJson)
    for (const index in responseJson) {
        const room = createRoom(responseJson[index]);
        roomDiv.appendChild(room);
    }
}

function createRoom(roomResponse) {

    console.log("roomResponse = ", roomResponse)
    const form = document.createElement("form");
    const enterAnchor = Object.assign(document.createElement('a'),
        {href: `/main/${roomResponse['id']}`, innerText: roomResponse['roomName']});

    const deleteButton = Object.assign(document.createElement('img'),
        {
            src: '/images/X_BUTTON.png',
            height: '15',
            style: "cursor: pointer;",
            width: '15'
        });
    deleteButton.onclick = function () {
        const password = prompt("패스워드를 입력하세요 : ");
        send(`/room/${roomResponse['id']}`, {
                method: 'delete',
                body: JSON.stringify({roomName: roomResponse['roomName'], password: password}),
                headers: new Headers({'Content-Type': 'application/json'})
            }, relocate);
    }

    form.appendChild(enterAnchor);
    form.appendChild(deleteButton);
    return form;
}


function toggleHidden(targetId) {
    const element = document.getElementById(targetId);
    if (element.classList.contains("hidden")) {
        element.classList.remove("hidden");
    } else {
        element.classList.add("hidden");
    }
}

// }
// ------------ draw end ------------


// ------------- utils start -----------------

function log(responseJson) {
    console.log('logging ...', responseJson);
}

function relocate(responseJson) {
    console.log("responseJson in relocate =", responseJson);
    window.location.href = responseJson['url'];
}


function toJSON(form) {
    const json = {};
    const formData = new FormData(form);
    Array.from(formData.entries()).forEach(([key, value]) => {
        json[key] = value;
    })
    return JSON.stringify(json);
}

function getCurrentParam(key) {
    let params = (new URL(document.location)).searchParams;
    return params.get(key);
}

function getLastPath() {
    const pathName = new URL(document.location).pathname;
    const splitted = pathName.split("/");
    return splitted[splitted.length - 1];
}

function showStatus(responseJson) {
    let string = 'WHITE SCORE = ' + responseJson['score']['WHITE'] +
        '\n' +
        'BLACK SCORE = ' + responseJson['score']['BLACK'];
    alert(string);
}

async function send(path, fetchBody, handler) {
    async function alertIfException(responseJson) {
        console.log("toJSON = ", responseJson);
        if (responseJson['exception']) {
            alert(responseJson['exception']);
            return null;
        }
        return responseJson;
    }

    let response = await fetch(path, fetchBody);
    let responseJson = await response.json();
    if (await alertIfException(responseJson)) {
        handler(responseJson);
    }
}

// --------------- utils end ------------------