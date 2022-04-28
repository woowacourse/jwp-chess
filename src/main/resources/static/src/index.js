const createGameButton = document.getElementById("createGame");
const resumeGameButton = document.getElementById("resumeGame");

createGameButton.addEventListener("click", function() {
    location.href = "/game/create";
})

resumeGameButton.addEventListener("click", async function() {
    await fetch("/game/list", {
        method: "GET",
    }).then((handleErrors) => {
        location.replace("/game/list");
    }).catch(function (error) {
            alert(error.message);
        })
})

async function handleErrors(response) {
    if (!response.ok) {
        let errorMessage = await response.json();
        throw Error(errorMessage.message);
    }
    return response;
}
