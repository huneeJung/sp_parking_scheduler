프로메테우스 / 그라파나 연동

* 프로메테우스
1. https://prometheus.io/download/ OS에 적합한 프로메테우스 설치
2. 설치된 프로메테우스 디렉토리로 이동 후 ./prometheus 실행
3. 로컬에서 정상 동작 확인
4. 프로메테우스 구현체 의존성 및 스프링 엑츄에이터 의존성 build.gradle 추가
5. application.yml 설정 추가
	< 모든 메트릭이 노출되도록 설정 >
        management:
          endpoints:
            web:
              exposure:
                include: "*"
6. 어플리케이션 실행 및 프로메테우스 재실행 아래 접속하여 데이터 포맷 확인 
7. http://localhost:서버포트번호/actuator/metrics ( 엑추에이터 Json 포맷)
8. http://localhost:8080/actuator/prometheus  ( 프로메테우스 포맷 )
9. 프로메테우스는 일종의 DB 로 계속해서 쌓이는 로그를 저장하기 위한 용도
10. 프로메테우스는 Json 데이터를 파싱할 수 없어서 프로메테우스 구현체를 통해 프로메테우스가 취급하는 데이터 포맷으로 프로메테우스 서버와 인터페이스
11. 프로메테우스 설정 파일 수정

	< 프로메테우스 어플리케이션 대상 설정 (Local)>
          - job_name: "spring-actuator”
            metrics_path: '/actuator/prometheus’.  # 메트릭 데이터 요청 Path
            scrape_interval: 1s    # 어플리케이션 메트릭 데이터 호출 주기
            static_configs:
              - targets: ['localhost:어플리케이션 서버 포트]

	< 프로메테우스 어플리케이션 대상 설정 (Docker)>
        - job_name: "springboot"
            metrics_path: "/actuator/prometheus"
            static_configs:
              - targets:
                  - "host.docker.internal:어플리케이션 서버 포트”

13. 도커에 프로메테우스 설치 및 컨테이너 실행
* docker run \ -p 9090:9090 \ -v "설정 파일 경로":/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
1. Status - target 탭에서 정상 연동 확인 가능
2. 아래 명령어를 통해 프로메테우스가 요청한 횟수 확인 가능
* http_server_requests_seconds_count{uri="/actuator/prometheus"}


* 그라파나 
1. 도커에 그라파나 설치 
2. docker run -p 3000:3000 -name grafana grafana/grafana
3. http://localhost:3000 접속 / 초기 id: admin / 초기 pw: admin
4. Datasource 프로메테우스 연동 
5. 요청 Url 입력시 localhost 로 입력하면 커넥션 문제 발생. 현재 사용중인 사설 ip 및 포트로 입력 후 연결 테스트
6. 대시보드 import
7. JMV 대시보드 : https://grafana.com/grafana/dashboards/4701-jvm-micrometer/
8. Datasource 랑 연동 후 대시보드 추가
9. 모니터링 확인


* 참조 블로그 : https://jojaeng2.tistory.com/82
