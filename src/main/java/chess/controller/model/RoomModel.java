package chess.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import chess.controller.RoomApiController;
import chess.domain.room.Room;
import org.springframework.hateoas.EntityModel;

public class RoomModel extends EntityModel<Room> {

    private Long roomId;
    private String title;
    private int count;
    private boolean locked;

    public RoomModel() {
    }

    public RoomModel(Room room) {
        this.roomId = room.id();
        this.title = room.title();
        this.count = room.count();
        this.locked = room.isLocked();
        add(linkTo(methodOn(RoomApiController.class).findRoom(roomId)).withSelfRel());
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
