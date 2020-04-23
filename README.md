## 체스 - 스프링 실습

### 기능 요구사항
- 1단계
    - 체스판을 초기화 할 수 있다.
    - 말 이동을 할 수 있다.
    - 승패 및 점수를 판단할 수 있다.
    - 웹으로 체스 게임이 가능해야 한다.(스파크 적용)
    - 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)
- 2단계
    - 스파크 애플리케이션을 유지한 상태에서 스프링 애플리케이션을 추가
    - 도메인 객체가 아니며 재사용 할 객체를 스프링 빈을 활용하여 관리하기

### 프로그래밍 요구사항
- 1단계
    [프로그래밍 체크리스트](https://github.com/woowacourse/woowacourse-docs/blob/master/cleancode/pr_checklist.md)의 원칙을 지키면서 프로그래밍 한다.
- 2단계
    - 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
    - @Controller나 @RestController를 활용하여 요청을 받아야 한다.
    - 웹 UI를 적용할 때 도메인 객체의 변경을 최소화해야한다.
    - 스프링 빈임을 나타내는 애너테이션(@Component, @Service 등)을 활용한다.
    - 컴포넌트 스캔을 통해 빈 등록하여 사용한다.
