# jtop_prj
Gitpod 기반의 Java \leftrightarrow Python 상호 운용성(Interoperability) 테스트 프로젝트.

# API Call Proto 실행 명령어 정리
```
# gitpod 터미널에서 실행시 java 17 경로설정 필요
/workspace/jtop_prj (main) $ 
source ./set_java17_path.sh

# flask 실행
gitpod /workspace/jtop_prj (main) $ 
python3 ./python-service/app.py 

# spring 실행
# 깨끗하게 빌드하고 실행 가능한 JAR 파일을 생성합니다.
cd java-service
mvn clean package

# 스프링 부트 서버를 실행합니다.
# 기본 포트: 8080
mvn spring-boot:run

# api 호출
https://8080-bluecase1-jtopprj-ltdtm858b3e.ws-us121.gitpod.io/test-python-api
```