const score = () => {
    let url = "/status/" + fetchId();
    let scoreRequest = {
        method: "GET",
    };
    fetch(url, scoreRequest)
        .then(response => {
            alert(response.data);
        })
        .catch(reason => {
            alert(reason.data);
        })

};

const save = () => {
    let url = "/save/" + fetchId();
    let saveRequest = {
        method: "POST",
    };
    fetch(url, saveRequest)
        .then(() => {
            alert("저장을 완료했습니다.");
        })
        .catch(reason => {
            alert(reason.data);
        })

};

const restart = () => {
    let url = "/restart/" + fetchId();
    let saveRequest = {
        method: "POST",
    };
    fetch(url, saveRequest)
        .then(() => {
            location.href = "/chess/" + fetchId();
        })
        .catch(reason => {
            alert(reason.data);
        })

};

const end = () => {
    let url = "/end/" + fetchId();
    let saveRequest = {
        method: "POST",
    };
    fetch(url, saveRequest)
        .then(() => {
            alert("게임을 종료했습니다.");
            location.href = "/";
        })
        .catch(reason => {
            alert(reason.data);
        })

};

function findPieceBy(piece, team) {
    return "" + piece + "<img src='../piece/_.png' style='max-width: 100%' alt='" + team + "" + piece + team + "'>";
}
