# Space Survival

## 1. 프로젝트 소개

**Space Survival**은 Android CustomView 기반의 2D 생존형 슈팅 게임이다.

플레이어는 우주선을 조작하여 화면 안을 자유롭게 이동하고, 자동으로 발사되는 총알로 적 비행선을 파괴한다. 적 비행선은 화면 가장자리에서 등장하여 플레이어를 향해 접근하며, 운석은 총알로 파괴할 수 없는 장애물로 등장한다.

플레이어는 적과 운석을 피하면서 최대한 오래 생존하고 높은 점수를 얻는 것을 목표로 한다.

본 프로젝트는 수업에서 사용한 `a2dg` 프레임워크를 기반으로 제작하였다.

---

## 2. 프로젝트 링크

| 항목 | 링크 |
|---|---|
| README.md | [README.md 링크 입력](README_URL) |
| Git Repository | [Git Repository 링크 입력](REPOSITORY_URL) |
| 2차 발표 영상 | [2차 발표 영상 링크 입력](SECOND_PRESENTATION_VIDEO_URL) |
| 1차 발표 영상 | [1차 발표 영상 링크 입력](FIRST_PRESENTATION_VIDEO_URL) |
| 1차 발표 당시 README.md | [1차 README 링크 입력](FIRST_README_URL) |

---

## 3. 수업 코드 및 프레임워크 활용

본 프로젝트는 수업에서 사용한 `a2dg` 프레임워크를 별도 모듈로 추가하여 사용하였다.

프로젝트 구조는 다음과 같다.

```text
SpaceSurvival
 ├─ app   // 실제 게임 로직
 └─ a2dg  // 수업에서 사용한 2D 게임 프레임워크
```

`a2dg` 모듈에서 사용한 주요 구조는 다음과 같다.

| 구성 요소 | 역할 |
|---|---|
| `BaseGameActivity` | 게임 화면을 실행하는 기본 Activity |
| `GameView` | CustomView 기반 게임 화면 |
| `GameContext` | Context, 리소스, 화면 크기, Scene 정보 관리 |
| `Scene` | 게임 장면 단위 |
| `World` | GameObject를 Layer 단위로 관리 |
| `IGameObject` | `update()` / `draw()`를 가지는 게임 오브젝트 인터페이스 |
| `Sprite` | Bitmap 이미지를 화면에 출력하는 기본 클래스 |
| `IBoxCollidable` | 사각형 충돌 판정을 위한 인터페이스 |
| `IRecyclable` | 제거된 오브젝트 재활용을 위한 인터페이스 |
| `VertScrollBackground` | 반복 스크롤 배경 처리 |

`app` 모듈에서는 위 프레임워크를 기반으로 `Player`, `Bullet`, `Enemy`, `Asteroid` 등의 게임 오브젝트를 구현하였다.

---

## 4. 최초 개발 계획

프로젝트는 다음과 같은 8주 개발 계획을 기준으로 진행한다.

| 주차 | 계획 내용 |
|---|---|
| 1주차 | Android Studio 프로젝트 구성, CustomView 기반 게임 화면 생성, 기본 게임 루프 구성, 플레이어 우주선 이동 구현 |
| 2주차 | GameObject class/interface, Sprite, MainGame class 구조 설계, 플레이어/적/장애물/아이템 클래스 분리, 터치 입력 처리 적용 |
| 3주차 | 자동 공격 시스템 구현, 파괴 가능한 적 드론 생성 시스템 구현, 적 처치 판정 및 점수 증가 처리 |
| 4주차 | 파괴 불가능한 운석 장애물 생성 구현, 충돌 판정 및 체력 감소 처리, 생존 시간 기반 난이도 증가 적용 |
| 5주차 | Object Lifecycle Management(recycle) 구조 적용, Score / Font Drawing 기반 UI 표시, 배경 레이어링 및 스크롤 처리 |
| 6주차 | 시작 화면, 인게임 화면, 게임오버 화면 구성, Multiple Scene 기반 화면 전환 구현, 재시작 기능 추가 |
| 7주차 | 아이템 시스템 구현, 회복 아이템 및 보호막 아이템 적용, 전체 밸런스 조정 및 난이도 조절 |
| 8주차 | 버그 수정 및 최종 점검, 리소스 정리, 발표 영상 녹화 및 README 최종 보완 |

---

## 5. 현재까지의 진행 상황

