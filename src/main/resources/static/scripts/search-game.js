const onSuccessResponse = ({id, found}) => {
    if (found) {
        window.location.replace(`/game/${id}`);
        return;
    }
    alert(`${id}에 해당되는 게임은 아직 만들어지지 않았습니다!`)
}

const getTargetUrl = (event) => {
    const inputValue = document.getElementById("num_input").value;
    return `${event.target.action}?game_id=${inputValue}`;
}

const searchAndRedirect = async (event) => {
    event.preventDefault();
    const response = await fetch(getTargetUrl(event));
    const json = await response.json();
    onSuccessResponse(json);
}

const init = () => {
    const form = document.querySelector("form");
    form.addEventListener('submit', searchAndRedirect);
}

init();
