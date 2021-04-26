# 학습로그 1-1

# [Spring] @SprintBootApplication - 3

## 내용
- @SpringBootApplication 는 Spring 의 기본 설정을 선언해준다.
- `main`이 들어있는 클래스 상단에 붙여준다.
```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
```
- `@ComponentScan`은 `@Component`를 스캔하여 빈으로 등록된 것들을 찾아준다.

## 링크
- [참고한 블로그글](https://bamdule.tistory.com/31)

# [Spring] @Controller, @RestController - 3

## 내용
- `@RestController`는 `@Controller`에 `@ResponseBody`가 포함되는 개념

# [REST] http 메소드 - 3
## 내용
- post: DB에 insert 의 개념
- put: DB에 update 의 개념 (전체)
- patch: DB에 일부를 update 의 개념

## 링크
- [http 메소드 관련 글](https://javaplant.tistory.com/18)

# [Spring] GetMapping, PostMapping - 4
## 내용
- `@PostMapping(path="/home", consumes="application/json")`
- consumes 는 `Content-Type` 헤더에 기반한다.
- `@GetMapping(path = "/pets/{petId}", produces = "application/json")`
- produces 는 `Accept` 헤더에 기반한다.
- `Content-Type`은 해당 바디의 타입이 무엇인지를 나타내며 `Accept`는 받을 수 있는 타입을 나타낸다.
- 명시하지 않으면 기본적으로 json 타입이 적용되는 듯 하다. 
- MediaType 클래스의 상수인 `APPLICATION_JSON_VALUE`, `APPLICATION_XML_VALUE`를 많이 사용한다.

## 링크
- [스프링 공식문서 ~Mapping](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/spring-framework-reference/web.html#mvc-ann-requestmapping)
- [mdn 문서 Content-Type](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type)
- [mdn 문서 Accept](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Accept)

# [Network] REST - 4

## 내용
- REST(Representational State Transfer)는 효율적, 안정적이며 확장가능한 분산시스템을 가져올 수 있는소프트웨어 아키텍처 디자인 제약의 모음
- 그 제약들을 준수했을 때 그 시스템은 `RESTful`하다고 한다.
- REST의 기본 개념은 **리소스(자원)** 이다.
- REST란 자원의 이름을 구분하여 해당 자원의 상태(정보)를 주고 받는 것을 의미하며 `자원의 표현에 의한 상태전달` 을 의미한다.
- get, post 등의 API 를 만들 때, 자원의 관점에서 바라봐야 한다.

## 링크
- [REST service architecture](https://www.service-architecture.com/articles/web-services/representational_state_transfer_rest.html)
- [mdn REST](https://developer.mozilla.org/ko/docs/Glossary/REST)

# 학습로그 1-2

# [Spring] @Transactional - 3

## 내용
- 트랜잭션의 원자성을 유지시켜준다.
- 기본은 `(readOnly = false)`로 셋팅되어 있다. `(readOnly = true)`의 경우, 읽기 이외의 경우 exception을 던진다.

# [DB] 이벤트소싱 패턴 - 4
## 내용
- 이벤트를 저장하는 방식이다.
- 히스토리를 관리가 가능하며, 비즈니스 로직과 동기화할 필요가 없다.
- 히스토리가 많으면 비즈니스로직을 수행해야하는 오버헤드가 크다. 단점을 스냅샷을 활용하여 보완할 수 있다고 한다.

## 링크
- [마이크로소프트 이벤트소싱 관련 글](https://docs.microsoft.com/ko-kr/azure/architecture/patterns/event-sourcing)

# 학습로그 1-3

# [DB] 데이터 모델링 - 5

## 내용
- [데이터 모델링 공부하여 정리한 글](https://nauni.tistory.com/218?category=928627)
- [정규화 공부하여 정리한 글](https://nauni.tistory.com/225?category=928627)

## 태그
- DB

# [Architecture] Layered Architecture, Dto - 4

## 내용
- layered architecture는 관심사를 분리하여 레이어로 구분한 것을 의미한다.
- presentation layer: UI에 관련된 내용
- application layer: 서비스들을 관리한다. 도메인에 포함되지 않지만 기능 요구사항들을 관리
- domain layer: 핵심 비지니스 로직이 존재
- infrastructure layer(persistence layer): DB나 자료를 영구저장하는 것과 관련된 레이어
- 관심사를 가지고 구분하였기 때문에 관심사별로 응집되어 있다. 레이어간 교체가능할 수 있기 때문에 유지보수에 좋을 수 있다.
- 레이어 간에 소통을 하므로 소통하기 위해서는 각 모든 레이어를 거쳐야 한다. 적당한 규모로 나누는 것이 어렵다.
## 링크
- [layered architecture에 대한 참고자료 링크](https://dzone.com/articles/layered-architecture-is-good)
## 태그
- LayeredArchitecture, Dto

# 학습로그 1-4

# [Architecture] 추상화 - 4
## 내용
- 과한 추상화는 코드의 가독성을 해친다.
- [지나친 추상화 프로젝트 깃허브 참고자료 링크](https://github.com/EnterpriseQualityCoding/FizzBuzzEnterpriseEdition)
- 현재 단계에서 굳이 interface를 사용하여 의존관계를 분리해주는 것은 지나칠 수 있다.
- 현재 단계의 프로젝트 규모에서 dto 조립기 까지 만들 필요 없이 dto에서 도메인 정보를 받아 만들어주는 로직정도는 가져도 괜찮다.
- dto의 비즈니스 로직이 들어가면 안 된다는 것은 도메인의 로직을 의미하는 것이다.
- 도메인은 dto 정보를 모르게, dto는 일부 도메인 정보를 알고 적절한 값으로 사용가능하게 만드는 방식을 사용할 수 있다.

## 태그
- 추상화

