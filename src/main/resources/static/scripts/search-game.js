const onSuccessResponse = ({id, found}) => {
    if (found) {
        window.location.replace(`/game/${id}`);
        return;
    }
    alert(`${id}에 해당되는 게임은 존재하지 않습니다!`)
}

const getTargetUrl = () => {
    const inputValue = document.getElementById("num_input").value;
    return `game/${inputValue}/info`;
}

const searchAndRedirect = async (event) => {
    event.preventDefault();
    const response = await fetch(getTargetUrl());
    const json = await response.json();
    onSuccessResponse(json);
}

const initSearchForm = () => {
    const form = document.getElementById("search-form");
    form.addEventListener('submit', searchAndRedirect);
}

initSearchForm();
