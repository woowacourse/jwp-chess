const $moveBtn = document.querySelector(".moveBtn");

function moveToBoard() {
    window.location.href = `/game/1`
}

$moveBtn.addEventListener("click", moveToBoard);