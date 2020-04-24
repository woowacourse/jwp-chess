async function move(moveInfo) {
    const moveResponse = await fetch('/api/piece', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(moveInfo)
    });

    if (moveResponse.ok) {
        renderPiece(await moveResponse.json());
    } else {
        alert(await moveResponse.text());
    }
}

function renderPiece(response) {
    const [from, to] = response.split(" ");
    const sourceNode = document.getElementById(from);
    const targetNode = document.getElementById(to);

    targetNode.innerText = sourceNode.innerText;
    sourceNode.innerText = " ";
}

let isFrom = true;
const moveInfo = {};

function boxClickHandler() {
    return (event) => {
        if (isFrom && event.target.innerText !== " ") {
            moveInfo.from = event.target.id;
            isFrom = false;
        } else if (!isFrom) {
            moveInfo.to = event.target.id;
            isFrom = true;
            move(moveInfo);
        }
    };
}

window.onload = () => document.querySelectorAll('.box')
    .forEach(box => box.addEventListener('click', boxClickHandler()));