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
    method: 'put',
    redirect: 'follow',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    body: JSON.stringify({
      source: fromInput,
      target: toInput,
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
