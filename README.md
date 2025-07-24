# SDI 탐방 관리 API 서버

간단한 탐방 일정 관리 REST API 서버입니다. Spring Boot를 사용하여 탐방 일정을 등록하고 조회하는 기능을 제공합니다.

✨ **주요 기능**

*   탐방 일정 등록
*   다가오는 탐방 일정 조회

🛠️ **기술 스택**

*   Java
*   Spring Boot
*   Spring Data JPA
*   PostgreSQL

🚀 **실행 방법**

1.  **사전 준비**
    *   Java (JDK)
    *   Gradle
    *   실행 중인 PostgreSQL 데이터베이스

2.  **환경 변수 설정**

    애플리케이션을 실행하기 전에 데이터베이스 연결을 위한 환경 변수를 설정해야 합니다.

    ```shell
    # 데이터베이스 접속 URL (기본값: jdbc:postgresql://localhost:5432/sdi)
    export DB_URL=jdbc:postgresql://<your-db-host>:<port>/<database>
    # 데이터베이스 사용자 이름
    export DB_USERNAME=<your_db_username>
    # 데이터베이스 비밀번호
    export DB_PASSWORD=<your_db_password>
    ```

3.  **애플리케이션 실행**

    터미널에서 아래 명령어를 입력하여 애플리케이션을 실행합니다.

    ```shell
    ./gradlew bootRun
    ```

    애플리케이션은 기본적으로 8080 포트에서 실행됩니다.

📝 **API 명세**

1.  **다가오는 탐방 일정 조회**
    *   **Endpoint**: `GET /api/v1/visits`
    *   **Description**: 특정 시간 이후의 모든 탐방 일정을 시작 시간 순서대로 조회합니다.
    *   **Example Request**:
        ```shell
        curl "http://localhost:8080/api/v1/visits"
        ```
        
2   **텔레그램 봇 콜백 등록**
    *   **Endpoint**: `GET /api/v1/callbacks/telegram`
    *   **Description**: 텔레그램 1기봇이 메세지를 수신한경우 콜백을 통해 알려주도록 콜백을 등록합니다.
    *   **Path Parameter**: `/bot<TOKEN>`
    *   **Query Parameter**:
        *   `setWebhook` (String): 콜백을 받으려는 서버 엔드포인트
    *   **Example Request**:
        ```shell
        curl "https://api.telegram.org/bot<TOKEN>/setWebhook?url=<URL>"
        ```