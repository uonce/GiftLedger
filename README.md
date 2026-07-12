<div align="center">
  <h1>품앗이</h1>
  <p><strong>경조사비 관리 시스템</strong></p>
</div>

<div align="center">
"지난 결혼식에 얼마 냈더라?" — 경조사비 관리의 어려움을 해결하는 웹 시스템입니다. <br>
Spring Boot + JPA로 RESTful API를 구축하고, JWT 인증, 복잡한 엔티티 관계 설계, 
통계 쿼리 최적화 등 실무 기술을 적용했습니다.

<br><br>

**팀 구성** <br>
3인 (김우식, 송유원, 현홍석) <br>
유레카 백엔드 비대면 미니 프로젝트 <br>

</div>

<br><br>

## 기술 스택

![Spring Boot](https://img.shields.io/badge/SPRING%20BOOT-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/SPRING%20SECURITY-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Swagger](https://img.shields.io/badge/SWAGGER-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

<br>

## 핵심 기능

**이벤트(경조사) 관리**
- 행사, 지인, 주고받은 금액을 한 번에 등록/수정/삭제/조회
- 이벤트별 거래 내역 상세 조회

**분석 및 통계**
- 종합 대시보드: 총 준/받은 금액, 잔액, 회수율, 전년 대비 지출 변화량
- 지출 패턴: 월별/요일별 지출 추이, 이벤트별 분포
- 지인 분석: Top 5 지인 목록, 지인별 회수율
- 회수율: 전체 회수율, 미회수 지인 목록 및 장기 미회수 경고

<br>

## ERD
<img width="700" src="https://github.com/user-attachments/assets/39d7b989-942d-4190-9759-01485ae9dbf2" />

| 엔티티 | 설명 |
|---|---|
| `Member` (사용자) | 시스템 사용자 정보 관리 |
| `Event` (경조사) | 경조사 일정 정보 관리, Member가 생성(1:N), 주최자 여부(is_owner)로 역할 구분 |
| `Event_Acquaintance` | 경조사와 지인의 N:M 관계를 위한 중간 테이블 |
| `Acquaintance` (지인) | 지인 정보 관리, Member가 생성(1:N), relation으로 관계 유형 분류 |
| `Gift_Log` (경조사비) | 경조사비 기록 관리, 특정 Event_Acquaintance에 종속(1:N) |

<br>

## API 설계
![Swagger](https://github.com/user-attachments/assets/a0897962-ff54-4de5-9207-1092c0b0f6db)

<br>

## 테스트
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/1463c678-98aa-45d9-a111-dcaab29e8aef" />

<br>

## 화면 미리보기

| ![랜딩페이지](https://github.com/user-attachments/assets/f1ec6600-bdd5-4b61-86c3-a74bc24085ab) | ![회원가입](https://github.com/user-attachments/assets/761fa05b-37e9-4e62-a059-94667edd1e04) | ![로그인](https://github.com/user-attachments/assets/d584545a-82a3-4de5-8f59-252751e774ff) |
|---|---|---|
| 랜딩페이지 | 회원가입 | 로그인 |
| ![이벤트등록](https://github.com/user-attachments/assets/bb1d3b68-e1b7-405f-99ba-49f6482c7ece) | ![이벤트조회](https://github.com/user-attachments/assets/b740a320-d62b-4e45-b867-4fa3a13e7c78) | ![이벤트상세](https://github.com/user-attachments/assets/d0076a82-0061-4fe1-a889-9e2e6a7ed76d) |
| 이벤트 등록 | 이벤트 조회 | 이벤트 상세 |
| ![대시보드](https://github.com/user-attachments/assets/0c6379e0-e89a-4b6e-8694-1ccb23546bf4) | ![지출패턴](https://github.com/user-attachments/assets/535b6cb6-8ae7-4862-9b47-182600a1ffcf) | ![지인분석](https://github.com/user-attachments/assets/005c253e-e36f-4196-95c1-79a66a0a313e) |
| 종합 대시보드 | 지출 패턴 | 지인 분석 |


