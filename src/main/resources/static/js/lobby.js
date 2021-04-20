const $newGameBtn = document.querySelector('#newGameBtn');
const $gameNameInput = document.querySelector('#gameNameInput');

$newGameBtn.addEventListener('click', newGame);

function newGame() {
    $.ajax({
        type: "POST",
        url: "/chess",
        data: JSON.stringify({
            "gameName": $gameNameInput.value
        }),
        contentType: "application/json",
        dataType: "json",
        success: update,
        error: alertError
    })
}

function update(gameId) {
    location.href = `/chess/${gameId}`
}

function alertError(response) {
    alert(response.responseText);
}
