let source;
let target;

window.onload = function () {
    const elements = document.querySelectorAll(".cell");
    [].forEach.call(elements, (elem) => {
        elem.addEventListener("click", (ev) => {
            if (source === undefined) {
                source = elem.id;
            } else {
                target = elem.id;

                fetch("/move", {
                    method: "post",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        roomName: document.getElementById('name').innerText,
                        source: source,
                        target: target
                    })
                }).then(res => {
                    if (res.status !== 200) {
                        console.log("error", res);
                        source = undefined;
                    } else location.reload();
                })
            }
        })
    })
}