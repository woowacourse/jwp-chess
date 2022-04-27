window.onload= async function () {
    await findAllChessGames();
}

function loadChessGamePage(uri) {
    location.href = "/play?location=" + uri;
}

function showAllChessGames(chessGames){
    Array.from(chessGames).forEach(
        function (element) {
            let board = document.getElementById("games");

            const gameDiv = document.createElement("div");

            const titleTag = document.createElement("span");
            titleTag.textContent = element.title;

            const loadButton = document.createElement("button");
            loadButton.setAttribute("onclick", "loadGame(" + element.id +")");
            loadButton.textContent = "입장하기";

            const deleteButton = document.createElement("button");
            deleteButton.setAttribute("onclick", "deleteGame(" + element.id +")");
            deleteButton.textContent = "삭제";

            gameDiv.appendChild(titleTag);
            gameDiv.appendChild(loadButton);
            gameDiv.appendChild(deleteButton);

            board.appendChild(gameDiv);
        }
    )
}

async function findAllChessGames() {
    const chessGames = await fetch("/chessgames")
        .then(response => handlingException(response))
        .then((response) => response.json())
        .catch(error => {
            alert(error.message);
        });
    showAllChessGames(chessGames);
}
