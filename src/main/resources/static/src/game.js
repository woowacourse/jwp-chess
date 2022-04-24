const section = document.querySelector("section");

let fromInput;
let toInput;
let gameId;

section.addEventListener("mousedown", (event) => {
  saveId();
  fromInput = findTagId(event);
})

section.addEventListener("mouseup", (event) => {

  toInput = findTagId(event);

  fetch("/game/"+gameId+"/move", {
    method: "post",
    redirect: 'follow',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    body: JSON.stringify({
      source: fromInput,
      target: toInput,
      gameId: gameId
    }),
  })
  .then(res=>{
     window.location.href = res.url
   })
  .catch(error => {
    alert(error.message)
  })
})

function saveId() {
  let url = new URL(window.location.href);
  gameId = url.pathname.split("/")[2]

}

function findTagId(event) {
  if (event.target.nodeName === 'IMG') {
    return event.target.parentNode.id;
  }
  return event.target.id;
}

//spark를 썼을 때 에러 처리 하려고
//java에서 생기는 에러를 잡아서
//다시 js 에러로 바꿔서 날려줌.
function status(res) {
  if (!res.ok) {
    return res.text().then(text => {
      throw new Error(text)
    })
  }
}
