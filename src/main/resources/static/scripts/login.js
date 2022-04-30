const buildJsonBody = (form) => {
    const gameName = document.getElementById("game_id").innerText;
    const inputValue = form.querySelector(".password_input").value;
    return {name: gameName, password: inputValue};
}

const submitLogin = async (e, form) => {
    e.preventDefault();
    const response = await fetch(`${form.action}/auth`, {
        headers: {'Content-Type': 'application/json'},
        method: "post",
        body: JSON.stringify(buildJsonBody(form)),
        credentials: "include",
    });
    if (!response.ok) {
        return alert(await response.text());
    }
    alert(await response.text());
    window.location.replace(form.action);
}

const initLoginForm = () => {
    const form = document.getElementById("login-form");
    form.addEventListener("submit", (e) => submitLogin(e, form));
}

initLoginForm();
