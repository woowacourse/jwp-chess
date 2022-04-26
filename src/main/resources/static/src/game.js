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

    fetch("/game/"+ gameId +"/move", {
        method: "post",
        redirect: 'follow',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            source: sourceInput,
            target: targetInput
        }),
    })
        .then(res => {
            window.location.href = res.url;
        })
        .catch(error => {
            alert(error.message + ": 잘못된 이동입니다")
        })
})

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
