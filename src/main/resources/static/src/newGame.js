const createGame = document.getElementById("createGame");

createGame.addEventListener("click", async function () {
    const input_name = document.getElementById("newRoomName").value;
    const input_password = document.getElementById("password").value;
    const input_confirmPassword = document.getElementById("confirmPassword").value;

    let response = await fetch("/game/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            name: input_name,
            password: input_password,
            confirmPassword: input_confirmPassword,
        }),
    }).then(handleErrors)
        .catch(function (error) {
            alert(error.message);
        })
    response = await response.json();
    location.href = "/game/"+ response.id;
})

async function handleErrors(response) {
    if (!response.ok) {
        let errorMessage = await response.json();
        throw Error(errorMessage.message);
    }
    return response;
}
