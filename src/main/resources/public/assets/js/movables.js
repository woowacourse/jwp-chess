export const removeAllMovables = () => {
  const movables = document.querySelectorAll(".movable");
  movables.forEach(movable => {
    movable.parentElement.removeChild(movable);
  });
}

async function getMovables(point, roomId) {
  const response = await fetch("./" + roomId + "/points/" + point + "/movable-points");
  const result = await response.json();
  console.log(result);
  if (response.ok) {
    return result["points"];
  } else {
    alert("HTTP-Error: " + result["message"]);
  }
}

export const addMovables = (point, roomId) => {
  getMovables(point, roomId).then(movables => {
    for (const i in movables) {
      const square = document.querySelector(
          "#" + movables[i].x + movables[i].y);
      const moveDiv = document.createElement("div");
      moveDiv.classList.add("movable");
      moveDiv.value = point;
      square.insertAdjacentElement("afterbegin", moveDiv);
    }
  });
}