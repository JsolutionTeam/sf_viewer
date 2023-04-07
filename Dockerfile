
# 도커 이미지 빌드 명령어
# docker build -t knuh-api:1.0.0 .

# 도커 컴포즈 배포 명령어 --no-cache는 기존 이미지를 덮어씌운다.
# docker-compose up --force-recreate -d

# Start with a base image containing Java runtime
FROM openjdk:11
# slim 버전

# /app 디렉토리 생성 CMD 명령어를 통해 생성
RUN mkdir -p /app

# /app 디렉토리를 Working Directory로 설정
WORKDIR /app

# 현재 디렉터리에 있는 파일들을 이미지 내부 /app 디렉터리에 추가함
ADD . /app

# Add Author info
LABEL maintainer="jsolution"

# Add a volume to /tmp
VOLUME ${PWD}/logs:/logs

# Make port 18080 available to the world outside this container
EXPOSE 18080

# The application's jar file
ARG JAR_FILE=./api/build/libs/api.jar

# Add the application's jar to the container
ADD ${JAR_FILE} /app.jar

#COPY ./build/libs/knuh.jar /app.jar

# 타임존 설정 OracleDB JDBC 접속 중 에러발생 해결
ENV TZ=Asia/Seoul

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/tmp/dumps/heapdump.hprof"]
