# VINNY - Backend

> 취향 기반 빈티지샵 탐색 & 스타일 공유 서비스의 백엔드 저장소입니다.
> 

---

---

## 🛠️ 개발 환경

- **Language**: Java 17
- **Framework**: Spring Boot 3.5.3
- **Build Tool**: Gradle
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Infra**: AWS EC2, RDS
- **API 문서화**: Swagger (springdoc-openapi)

---

## 📁 폴더 구조 (도메인형 DDD 기반)

```
📂 backend
├── 📂 Member
│   ├── 📂 controller      # HTTP 요청 처리
│   ├── 📂 service         # 비즈니스 로직
│   ├── 📂 repository      # DB 접근
│   ├── 📂 domain          # 핵심 엔티티
│   └── 📂 dto             # 요청/응답 데이터 구조
│
├── 📂 Shop
├── 📂 Auth
├── 📂 Common             # 공통 로직 (예외, 응답 Wrapper 등)
└── 📂 Config             # 설정 파일

```

---

## 🔖 브랜치 전략 (GitHub Flow 기반)

- `main`: 제품 출시 브랜치 (배포 대상)
- `develop`: 통합 개발 브랜치 (optional)
- `feat/feature-name`: 기능 개발용 브랜치
- `fix/bug-name`: 버그 수정 브랜치
- `hotfix/critical-name`: 긴급 수정 브랜치

---

## 💬 PR 규칙

- 제목 형식: `✨ [Feat] 로그인 API 구현`
- 템플릿 기반 작성 (유형 / 작업 내용 / 리뷰 포인트 등)

| 태그 | 설명 |
| --- | --- |
| ✨ [Feat] | 새로운 기능 추가 |
| 🐛 [Fix] | 버그 수정 |
| 🎨 [Design] | API 응답 포맷/UI 관련 수정 |
| 📝 [Docs] | 문서 수정 (README 등) |
| 🔧 [Chore] | 설정 변경, 의존성 관리 등 |
| 🚀 [Hotfix] | 배포 중 긴급 수정 |

---

## ✅ 커밋 컨벤션

```bash
<태그>: <제목>

- 작업 내용 상세
- 작업 내용 상세2

#이슈번호 (optional)

```

### 예시

- `feat: 회원가입 API 구현`
- `fix: 회원가입 시 닉네임 중복 오류 수정`

---

## 💠 Gitmoji 가이드

| Gitmoji | 태그 | 설명 |
| --- | --- | --- |
| ✨ | feat | 새로운 기능 |
| 🐛 | fix | 버그 수정 |
| ♻️ | refactor | 리팩토링 |
| 🎨 | style | 코드 스타일 변경 |
| 📝 | docs | 문서 수정 |
| 🔧 | chore | 설정 변경 |
| ✅ | test | 테스트 코드 |
| 🚀 | hotfix | 긴급 배포 이슈 |
| 🔀 | merge | 브랜치 병합 |

---

## 🧾 Database 규칙

- **테이블 명**: `lower_snake_case`
- **PK 컬럼명**: `id`
- **기본 날짜 컬럼**: `created_at`, `updated_at`, `deleted_at`
- **작성자/수정자 필드**: `created_by`, `updated_by`
- **FK 명명 규칙**: `{참조테이블}_id`

---

## 📚 API 테스트 정책

- **Swagger**: 개발 중 수동 확인용
- **Mock Test**: 단위 테스트 및 CI 파이프라인 검증용

---

> 이 문서는 프로젝트 진행에 따라 계속 업데이트됩니다.
>
