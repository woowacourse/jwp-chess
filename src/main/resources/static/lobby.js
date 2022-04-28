const RoomWrapper = ({children}) => {
    return (
        <ul>{children}</ul>
    );
}

const Room = ({id, title}) => {
    return (
        <li>
            <a href={`/game.html?id=${id}`}> {title} </a>
        </li>
    );
}

const Lobby = () => {
    const [rooms, setRooms] = React.useState([]);

    React.useEffect(() => {
        fetchRooms().then(rooms => {
            setRooms(rooms);
        });
    }, []);

    const handleCreateRoomClick = () => {
        location.href = `/create-room.html`;
    }

    return (
        <React.Fragment>
            <RoomWrapper>
                {
                    rooms.map((room) => <Room id={room.id} title={room.title}/>)
                }
            </RoomWrapper>

            <Button onClick={handleCreateRoomClick}> 새로운 방 생성하기 </Button>
        </React.Fragment>
    );
};

ReactDOM.render(<Lobby/>, document.querySelector('#root'));