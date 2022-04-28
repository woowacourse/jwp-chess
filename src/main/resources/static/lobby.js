const Title = styled.div`
  text-align: center;
  font-size: 100px;
`;

const RoomWrapper = styled.ul`
  margin: 40px 0;

  & > li {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    padding: 20px;
    font-size: 30px;
    cursor: pointer;
    transition: all 0.2s;
  }

  & > li > a:nth-child(1):hover {
    background-color: #ffffff;
    color: #2ac1bc;
    text-decoration: none;
    box-shadow: 0 0 20px rgba(255, 255, 255, 0.9);
  }

  & > li > a:nth-child(2) {
    color: #868e96;
  }

  & > li > a:nth-child(2):hover {
    color: #343a40;
  }

  & > li > a {
    color: inherit;
    text-decoration: none;
    transition: all 0.2s;
  }
`;

const Room = ({id, title}) => {
    return (
        <li>
            <a href={`/game.html?id=${id}`}> {title} </a>
            <a href={`/delete-room.html?id=${id}`}> 방 제거하기 </a>
        </li>
    );
};


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
        <PageWrapper>
            <Title> 체스의 민족 </Title>
            <RoomWrapper>
                {
                    rooms.map((room) => <Room id={room.id} title={room.title}/>)
                }
            </RoomWrapper>

            <Button onClick={handleCreateRoomClick}> 새로운 방 생성하기 </Button>
        </PageWrapper>
    );
};

ReactDOM.render(<Lobby/>, document.querySelector('#root'));