const buildJsonBody = () => {
    const gameName = document.getElementById("game_name").innerText;
    const inputValue = document.getElementById("password_input").value;
    return {name: gameName, password: inputValue};
}

const deleteGameRedirectToHome = async (e, form) => {
    e.preventDefault();
    const response = await fetch(form.action, {
        headers: {'Content-Type': 'application/json'},
        method: "delete",
        body: JSON.stringify(buildJsonBody())
    });
    if (!response.ok) {
        return alert(await response.text());
    }
    alert("게임이 성공적으로 삭제되었습니다!");
    window.location.replace("/");
}

const init = () => {
    const form = document.querySelector("form");
    form.addEventListener("submit",
        (e) => deleteGameRedirectToHome(e, form)
    );
}

init();
