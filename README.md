
## 프로젝트 제목
**Star Survivor**

## 발표 영상 자료에 대한 링크
- https://youtu.be/7MpUPW3_Ar8

## 프로젝트 Git Repository 에 대한 링크
- https://github.com/LeeYlyw/StarSurvivor

## README.md 에 대한 링크
- https://github.com/LeeYlyw/StarSurvivor/blob/main/README.md

---

## 프로젝트 개요
본 프로젝트는 스마트폰 프로그래밍 수업에서 다루는 게임 제작 기술을 응용하여 제작하는 **2D 우주 생존 액션 게임**이다.  
수업 Git repo의 README를 참고하여, 이번 학기에 다루게 되는 **CustomView 기반 게임 화면**, **게임 루프와 시간 기반 이동**, **GameObject / Sprite / MainGame 구조**, **터치 이벤트 처리**, **프레임 애니메이션**, **적 생성 및 오브젝트 재활용**, **점수 표시**, **배경 레이어링 및 스크롤**, **충돌 처리**, **다중 씬 구성**을 중심으로 개발 범위를 설정하였다.

---

## 게임 컨셉

### High Concept
플레이어는 우주 공간에서 우주선을 조작하여 몰려오는 적 드론과 장애물을 피하면서 최대한 오래 생존해야 한다.  
적 드론은 공격으로 파괴할 수 있지만, 장애물인 운석은 파괴할 수 없으므로 회피해야 한다.  
시간이 지날수록 적과 장애물의 수가 증가하며, 플레이어는 이동, 자동 공격, 아이템 획득을 통해 더 오래 생존해야 한다.

### 핵심 메카닉
- 터치 또는 드래그를 이용한 우주선 이동
- 자동 발사되는 기본 공격
- 파괴 가능한 적 드론과 파괴 불가능한 운석 장애물의 구분
- 적 처치 시 점수 획득
- 적 또는 장애물과 충돌 시 체력 감소
- 아이템 획득을 통한 회복 또는 일시적 강화
- 체력이 0이 되면 게임오버

---

## 상세 개발 범위

### 개발 주요 요소
- 플레이어 우주선 1종
- 파괴 가능한 일반 적 3종
- 파괴 가능한 특수 적 1종
- 파괴 불가능한 운석 장애물 2종
- 무기 2종
  - 기본 자동 발사
  - 강화 발사
- 아이템 2종
  - 체력 회복 아이템
  - 보호막 아이템
- 배경 레이어 2단
  - 우주 배경
  - 별 스크롤 배경
- UI 화면 3개
  - 시작 화면
  - 게임 화면
  - 게임오버 화면
- 점수 시스템 1종
- 체력 시스템 1종
- 생존 시간 기반 난이도 증가 시스템 1종

### 수업 내용 반영 요소
- CustomView 활용
- postDelayed(), Choreographer를 이용한 게임 루프 구성
- Frame Time, FPS, Move based time 기반의 시간 처리
- GameObject class/interface, Sprite, MainGame class 구조 활용
- Touch Event handling 기반 플레이어 조작
- Frame Animation 적용
- EnemyGenerator를 응용한 적 생성 시스템
- Object Lifecycle Management (recycle) 구조 활용
- Score / Font Drawing 기반 점수 표시
- Background Layering (Parallax Scroll) 및 스크롤 배경 처리
- Collision Check / Collision Handling 적용
- Multiple Scene 기반의 시작 / 인게임 / 게임오버 화면 구성

---

## 예상 게임 실행 흐름

### 1. 시작 화면
<img width="507" height="495" alt="t1" src="https://github.com/user-attachments/assets/376aba37-4669-4f21-b1aa-801bbcf4dd6f" />

- 게임 제목과 시작 버튼이 표시된다.
- 사용자는 버튼을 눌러 게임을 시작할 수 있다.

### 2. 게임 시작
<img width="509" height="497" alt="t2" src="https://github.com/user-attachments/assets/92c1d152-ac8e-4ab0-ae8b-830a2caaa93e" />

- 플레이어 우주선이 화면 하단에서 이동하며 적 드론과 운석을 회피한다.
- 점수, 체력, 생존 시간이 화면에 표시된다.

### 3. 생존 플레이
<img width="1024" height="578" alt="t3" src="https://github.com/user-attachments/assets/2ba26547-d39e-4eae-aceb-cd3a71355e1d" />

- 적 드론은 자동 공격으로 파괴할 수 있다.
- 운석은 파괴할 수 없으므로 회피가 필요하다.

### 4. 아이템 획득 및 강화
<img width="1024" height="578" alt="t3" src="https://github.com/user-attachments/assets/7ec48818-474c-41a0-a720-743bc5f68832" />

- 체력 회복 아이템 또는 보호막 아이템을 획득할 수 있다.
- 아이템을 활용하여 더 오래 생존할 수 있다.

### 6. 게임오버
<img width="1024" height="1536" alt="ChatGPT Image 2026년 4월 6일 오전 08_59_46" src="https://github.com/user-attachments/assets/18d2294e-3c42-44b7-a3e1-74f2030f08d6" />

- 체력이 0이 되면 게임오버 화면으로 전환된다.
- 최종 점수와 생존 시간을 확인하고 다시 시작할 수 있다.

## 개발 일정
**4월 6일에 시작하는 주를 1주차로 하여 8주간의 주단위 상세 개발 일정**

### 1주차
- Android Studio 프로젝트 구성
- CustomView 기반 게임 화면 생성
- 기본 게임 루프 구성
- 플레이어 우주선 이동 구현

### 2주차
- GameObject class/interface, Sprite, MainGame class 구조 설계
- 플레이어, 적, 장애물, 아이템 클래스 분리
- 터치 입력 처리 적용

### 3주차
- 자동 공격 시스템 구현
- 파괴 가능한 적 드론 생성 시스템 구현
- 적 처치 판정 및 점수 증가 처리

### 4주차
- 파괴 불가능한 운석 장애물 생성 구현
- 충돌 판정 및 체력 감소 처리
- 생존 시간 기반 난이도 증가 적용

### 5주차
- Object Lifecycle Management (recycle) 구조 적용
- Score / Font Drawing 기반 UI 표시
- 배경 레이어링 및 스크롤 처리

### 6주차
- 시작 화면, 인게임 화면, 게임오버 화면 구성
- Multiple Scene 기반 화면 전환 구현
- 재시작 기능 추가

### 7주차
- 아이템 시스템 구현
- 회복 아이템, 보호막 아이템 적용
- 전체 밸런스 조정 및 난이도 조절

### 8주차
- 버그 수정 및 최종 점검
- 리소스 정리
- 발표 영상 녹화 및 README 최종 보완
