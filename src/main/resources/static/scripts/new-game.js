const buildBody = (form) => {
    const data = {};
    for (const pair of new FormData(form)) {
        data[pair[0]] = pair[1];
    }
    return data;
}

const initGameAndGetId = async (e, form) => {
    e.preventDefault();
    const response = await fetch("/game", {
        headers: {'Content-Type': 'application/json'}, // 디폴트: text/plain;charset=UTF-8
        method: "post",
        body: JSON.stringify(buildBody(form))
    });
    if (!response.ok) {
        return alert(await response.text());
    }
    const {id} = await response.json();
    window.location.replace(`/game/${id}`);
}

const init = () => {
    const form = document.querySelector("form");
    form.addEventListener("submit",
        (e) => initGameAndGetId(e, form)
    );
}

init();