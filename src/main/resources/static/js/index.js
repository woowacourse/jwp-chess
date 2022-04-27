const modal = document.getElementById('modal');

const modalOpenButton = document.getElementById('find-game-button');
modalOpenButton.addEventListener('click', () => {
  modal.style.visibility = 'visible';
});

const modalCloseButton = document.getElementById('box__close-button');
modalCloseButton.addEventListener('click', () => {
  modal.style.visibility = 'hidden';
});
