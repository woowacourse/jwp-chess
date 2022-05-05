package chess.dto;

public class RoomDto {
   private int id;
   private String name;
   private String password;
   private String status;

   public RoomDto(int id, String name, String password, String status) {
      this.id = id;
      this.name = name;
      this.password = password;
      this.status = status;
   }

   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getPassword() {
      return password;
   }

   public String getStatus() {
      return status;
   }
}
