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