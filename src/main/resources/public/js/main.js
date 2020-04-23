document.querySelectorAll(".position").forEach(element => {
    element.addEventListener("click", async function () {
        let command;

        if (document.getElementById("moveStart").value) {
            document.getElementById("moveEnd").value = element.id;
            command = "이동";
        } else {
            document.getElementById("moveStart").value = element.id;
            command = "갈 수 있는 곳 확인";
        }

        document.getElementById("moveCommand").value = command;
        document.getElementById("moveForm").submit();
    });
});