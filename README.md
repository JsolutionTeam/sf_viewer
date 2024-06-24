# sf_viewer

배포 후 실행 방법
메인 스프링부트 어플리케이션 어노테이션이 붙은 멀티모듈을 각각 실행해야 한다

api같은 경우 @SpringbotApplication 어노테이션이 있기 때문에
gradlew :api:bootRun 으로 실행한다.

현재 jenkins 설정 값
gradlew build && gradlew :api:bootRun --args='--spring.profiles.active=prod'

---
# 센서 장치가 추가됨에따라 TCP SERVER 추가

## 유지보수 시 확인해야 할 사항
1. 센서 정보가 추가되면 application.yml에 sensors를 수정해야 한다.

