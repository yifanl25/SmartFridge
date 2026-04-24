# Java Architecture

This Java source tree is the backend/support side of SmartFridge.

Official product direction:

- `hello_flutter` = official frontend
- `api/web/*ApiController` = official HTTP entry layer
- `controller/*` = internal coordination / facade layer
- `service/*` = business logic and session state
- `model/*` = data model layer
- `ui/*` = legacy console/demo layer kept for backward compatibility

Main runtime chain:

`Flutter -> FridgeApiService -> api/web/*ApiController -> controller/* -> service/* -> model/*`

Why both `api/web` and `controller` exist:

- `api/web` owns Spring Boot annotations, routes, and HTTP request/response handling
- `controller/*` preserves the older Java module boundaries and stays reusable from both HTTP and console flows
- `service/*` remains the place for business rules instead of duplicating logic in HTTP classes

This is a transitional architecture by design: it keeps the project explainable without forcing a large destructive refactor.
