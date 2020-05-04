window.onload = function () {
    var error = document.getElementById("error").value;
    var end = document.getElementById("end").innerText;
    var turn = document.getElementById("turn");
    var moveBtn = document.getElementById("moveBtn");

    if (error) {
        alert(error);
        window.history.back();
    }

    if (end) {
        moveBtn.setAttribute("disabled", "disabled");
        turn.innerText = "";
        alert(end.innerText);
    }
};
