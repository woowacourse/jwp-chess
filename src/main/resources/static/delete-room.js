const DeleteRoom = () => {
    const [password, setPassword] = React.useState("");

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleDeleteClick = async () => {
        const {id} = parseQueryString();

        try {
            await deleteRoom(id, password);
            location.href = "/";
        } catch (e) {
            if (e.response.status === 404) {
                alert("방을 찾을 수 없습니다.");
                return;
            }

            if (e.response.status === 500) {
                alert("알 수 없는 서버 에러가 발생하였습니다.");
                return;
            }

            alert(e.response.data.message);
        }
    }

    return (
        <PageWrapper>
            <Title> 방 제거하기 </Title>

            <TextField type="password" placeholder="방 비밀번호" onChange={handlePasswordChange}/>
            <Button onClick={handleDeleteClick}> 방 제거하기 </Button>
        </PageWrapper>
    );
};

ReactDOM.render(<DeleteRoom/>, document.querySelector('#root'));
