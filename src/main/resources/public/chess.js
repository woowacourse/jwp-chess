async function move(moveInfo, game_id) {
    const moveResponse = await fetch(`/api/piece/${game_id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(moveInfo)
    });
}

function alertMessage(response) {
    alert(response.responseText);
}

function renderPiece(response) {
    const fromTo = response.split(" ");
    const sourceNode = document.getElementById(fromTo[0]);
    const targetNode = document.getElementById(fromTo[1]);

    targetNode.innerText = sourceNode.innerText;
    sourceNode.innerText = " ";
}

function boxClickHandler() {
    let isFrom = true;
    const moveInfo = {};
    return (event) => {
        if (isFrom && event.target.innerText !== " ") {
            moveInfo.from = event.target.id;
            isFrom = false;
        } else if (!isFrom) {
            moveInfo.to = event.target.id;
            isFrom = true;
            move(moveInfo, params.get("game_id"));
        }
    };
}

const query = window.location.search;
const params = new URLSearchParams(query);

window.onload = () => {
    document.querySelectorAll('.box')
        .forEach(box => box.addEventListener('click', boxClickHandler()));
    document.querySelector("#param").setAttribute('value', params.get("game_id"));
}
