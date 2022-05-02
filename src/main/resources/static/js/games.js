const items = document.querySelector(".items");

function setList() {
    $.ajax({
        url: "/games",
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "get",
        success: function (data) {
            const games = data.games;
            $.each(games, function(index, game) {
                console.log(game.name)
                console.log(game.id)
                const item =  createItem(game.id, game.name)
                items.appendChild(item);
            })
        },
        error: function (data){
            alert(data)
        }
    })
}

function deleteGame(id) {
    const object = {
        "password": document.getElementById("password").value
    }
    $.ajax({
        url: "/" + id,
        contentType: 'application/json; charset=utf-8',
        type: "delete",
        data: JSON.stringify(object),
        success: function (data) {
            location.reload();
            alert(data);
        },
        error: function (data){
            alert(JSON.stringify(data));
        }
    })
}

function createItem(id, name) {
    const itemRow = document.createElement('ul');
    itemRow.setAttribute('class', 'itemRow');

    const item = document.createElement('div');
    item.setAttribute('class', 'item');

    const participate = document.createElement("form");
    participate.setAttribute("method", "post");
    participate.setAttribute("action", "/participate/" + id);

    const participateMessage = document.createElement('span');
    participateMessage.innerText = '게임 참여하기';
    participate.appendChild(participateMessage);

    const room = document.createElement("input");
    room.setAttribute("type", "submit");
    room.setAttribute("value", "체스방 이름:" + name + " 체스방 번호:" + id)
    participate.appendChild(room);

    const deleteForm = document.createElement("input");
    deleteForm.setAttribute("id", "password");
    const passwordMessage = document.createElement('span');
    passwordMessage.innerText = '비밀번호 입력';

    deleteForm.appendChild(passwordMessage);

    const send = document.createElement("button");
    send.setAttribute("onclick", "deleteGame("+id+")");
    send.textContent = "방 삭제";

    const blank = document.createElement('br');

    item.appendChild(participate);
    item.appendChild(deleteForm);
    item.appendChild(send);

    itemRow.appendChild(item);
    itemRow.appendChild(blank);

    return itemRow;
}
