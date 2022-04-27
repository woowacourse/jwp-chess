const toBody = (form) => {
    var object = {};
    object['id'] = form.parentElement.querySelector(".id").textContent;
    object['title'] = form.parentElement.querySelector("a").text;
    object['running'] = form.parentElement.querySelector(".running").textContent;
    for (const pair of new FormData(form)) {
        object[pair[0]] = pair[1];
    }
    return JSON.stringify(object);
}

const deleteBoard = async (event, form) => {
    event.preventDefault();
    const id = form.parentElement.querySelector(".id").textContent;
    const response = await fetch("/game/"+id, {
        headers: {'Content-Type': 'application/json'},
        method: 'post',
        body: toBody(form)
    });

    if (!response.ok) {
        return alert(await response.text());
    }

    form.parentElement.remove();

    const total = document.querySelector("#total");
    total.textContent = total.textContent-1;
}

const init = () => {
    const gameForm = document.querySelector(".game_form");
    gameForm.addEventListener("submit",
        (event) => deleteBoard(event, gameForm)
    );
}

init();
