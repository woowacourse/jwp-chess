const mainStart = document.querySelector("#main-start");
const mainLoad = document.querySelector("#main-load");
const mainView = document.querySelector("#main-view");
const basePath = 'http://localhost:8080';

mainStart.addEventListener("click", async () => {
    let result = window.prompt("게임 이름을 입력해주세요");
    if (result === '' || result === null) {
        return;
    }

    const data = {
        name: result
    };

    const option = {
        method: 'POST',
        headers: {
            'content-type': 'application/json;charset=UTF-8',
        },
        body: JSON.stringify(data)
    };

    const response = await fetch(basePath + "/api/games", option)

    if (response.status === 400 || response.status === 500) {
        const body = await response.json();
        alert(body.message);
        return;
    }
    localStorage.setItem("name", result)
    window.location = basePath + "/games"
});

mainLoad.addEventListener("click", async () => {
    let result = window.prompt("찾으려는 게임 이름을 입력해주세요");
    if (result === '' || result === null) {
        return;
    }

    const response = await fetch(basePath + "/api/games/" + result);

    if (response.status === 400 || response.status === 500) {
        const body = await response.json();
        alert(body.message);
        return;
    }
    localStorage.setItem("name", result)
    window.location = basePath + "/games"
});

mainView.addEventListener("click", async () => {
    const response = await fetch(basePath + "/api/games/view");
    const body = await response.json();

    const roomContainer = document.querySelector(".room-container");
    const table = roomContainer.children[0];
    if (roomContainer.getAttribute("style") === "display: block") {
        const first = table.children[0];
        while (table.firstChild) {
            table.removeChild(table.firstChild);
        }
        table.append(first);
        roomContainer.setAttribute("style", "display: none");
        return;
    }

    if (response.status === 400 || response.status === 500) {
        alert(body.message);
        return;
    }
    const gameStatusRequests = body.gameStatusRequests;

    gameStatusRequests.forEach(gameStatusRequest => {
        const tr = document.createElement("tr");
        tr.setAttribute("align", "center");
        tr.setAttribute("bgcolor", "skybule");

        let td = document.createElement("td");
        td.innerText = gameStatusRequest.chessName;
        tr.appendChild(td);

        td = document.createElement("td");
        if (gameStatusRequest.gameOver) {
            td.innerText = "X";
        } else {
            td.innerText = "O";
        }
        tr.appendChild(td);
        table.appendChild(tr);
    })
    roomContainer.setAttribute("style", "display: block");
})