const CreateRoom = () => {
    const [title, setTitle] = React.useState("");
    const [password, setPassword] = React.useState("");

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    }

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleCreateClick = async () => {
        try {
            const room = await createRoom(title, password);
            location.href = `/game.html?id=${room.id}`;
        } catch (e) {
            alert(e.response.data.message);
        }
    }

    return (
        <PageWrapper>
            <Title> 방 생성하기 </Title>

            <TextField type="text" placeholder="방 이름" onChange={handleTitleChange}/>
            <TextField type="password" placeholder="방 비밀번호" onChange={handlePasswordChange}/>

            <Button onClick={handleCreateClick}> 방 생성하기 </Button>
        </PageWrapper>
    );
};

ReactDOM.render(<CreateRoom/>, document.querySelector('#root'));
