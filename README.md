# sf_viewer

배포 후 실행 방법
메인 스프링부트 어플리케이션 어노테이션이 붙은 멀티모듈을 각각 실행해야 한다

api같은 경우 @SpringbotApplication 어노테이션이 있기 때문에
gradlew :api:bootRun 으로 실행한다.

현재 jenkins 설정 값
gradlew build && gradlew :api:bootRun --args='--spring.profiles.active=prod'
