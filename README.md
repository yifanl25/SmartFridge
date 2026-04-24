[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/W5CdCei3)

# Final Project for CS 5004 - SmartFridge

* The group member's names and link to their personal githubs
    * Duanxi Zhou: https://github.com/Chickenzdx
    * Wenqi Hao: https://github.com/Wenqi0211
    * Yufan Li: https://github.com/yufanli673-art
    * Yifan Lin: https://github.com/yifanl25

* The application name and a brief description of the application
  SmartFridge is an application designed to help users manage their refrigerator inventory efficiently and
  generate recipes. The application follows the MVC (Model-View-Controller) architecture and allows users to have
  personalized meal planning, grocery planning, fridge inventory management, health reminders, recipe recommendations,
  and community motivation.

* Links to design documents and manuals
  https://docs.google.com/document/d/1FQztXCLkvf-WmPtpMuK_digAThjMJHzoUvuYCaVixUE/edit?usp=sharing
* Instructions on how to run the application
    * Project link: https://github.com/5004-SEA-GEENG/finalproject-i-love-java.git

## Run (CLI vs HTTP API)

- **Interactive console (original course driver):** `./gradlew run` — uses `ui.Main` → `SmartFridgeApp`.
- **Spring Boot REST API (for Flutter / other clients):** `./gradlew bootRun` — listens on **port 8080** (
  `api.SmartFridgeApiApplication`).
    - Example: `GET http://127.0.0.1:8080/api/inventory`
    - CORS is open for development (`/api/**`).

### Flutter app (`hello_flutter`)

- Start the API first (`bootRun`).
- Default base URL: `http://127.0.0.1:8080` (iOS Simulator / desktop). **Android Emulator** uses
  `http://10.0.2.2:8080` (see `lib/config/api_config.dart`).
- Override: `flutter run --dart-define=SMARTFRIDGE_API=http://YOUR_LAN_IP:8080`

Added two last sections as reminders that are needed for homework.

## LLM Disclosure

We used LLM to help design the recipe scoring logic for the recommendation feature by providing it with our recipes.json
and asking it to suggest a scoring approach. We also used Claude to generate two JSON files: recipes and grocery inventory,
to simulate the data needed for the application pages.

In terms of coding, we used LLM to generate starter code for each class after designing the UML ourselves, which we
then built on and modified. It also helped us identify additional classes needed to connect the front end with Flutter.
Finally, we used LLM to generate edge cases for unit tests and to understand how to integrate external APIs.

## References
