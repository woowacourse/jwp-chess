const toggleStartGameSection = () => {
    const hideSection = document.querySelector("#new_game_section");
    hideSection.classList.toggle("hide");
}

const toBody = (form) => {
    var object = {};
    new FormData(form).forEach(function(value, key){
        object[key] = value;
    });
    return JSON.stringify(object);
}

const initGameAndGetId = async (event, form) => {
    event.preventDefault();
    const response = await fetch("/game/new", {
        headers: {'Content-Type': 'application/json'},
        method: "post",
        body: toBody(form)
    });

    if (!response.ok) {
        return alert(await response.text());
    }

    const {id} = await response.json();
    window.location.replace(`/game/${id}`);
}

const init = () => {
    const newGame = document.querySelector("button#new_game");
    newGame.addEventListener("click", toggleStartGameSection);

    const initGame = document.querySelector("#new_game_form");
    initGame.addEventListener("submit",
        (e) => initGameAndGetId(e, initGame)
    );
}

init();