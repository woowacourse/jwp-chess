# 학습로그 2-1

# [Test] JdbcTest - 5

## 내용
- `@JdbcTest`에는 `@Transactional`이 포함되어 있어 각 테스트 후 롤백된다.
- 테스트용 프로퍼티 파일 이름에 `application-test.properties`를 쓰고, 테스트 클래스에 `@ActiveProfiles("test")`을 붙여 해당 프로퍼티 파일을 적용할 수 있다.
- 프로퍼티 파일에서 클래스패스에 적용되는 sql 문은 테스트마다 실행되기 때문에 `DROP TABLE IF EXISTS 이름` 을 해주어야 예외없이 테스트가 실행된다.
- Dao 테스트에 적용해보았다.

## 태그
- JdbcTest, Test

# [Test] Mockito - 5

## 내용
- mock 객체를 만들어 수행할 수 있다. 
- BDD 스타일로 given, when, then으로 실행 가능하다.
- Service layer 에 적용해보았다.
## 태그
- Mockito, Test

# [Test] MvcMock - 5

## 내용
- 전달해주는 객체를 equals를 가지고 비교한다.
- 객체가 같지 않으면 mock에 설정한 코드를 실행하지 않는다. 조건과 같은 객체가 들어왔을 때만 mock을 실행
- 메소드에 인자 값에 객체를 넣어줄 때, `any(원하는객체.class)`로 해당 클래스에 해당하는 인스턴스를 조건으로 넣어줄 수 있다.
- MVC 모델에 특화되어 있는 mock 테스트 도구이며, view 와 관련된 로직을 테스트 가능하다.
- Controller 에 적용해보았다.

## 태그
- MvcMock, Test