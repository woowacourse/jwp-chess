function openPop() {
    document.getElementById("popup_layer").style.display = "block";
}

//팝업 닫기
function closePop() {
    document.getElementById("popup_layer").style.display = "none";
}

window.onload = startAndDraw

async function startAndDraw() {
    await fetch("/list")
        .then(res => res.json())
        .then(res => {
            drawBoard(res)
        })
}

function drawBoard(res) {
    res.forEach((data) => {
        let eachDiv = document.getElementById("start")
        let tr = document.createElement("tr")
        eachDiv.appendChild(tr)
        let td = document.createElement("td")
        let div = document.createElement("div")
        div.setAttribute("id", data.id)
        div.innerText = data.name
        td.appendChild(div)
        tr.appendChild(td)

        let td2 = document.createElement("td")
        let div2 = document.createElement("div")

        div2.setAttribute("id", data.id)
        div2.setAttribute("onclick" , "delete_room(id)")
        div2.innerText = "삭제"
        td2.appendChild(div2)
        tr.appendChild(td2)
    })
}

function delete_room(e) {
    alert(e);
}
