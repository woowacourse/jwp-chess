const deleteBoard = async (event, form) => {
    const {id} = form.parentElement.querySelector(".id").textContent;

    event.preventDefault();
    const response = await fetch("/game/${id}", {
        headers: {'Content-Type': 'application/json'},
        method: "delete"
    });

    if (!response.ok) {
        return alert(await response.text());
    }

    form.remove();
}

const init = () => {
    const gameForm = document.querySelector(".game_form");
    gameForm.addEventListener("submit",
        (event) => deleteBoard(event, gameForm)
    );
}

init();
