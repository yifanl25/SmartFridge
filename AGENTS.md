# Project Instructions

- Official frontend: `hello_flutter`
- Official backend HTTP layer: `src/main/java/api/web/*ApiController`
- `src/main/java/controller/*` are internal coordinators/facades, not web controllers
- `src/main/java/service/*` contains business logic and session state
- `src/main/java/model/*` contains data models
- `src/main/java/ui/*` is legacy console/demo code only
- Keep files when possible; prefer documentation and small consistency fixes over major refactors
- Do not add Spring MVC route annotations into `controller/*`
- Keep the main runtime chain: `Flutter -> FridgeApiService -> api/web/*ApiController -> controller/* -> service/* -> model/*`
