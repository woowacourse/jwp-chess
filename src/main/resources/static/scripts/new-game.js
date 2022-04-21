const initGameAndGetId = async () => {
    const response = await fetch("/", {method: "post"});
    const {id} = await response.json();
    window.location.replace(`/game/${id}`);
}

const newGameBtn = document.querySelector("button#init_game");

const init = () => {
    newGameBtn.addEventListener("click", initGameAndGetId);
}

init();