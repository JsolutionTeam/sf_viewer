# sf_viewer

배포 후 실행 방법
메인 스프링부트 어플리케이션 어노테이션이 붙은 멀티모듈을 각각 실행해야 한다

api같은 경우 @SpringbotApplication 어노테이션이 있기 때문에
gradlew :api:bootRun 으로 실행한다.

현재 jenkins 설정 값
gradlew build && gradlew :api:bootRun --args='--spring.profiles.active=prod'

---
# 개폐 장치로 인한 Spring integration 도입
## Spring integration 용어 정리
> 출처
> <br>https://docs.spring.io/spring-integration/reference/html/
> <br>https://medium.com/@zakoub/spring-integration-example-with-jpa-adapter-basic-usage-d96848ffcfac
1. 필터(Filter)
   1. 메시지를 생성하거나 소비할 수 있는 구성 요소.
   2. 메세지 엔드포인트라고 부르는 것
   3. 인프라에서 격리된 비즈니스 애플리케이션에 가깝다
2. 파이프(Pipe)
   1. 구성 요소 자체가 느슨하게 결합된 상태를 유지하도록 필터간에 메제시를 전송
   2. 파이프는 인터페이스로 캡슐화 해야 함.
   3. 메시지 채널이라고 부르는 것 (subscribe와 publish를 포인트 투 포인트로 하나로 묶어주는)
3. 메시지(Message)
   1. 비지니스 데이터 - 끝에서 끝까지 운반하기 위해 모든 일을 사용
   2. 메타데이터 - 개발자가 편의를 위해 추가한 id, timestamp, extra information 등
4. 메시지 트랜스포머(Message Transformer)
   1. 메시지를 변환하는 구성 요소
5. 메시지 필터(Message Filter)
   1. 사용된 필터와 혼동하지 않도록 메시지를 필터링 해야 하는지 여부를 결정
   2. 조건을 테스트하는 부울 메서드로 표시
6. 메시지 라우터(Message Router)
   1. 메시지를 수신할 채널을 결정
7. 스플리터(Splitter)
   1. 메시지를 전송할 다른 여러 멧지로 분할하는 끝점과 비슷
   2. 복잡한 시나리오에서 많이 사용 됨
8. 애그리게이터(Aggregator)
   1. 여러 메시지를 하나의 메시지로 결합
   2. 스플리터와 반대
9. 채널 어댑터(Channel Adapter)
   1. 채널 어댑터는 메시지를 수신하고 메시지를 전송하는 데 사용되는 메시지 채널을 사용
   2. 어댑터는 인바운드 또는 아웃바운드로 구성될 수 있음
   3. 인바운드는 메시지를 수신하고 아웃바운드는 메시지를 전송
10. 서비스 액티베이터(Service Activator)
    1. 서비스 인스턴스를 메시징 시스템에 연결하기 위한 일반 끝 점
    2. 입력 메시지 채널을 구성해야 하며 호출할 서비스 메서드가 값을 반환할 수 있는 경우 출력 메시지 채널도 제공 가능

