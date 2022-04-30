const score = document.getElementById("score");

async function selectStatus(id) {
    const res = await fetch('/game/status/' + id, {
        method: 'get'
    });

    const data = await res.json();
    if (!res.ok) {
        alert(data.message);
        return;
    }
    score.innerText = `백: ${data.whiteScore}점 흑: ${data.blackScore}점`;
}
