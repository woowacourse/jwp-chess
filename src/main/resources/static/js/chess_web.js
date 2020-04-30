window.onload = function () {
    var error = document.getElementById("error").value;
    var end = document.getElementById("end").innerText;
    var turn = document.getElementById("turn");
    var moveBtn = document.getElementById("moveBtn");
    var startBtn = document.getElementById("startBtn");
    var path = window.location.pathname;

    if (error) {
        alert(error);
        window.history.back();
    }

    if (end) {
        moveBtn.setAttribute("disabled", "disabled");
        turn.innerText = "";
        alert(end.innerText);
    }

    if (!path.startsWith("/ready")) {
        startBtn.value = "재시작";
    }
    if (path.startsWith("/ready")) {
        moveBtn.setAttribute("disabled", "disabled");
    }
};