2차 발표 시점에서는 1~4주차의 핵심 기능을 중심으로 구현하였다.  
5주차 이후 항목은 일부 선행 구현된 부분과 최종 발표까지 보완할 부분으로 구분하였다.

| 항목 | 진행률 | 설명 |
|---|---:|---|
| Android Studio 프로젝트 구성 | 100% | 새 Android 프로젝트 생성 및 패키지 구성 완료 |
| CustomView 기반 게임 화면 생성 | 100% | `a2dg`의 `BaseGameActivity`, `GameView` 구조 사용 |
| 기본 게임 루프 구성 | 100% | `Scene`, `World`, `IGameObject` 기반으로 `update()` / `draw()` 처리 |
| 플레이어 우주선 이동 구현 | 100% | 터치 위치로 자유 이동하고 이동 방향에 따라 회전 |
| GameObject / Sprite 구조 설계 | 100% | `Player`, `Bullet`, `Enemy`, `Asteroid`를 `Sprite` 기반으로 구성 |
| 플레이어, 적, 장애물 클래스 분리 | 100% | `Player`, `Enemy`, `Asteroid` 클래스를 분리하여 구성 |
| 아이템 클래스 분리 | 0% | 7주차 구현 예정 |
| 터치 입력 처리 | 100% | `MainScene`에서 터치 이벤트를 받아 `Player`에 전달 |
| 자동 공격 시스템 | 100% | 플레이어가 바라보는 방향으로 총알 자동 발사 |
| 파괴 가능한 적 생성 시스템 | 100% | 적 비행선이 화면 가장자리에서 생성되어 플레이어 방향으로 이동 |
| 적 처치 판정 및 점수 증가 처리 | 100% | `Bullet`과 `Enemy` 충돌 시 체력 감소, 파괴 시 점수 증가 |
| 파괴 불가능한 운석 장애물 | 100% | `Asteroid`는 총알로 파괴되지 않고 회피 대상 역할 |
| 충돌 판정 및 체력 감소 처리 | 100% | `Player-Enemy`, `Player-Asteroid` 충돌 시 HP 감소 |
| 생존 시간 기반 난이도 증가 | 80% | wave 증가에 따라 적과 운석의 속도 증가 적용 |
| Object Lifecycle Management(recycle) | 70% | `IRecyclable`, `world.obtain()` 구조를 일부 오브젝트에 적용 |
| Score / Font Drawing 기반 UI | 80% | `Score` 클래스를 통해 점수와 HP를 텍스트로 표시 |
| 배경 레이어링 및 스크롤 처리 | 90% | `VertScrollBackground`를 이용하여 우주 배경 스크롤 적용 |
| Multiple Scene 기반 화면 전환 | 20% | 현재는 `MainScene` 중심 구현, Start/GameOver Scene은 이후 확장 예정 |
| 재시작 기능 | 0% | 6주차 구현 예정 |
| 아이템 시스템 | 0% | 7주차 구현 예정 |
| 전체 밸런스 조정 | 50% | 적, 총알, 운석 크기와 생성 빈도 일부 조정 |
| 리소스 정리 | 70% | 배경, 플레이어, 적, 운석, 총알 이미지 적용. 일부 리소스는 추가 개선 예정 |

---

## 6. 주차별 개발 진행 현황

| 주차 | 계획 내용 | 현재 상태 |
|---|---|---|
| 1주차 | Android Studio 프로젝트 구성, CustomView 기반 게임 화면 생성, 기본 게임 루프 구성, 플레이어 우주선 이동 구현 | 완료 |
| 2주차 | GameObject / Sprite 구조 설계, 클래스 분리, 터치 입력 처리 | 대부분 완료. 아이템 클래스는 7주차 구현 예정 |
| 3주차 | 자동 공격 시스템, 파괴 가능한 적 드론 생성, 적 처치 판정 및 점수 증가 | 완료 |
| 4주차 | 파괴 불가능한 운석 장애물, 충돌 판정 및 체력 감소, 생존 시간 기반 난이도 증가 | 완료 |
| 5주차 | recycle 구조, Score / Font Drawing UI, 배경 레이어링 및 스크롤 | 일부 선행 구현 완료. 세부 정리는 추가 예정 |
| 6주차 | 시작 화면, 인게임 화면, 게임오버 화면, Multiple Scene 전환, 재시작 | 이후 구현 예정 |
| 7주차 | 회복 아이템, 보호막 아이템, 전체 밸런스 조정 | 이후 구현 예정 |
| 8주차 | 버그 수정, 리소스 정리, 발표 영상 녹화, README 최종 보완 | 최종 발표 전 진행 예정 |

