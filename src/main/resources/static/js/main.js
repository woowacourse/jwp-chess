const mainStart = document.querySelector("#main-start");
const mainLoad = document.querySelector("#main-load");
const basePath = 'http://localhost:4567';

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
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    };

    const response = await fetch(basePath + "/games", option)
    .then(res => res.json());

    if (response.statusCode === 400) {
        alert(response.message);
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

    const response = await fetch(basePath + "/games/" + result)
    .then(res => res.json());

    if (response.statusCode === 400) {
        alert(response.message);
        return;
    }
    localStorage.setItem("name", result)
    window.location = basePath + "/games"
});
