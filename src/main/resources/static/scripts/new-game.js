const initGameAndGetId = async () => {
    const response = await fetch("/game", {method: "post"});
    const {id} = await response.json();
    window.location.replace(`/game/${id}`);
}

const init = () => {
    const newGameBtn = document.querySelector("button#init_game");
    newGameBtn.addEventListener("click", initGameAndGetId);
}

init();