---

## 7. Git Commit 관리

### GitHub Insights Commits

아래 이미지는 GitHub Insights의 commits 화면이다.

![github-insights-commits](./docs/github-insights-commits.png)

### 주별 Commit 수

| 주차 | 주요 작업 | Commit 수 |
|---|---|---:|
| 1주차 | 프로젝트 생성, `a2dg` 프레임워크 연결, 기본 게임 구조 구성 | 숫자 입력 |
| 2주차 | GameObject / Sprite 구조 정리, 클래스 분리, 터치 입력 처리 | 숫자 입력 |
| 3주차 | 자동 공격, 적 생성, 충돌 처리, 점수 증가 구현 | 숫자 입력 |
| 4주차 | 운석 장애물, HP 처리, 난이도 증가, 이미지 리소스 정리 | 숫자 입력 |

---

## 8. 목표 변경 사항

운석과 아이템 요소는 최초 계획에 포함되어 있었지만, 2차 발표 시점에서는 운석 장애물까지 우선 구현하였다. 
아이템 시스템은 이후 회복 아이템과 보호막 아이템을 중심으로 추가할 예정이다.
아이템 및 적 클래스 추가 구현을 조금 더 앞당길 예정

---

## 9. Activity 구성

| Activity | 역할 |
|---|---|
| `MainActivity` | `BaseGameActivity`를 상속하여 게임 화면을 실행한다. `createRootScene()`에서 `MainScene`을 생성한다. |

`MainActivity`는 별도의 XML 화면을 사용하는 일반 Activity가 아니라, 수업 프레임워크의 `BaseGameActivity`를 기반으로 게임 화면을 실행한다.

```text
MainActivity
   ↓
BaseGameActivity
   ↓
GameView
   ↓
MainScene
```

---

## 10. Scene 구성 및 전환 관계

2차 발표 시점에서는 `MainScene` 중심으로 구현되어 있다.

```text
MainActivity
   ↓
MainScene
   ↓
HP가 0이 되면 Player 동작 정지
```

현재는 게임 시작 후 바로 `MainScene`으로 진입한다.  
최종 버전에서는 다음과 같이 Scene을 확장할 예정이다.

```text
StartScene
   ↓
MainScene
   ↓
GameOverScene
   ↓
Restart
```

---

## 11. MainScene에 등장하는 GameObject 구성

| GameObject | Class | 그림 구성 | 동작 구성 | 상호작용 |
|---|---|---|---|---|
| 플레이어 | `Player` | `player_ship.png` | 터치 위치로 자유 이동, 이동 방향으로 회전 | `Enemy`, `Asteroid`와 충돌 시 HP 감소 |
| 총알 | `Bullet` | `bullet.png` | 플레이어가 바라보는 방향으로 자동 발사 | `Enemy`와 충돌 시 Enemy 체력 감소 |
| 적 비행선 | `Enemy` | `enemy_drone.png` | 화면 가장자리에서 생성되어 Player 방향으로 이동 | `Bullet`에 맞으면 체력 감소, Player와 충돌 시 피해 |
| 적 생성기 | `EnemyGenerator` | 화면에 직접 그리지 않음 | 일정 시간마다 Enemy 생성 | wave 증가에 따라 적 속도 증가 |
| 운석 | `Asteroid` | `asteroid.png` | 화면 가장자리에서 직선 이동 | Player와 충돌 시 큰 피해, Bullet으로 파괴 불가 |
| 운석 생성기 | `AsteroidGenerator` | 화면에 직접 그리지 않음 | 낮은 빈도로 Asteroid 생성 | 생존 시간이 길수록 운석 속도 증가 |
| 충돌 검사기 | `CollisionChecker` | 화면에 직접 그리지 않음 | 오브젝트 간 충돌 검사 | `Bullet-Enemy`, `Player-Enemy`, `Player-Asteroid` 처리 |
| 점수/UI | `Score` | 텍스트 UI | 점수와 HP 표시 | Enemy 처치, Player HP 상태 반영 |
| 배경 | `VertScrollBackground` | `space_background.png` | 세로 방향 반복 스크롤 | 게임 진행 배경 역할 |

---

## 12. 핵심 코드 설명

### Player

`Player`는 터치 입력을 받아 목표 좌표를 저장하고, 매 프레임 해당 위치를 향해 이동한다.  
이동 방향을 계산하여 플레이어 우주선을 회전시키고, 일정 시간마다 현재 바라보는 방향으로 `Bullet`을 생성한다.

