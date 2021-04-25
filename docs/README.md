## Refactoring TODO

- [x] MAP으로 다루던것을 좀더 명확하게 DTO로 수정
- [x] success 메시지 제거
- [x] Dao를 Repo로 네이밍 수정
- [x] insert할떄 사용하는 중복체크 쿼리 분리
- [x] GameStatusDto 객체를 만들어서 리턴하는것이 아닌 바로 리턴하게 수정
- [x] Gson 객체도 Bean 으로 만들기
- [x] ~~@JsonCreator를 통해 생성자를 찾도록 수정~~
  - Intellij 세팅으로는 default 생성자가 반드시 필요.
- [x] 테스트 코드 작성

---

## Step2

- [x] @Bean 어노테이션을 통해 빈을 생성하던거을 config을 통해 Gson을 bean으로 정의하기
- [x] Service 테스트 코드 작성