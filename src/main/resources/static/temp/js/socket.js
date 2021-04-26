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
let $board;

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
  $board = document.querySelector('.board');
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
  if ($board) {
    $board.addEventListener('click', clickEvent);
  }
}

function submit(target) {
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

function clickEvent({target}) {

  if (target.classList.contains('create-room')
      || target.parentElement.classList.contains('create-room')) {
    $create_modal.style.display = 'block';
    return;
  }

  if (target.classList.contains('enter') || target.parentElement.classList.contains('enter')) {
    //다 모달 띄워야됨.
    const enter = target.closest('.enter')
    $enter_modal.style.display = 'block';
    roomId = enter.id;
    locked = enter.classList.contains('lock');
    $enter_modal.id = enter.classList.contains('asPlayer') ? 'asPlayer'
        : 'asParticipant';
    impossible = enter.classList.contains('impossible');
    return;
  }

  if (target.parentElement.classList.contains('exit')) {
    exitAll();
    return;
  }

  if ((target.classList.contains('board-item') || target.parentElement.classList.contains('board-item')) && commandExecutor.board().movable()) {
    let targetBoardItem = target.closest(".board-item");

    if (targetBoardItem.classList.contains('movable')) {
      movePosition(targetBoardItem);
    } else {
      commandExecutor.board().selectItem(targetBoardItem);
    }
  }

  submit(target);

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

function movePosition(targetBoardItem) {
  const currentPosition = commandExecutor.board().getSelectedItem().id;
  const targetPosition = targetBoardItem.id;

  commandExecutor.board().move(targetBoardItem);
  roomRequest.move(currentPosition, targetPosition);
}

webSocket.onopen = () => {

}
