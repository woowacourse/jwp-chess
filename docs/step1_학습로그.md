# 학습로그 1-1

# [Spring] @SprintBootApplication - 3

## 내용
- @SpringBootApplication 는 Spring 의 기본 설정을 선언해준다.
- `main`이 들어있는 클래스 상단에 붙여준다.
```java
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
