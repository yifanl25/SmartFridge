[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/W5CdCei3)

# CS 5004 Final Project — **SmartFridge**

Console + Spring Boot REST + optional **Flutter** client: manage a fridge inventory, preferences, recipe recommendations, and a grocery list (in-memory session; no database).

---

## Group members & GitHub

| Name | GitHub |
|------|--------|
| Zhou Duanxi | *(add GitHub link)* |
| Wenqi | *(add GitHub link)* |

---

## Design documents & manuals

| Resource | Path in this repo |
|----------|-------------------|
| Design / architecture notes | [`DesignDocuments/README.md`](DesignDocuments/README.md) |
| Manual | [`Manual/README.md`](Manual/README.md) |
| Course instructions (GitHub Projects, expectations, …) | [`Instructions/`](Instructions/) |

---

## How to run

### Backend (Spring Boot API)

From the **project root** (`finalproject-i-love-java`):

```bash
./gradlew bootRun
```

- Serves REST on **`http://127.0.0.1:8080`**
- Main class: `api.SmartFridgeApiApplication`
- Example: `GET http://127.0.0.1:8080/api/inventory`
- CORS is open for local development (`/api/**`).
- Data is stored in memory for demo purposes and resets when the backend restarts.

**Interactive console only** (no HTTP):

```bash
./gradlew run
```

Uses `ui.Main` → `SmartFridgeApp` → `ConsoleApp`.

**Tests:**

```bash
./gradlew test
```

---

### Flutter app (`hello_flutter/`)

1. Start the API first (`./gradlew bootRun`).
2. In another terminal:

```bash
cd hello_flutter
flutter pub get
flutter run
```

Make sure the backend is running before opening recipe details or using grocery actions in Flutter.

---

### Default API base URL (Flutter)

| Environment | Base URL |
|-------------|----------|
| iOS Simulator, macOS/desktop, **same machine** as `bootRun` | `http://127.0.0.1:8080` |
| **Android Emulator** (host loopback) | `http://10.0.2.2:8080` |

Configured in `hello_flutter/lib/config/api_config.dart`.

**Override** (e.g. phone on same Wi‑Fi as laptop):

```bash
flutter run --dart-define=SMARTFRIDGE_API=http://YOUR_LAN_IP:8080
```

---

## Suggested demo flow

1. Set a health preference.
2. View and add fridge inventory items.
3. Open recipe recommendations.
4. Open one recipe detail.
5. Add missing ingredients to grocery list.
6. Review grocery list and checkout.

---

## LLM disclosure

*(If your course requires a statement about AI-assisted work, add it here.)*

---

## References

- Flutter framework and SDK documentation: https://docs.flutter.dev/
- Spring Boot documentation (2.7.x): https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/
- Jackson project documentation: https://github.com/FasterXML/jackson
- Course starter/project materials in this repository: `Instructions/`
