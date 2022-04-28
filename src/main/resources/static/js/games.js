const items = document.querySelector(".items");

function setList() {
    $.ajax({
        url: "/loadGames",
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

    const deleteForm = document.createElement("form");
    deleteForm.setAttribute("method", "post");
    deleteForm.setAttribute("action", "/" + id);

    const passwordMessage = document.createElement('span');
    passwordMessage.innerText = '비밀번호 입력';

    deleteForm.appendChild(passwordMessage);

    const password = document.createElement("input");
    password.setAttribute("name", "password");
    password.setAttribute("id", "password");

    const send = document.createElement("input");
    send.setAttribute("type", "submit");
    send.setAttribute("value", "방 삭제");

    const blank = document.createElement('br');

    deleteForm.appendChild(password);
    deleteForm.appendChild(send);

    item.appendChild(participate);
    item.appendChild(deleteForm);

    itemRow.appendChild(item);
    itemRow.appendChild(blank);

    return itemRow;
}