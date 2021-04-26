const url = "ws://" + location.hostname + ":" + location.port + "/chess";
const webSocket = new WebSocket(url);
const roomRequest = new ChessRequest();
const commandExecutor = new CommandExecutor();

let $create_room;
let $create_modal;
let $create_form;
let $exit;
let $error_message;

webSocket.onmessage = function (message) {
  commandExecutor.execute(JSON.parse(message.data));
  $create_room = document.querySelector('.create-room');
  $create_modal = document.querySelector('.create-modal');
  $create_form = document.querySelector('.create-form');
  $exit = document.querySelector('.exit');
  $error_message = document.querySelector('.error_message');
  eventHandler();
}

// document.addEventListener('DOMContentLoaded', eventHandler);

function eventHandler() {
  $create_room.addEventListener('click', clickEvent);
  $exit.addEventListener('click', clickEvent);
  $create_form.addEventListener('click', clickEvent);
}

function clickEvent({target}) {

  if (target.classList.contains('create-room')) {
    $create_modal.style.display = 'block';
    return;
  }

  if (target.parentElement.classList.contains('exit')) {
    $create_modal.style.display = 'none';
    $error_message.textContent = '';
    return;
  }

  if (target.classList.contains('submit')) {
    const title = getElement(target, 'roomName').value;
    const password = getElement(target, 'password').value;
    const locked = getElement(target, 'locked').checked;

    if (title === '') {
      $error_message.textContent = '방 제목을 입력해주세요.';
      return;
    }
    if (locked && password === '') {
      $error_message.textContent = '비밀번호를 입력해주세요.';
      return;
    }

    $create_modal.style.display = 'none';
    $error_message.textContent = '';
    roomRequest.createRoom(title, locked, password);
  }

}

function getElement(target, itemName) {
  return target.parentElement.children.namedItem(itemName);
}

webSocket.onopen = () => {

}
