const section = document.querySelector("section");

let sourceInput;
let targetInput;
let gameId;

section.addEventListener("mousedown", (event) => {
    saveId();
    sourceInput = findTagId(event);
})

section.addEventListener("mouseup", (event) => {
    targetInput = findTagId(event);
    return updateGame()
})

async function updateGame() {
    const config = {
        headers: {'Content-Type': 'application/json'},
        method: "put",
        body: JSON.stringify({
            source : sourceInput,
            target : targetInput
        })
    };
    const response = await fetch(`/game/${gameId}/move`, config);
    if (!response.ok) {
        const errorMessage = await response.text()
        return alert(errorMessage);
    }
    window.location.reload();
}

function saveId() {
    const ID_PATH_INDEX = 2;
    gameId = new URL(window.location.href).pathname
        .split("/")[ID_PATH_INDEX];
}

function findTagId(event) {
    if (event.target.nodeName === 'IMG') {
        return event.target.parentNode.id;
    }
    return event.target.id;
}
