document.getElementById("restart").addEventListener("click", onRestart);

const POST = {
    "method": 'POST',
    "headers": {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
}

const moveToChessView = function () {
    window.location.href = '../../../java/chess/view';
}

async function onRestart() {
    await fetch('/api/chess', POST);
    moveToChessView();
}
