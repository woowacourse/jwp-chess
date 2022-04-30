const toBody = (form) => {
    var object = {};
    object['title'] = form.parentElement.getElementsByTagName("a")[0].textContent;
    object['running'] = form.parentElement.getElementsByClassName("running")[0].textContent;
    for (const pair of new FormData(form)) {
        object[pair[0]] = pair[1];
    }
    return JSON.stringify(object);
}

const deleteBoard = async (event) => {
    event.preventDefault();
    const form = event.target;
    const id = form.parentElement.getElementsByClassName("id")[0].textContent;
    const response = await fetch("/game/"+id, {
        headers: {'Content-Type': 'application/json'},
        method: 'delete',
        body: toBody(event.target)
    });

console.log(toBody(event.target));
    if (!response.ok) {
        return alert(await response.text());
    }

    form.parentElement.remove();

    const total = document.querySelector("#total");
    total.textContent = total.textContent-1;
}

const init = () => {
    const gameForm = document.getElementsByClassName("game_form");
    for (i = 0; i < gameForm.length; i++) {
        gameForm[i].addEventListener("submit", (event) => deleteBoard(event));
    }
}

init();
