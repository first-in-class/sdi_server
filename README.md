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
    *   **Query Parameter**:
        *   `startedAt` (DateTime): 조회 기준 시각 (ISO 8601 형식, 예: `2023-10-27T10:00:00Z`)
    *   **Example Request**:
        ```shell
        curl "http://localhost:8080/api/v1/visits?startedAt=2023-01-01T00:00:00Z"
        ```

2.  **새로운 탐방 일정 등록**
    *   **Endpoint**: `POST /api/v1/visits/new`
    *   **Description**: 새로운 탐방 일정을 등록합니다.
    *   **Request Body**:
        ```json
        {
          "startedAt": "2024-09-01T14:00:00Z",
          "finishedAt": "2024-09-01T16:00:00Z",
          "participantCount": "15",
          "teamName": "3기",
          "organizer": "김영우",
          "remark": "사전 질문 준비 필수"
        }
        ```
    *   **Example Request**:
        ```shell
        curl -X POST http://localhost:8080/api/v1/visits/new \
          -H "Content-Type: application/json" \
          -d '{
                "startedAt": "2024-09-01T14:00:00Z",
                "finishedAt": "2024-09-01T16:00:00Z",
                "participantCount": "15",
                "teamName": "3기",
                "organizer": "김영우",
                "remark": "사전 질문 준비 필수"
              }'
        ```