핵심 책임은 다음과 같다.

- 터치 입력 처리
- 자유 이동
- 방향 회전
- 자동 공격
- HP 관리
- 충돌 범위 제공

### Bullet

`Bullet`은 생성 시점에 플레이어의 방향 벡터를 전달받아 해당 방향으로 이동한다.  
화면 밖으로 나가면 `World`에서 제거되며, `Enemy`와 충돌하면 `Enemy`의 체력을 감소시킨다.

핵심 책임은 다음과 같다.

- 발사 방향 유지
- 방향에 따른 이동
- 충돌 범위 제공
- 화면 밖으로 나갈 경우 제거

### Enemy

`Enemy`는 화면 가장자리에서 생성되어 플레이어 방향으로 접근한다.  
체력을 가지고 있으며, `Bullet`에 맞으면 체력이 감소한다.  
체력이 0 이하가 되면 제거되고 점수를 제공한다.

핵심 책임은 다음과 같다.

- 화면 가장자리에서 등장
- 플레이어 방향으로 이동
- 체력 관리
- 파괴 시 점수 제공

### EnemyGenerator

`EnemyGenerator`는 일정 시간마다 적 비행선을 생성한다.  
생존 시간이 길어질수록 wave가 증가하며, 이에 따라 적의 이동 속도가 증가한다.

핵심 책임은 다음과 같다.

- 적 생성 주기 관리
- 적 생성 위치 결정
- 플레이어 방향 벡터 계산
- wave 기반 난이도 증가

### Asteroid

`Asteroid`는 파괴 불가능한 장애물이다.  
총알과 충돌해도 제거되지 않으며, 플레이어와 충돌하면 HP를 크게 감소시킨다.

핵심 책임은 다음과 같다.

- 화면 가장자리에서 등장
- 직선 이동
- 플레이어 충돌 시 피해
- 총알로 파괴되지 않는 장애물 역할

### AsteroidGenerator

`AsteroidGenerator`는 운석을 낮은 빈도로 생성한다.  
운석은 화면을 지나치게 복잡하게 만들지 않기 위해 한 번에 적은 수만 생성되며, 시간이 지날수록 속도가 증가한다.

핵심 책임은 다음과 같다.

- 운석 생성 주기 관리
- 운석 생성 위치와 이동 방향 결정
- 생존 시간 기반 속도 증가

### CollisionChecker

`CollisionChecker`는 `MainScene` 안의 주요 충돌을 처리한다.

처리하는 충돌은 다음과 같다.

| 충돌 | 처리 |
|---|---|
| `Bullet - Enemy` | Bullet 제거, Enemy 체력 감소 |
| `Player - Enemy` | Player HP 감소, Enemy 제거 |
| `Player - Asteroid` | Player HP 감소, Asteroid 제거 |
| `Bullet - Asteroid` | 처리하지 않음. Asteroid는 파괴 불가능한 장애물 |

### Score

`Score`는 현재 점수와 플레이어 HP를 화면에 표시한다.  
적을 처치하면 점수가 증가하고, 충돌로 인해 HP가 감소하면 UI에 반영된다.

---

## 13. UX 진행 방법

1. 앱을 실행하면 바로 `MainScene`이 시작된다.
2. 플레이어는 화면을 터치하거나 드래그하여 우주선을 이동시킨다.
3. 우주선은 이동 방향을 바라보며 자동으로 총알을 발사한다.
4. 적 비행선은 화면 가장자리에서 등장하여 플레이어를 향해 이동한다.
5. 총알로 적 비행선을 맞추면 적의 체력이 감소한다.
6. 적 비행선의 체력이 0이 되면 적이 제거되고 점수가 증가한다.
7. 운석은 파괴할 수 없으므로 피해야 한다.
8. 적 비행선이나 운석에 충돌하면 HP가 감소한다.
9. HP가 0이 되면 플레이어 동작이 정지한다.

---

## 14. 구현하면서 어려웠던 부분

- `a2dg` 프레임워크를 새 프로젝트에 모듈로 연결하면서 Gradle 설정과 의존성 문제를 해결해야 했다.
- 이미지 리소스의 투명 배경 문제로 인해 실제 게임 화면에서 흰 배경이 함께 출력되는 문제가 있었다.
- 적, 총알, 운석이 한 화면에 동시에 등장하면서 화면이 복잡해져 크기와 생성 빈도를 조정해야 했다.