const mainStart = document.querySelector("#main-start");
const mainLoad = document.querySelector("#main-load");
const mainView = document.querySelector("#main-view");
const basePath = 'http://localhost:8080';

mainStart.addEventListener("click", async () => {
    let result = window.prompt("게임 이름을 입력해주세요");
    if (result === '' || result === null) {
        return;
    }

    const data = {
        name: result
    };

    const option = {
        method: 'POST',
        headers: {
            'content-type': 'application/json;charset=UTF-8',
        },
        body: JSON.stringify(data)
    };

    const response = await fetch(basePath + "/api/games", option)

    if (response.status === 400 || response.status === 500) {
        const body = await response.json();
        alert(body.message);
        return;
    }
    localStorage.setItem("name", result)
    window.location = basePath + "/games"
});

mainLoad.addEventListener("click", async () => {
    let result = window.prompt("찾으려는 게임 이름을 입력해주세요");
    if (result === '' || result === null) {
        return;
    }

    const response = await fetch(basePath + "/api/games/" + result);

    if (response.status === 400 || response.status === 500) {
        const body = await response.json();
        alert(body.message);
        return;
    }
    localStorage.setItem("name", result)
    window.location = basePath + "/games"
});

mainView.addEventListener("click", async () => {
    open("view", "저장된 체스게임 방",
        "width = 500, height = 500, top = 100, left = 200, location = no");
})