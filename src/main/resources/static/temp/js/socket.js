const url = "ws://" + location.hostname + ":" + location.port + "/chess";
const webSocket = new WebSocket(url);
const roomRequest = new ChessRequest();
const commandExecutor = new CommandExecutor();

let $room_list;
let $create_modal;
let $create_form;
let $exits;
let $error_messages;
let $modals;
let $enter_modal;
let roomId;
let locked;
let impossible;

webSocket.onmessage = function (message) {
  commandExecutor.execute(JSON.parse(message.data));
  $room_list = document.querySelector('.room-list');
  $create_modal = document.querySelector('.create-modal');
  $create_form = document.querySelector('.create-form');
  $exits = document.querySelectorAll('.exit');
  $error_messages = document.querySelectorAll('.error_message');
  $modals = document.querySelectorAll('.modal');
  $enter_modal = document.querySelector('.enter-modal');
  eventHandler();
}

function eventHandler() {
  if ($room_list) {
    $room_list.addEventListener('click', clickEvent);
  }
  if ($enter_modal) {
    $enter_modal.addEventListener('click', clickEvent);
  }
  if ($create_modal) {
    $create_modal.addEventListener('click', clickEvent);
  }
}

function clickEvent({target}) {
  console.log(target)

  if (target.classList.contains('create-room')
      || target.parentElement.classList.contains('create-room')) {
    $create_modal.style.display = 'block';
    return;
  }

  if (target.classList.contains('enter')) {
    //다 모달 띄워야됨.
    $enter_modal.style.display = 'block';
    roomId = target.id;
    locked = target.classList.contains('lock');
    $enter_modal.id = target.classList.contains('asPlayer') ? 'asPlayer'
        : 'asParticipant';
    impossible = target.classList.contains('impossible');
    return;
  }

  if (target.parentElement.classList.contains('exit')) {
    exitAll();
    return;
  }

  if (target.classList.contains('submit')) {
    const nickname = getElement(target, 'nickname').value;
    const password = getElement(target, 'password').value;

    if (target.classList.contains('create-submit')) {
      const title = getElement(target, 'roomName').value;
      const locked = getElement(target, 'locked').checked;

      if (title === '') {
        $error_messages.forEach(error => error.textContent = '방 제목을 입력해주세요.');
        return;
      }

      if (locked && password === '') {
        $error_messages.forEach(error => error.textContent = '비밀번호를 입력해주세요.');
        return;
      }

      if (nickname === '') {
        $error_messages.forEach(error => error.textContent = '닉네임을 설정해주세요');
        return;
      }
      exitAll();
      roomRequest.createRoom(title, locked, password, nickname);
      return;
    }

    if (target.classList.contains('enter-submit')) {
      console.log(target);
      if (nickname === '') {
        $error_messages.forEach(error => error.textContent = '닉네임을 설정해주세요');
        return;
      }

      if (locked && password === '') {
        $error_messages.forEach(error => error.textContent = '비밀번호를 입력해주세요.');
        return;
      }

      if (impossible) {
        $error_messages.forEach(error => error.textContent = '인원이 가득차있습니다.');
        return;
      }

      if ($enter_modal.id === 'asPlayer') {
        roomRequest.enterRoomAsPlayer(roomId, nickname, password);
      } else {
        roomRequest.enterRoomAsParticipant(roomId, nickname, password);
      }

      exitAll();
    }
  }

}

function getElement(target, itemName) {
  return target.parentElement.children.namedItem(itemName);
}

function exitAll() {
  $modals.forEach(modal => modal.style.display = 'none');
  $error_messages.forEach(error => error.textContent = '');
  roomId = '';
  locked = '';
  impossible = false;
}

webSocket.onopen = () => {

}
