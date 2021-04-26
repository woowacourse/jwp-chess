const mainStart = document.querySelector("#main-start");
const mainLoad = document.querySelector("#main-load");
const signIn = document.querySelector("#main-signin");
const signUp = document.querySelector("#main-signup");
const rooms = document.querySelector(".room-single");
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

    const response = await fetch(basePath + "/api/v1/games", option)

    if (response.status === 400 || response.status === 401 || response.status === 500) {
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

    const response = await fetch(basePath + "/api/v1/games/" + result);

    if (response.status === 400 || response.status === 401 || response.status === 500) {
        const body = await response.json();
        alert(body.message);
        return;
    }
    localStorage.setItem("name", result)
    window.location = basePath + "/games"
});

rooms.addEventListener("click", async (source) => {
    const name = source.target.value;
    if(name ==="" || name === undefined){
        return;
    }
    if (window.confirm(name + "방에 입장 하시겠습니까?")) {
        localStorage.setItem("name", name);
        window.location = basePath + "/games";
    }
});

signIn.addEventListener("click", async () => {
    window.location = basePath + "/signin";
});

signUp.addEventListener("click", async () => {
    window.location = basePath + "/signup";
});
