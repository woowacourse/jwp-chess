package chess.dto.responsedto;

public class TestDto {

    private String name;
    private int age;

    public TestDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
