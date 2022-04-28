package chess.dto;

public class RoomDto {
   private String name;
   private String password;

   public RoomDto(String name, String password) {
      this.name = name;
      this.password = password;
   }

   public String getName() {
      return name;
   }

   public String getPassword() {
      return password;
   }
}
