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
            alert(e.message);
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