[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/W5CdCei3)
# Final Project for CS 5004 - (APPLICATION NAME/Update)

(remove this and add your sections/elements)
This readme should contain the following information: 

* The group member's names and link to their personal githubs
* The application name and a brief description of the application
* Links to design documents and manuals
* Instructions on how to run the application

Ask yourself, if you started here in the readme, would you have what you need to work on this project and/or use the application?  

## Run (CLI vs HTTP API)

- **Interactive console (original course driver):** `./gradlew run` — uses `ui.Main` → `SmartFridgeApp`.
- **Spring Boot REST API (for Flutter / other clients):** `./gradlew bootRun` — listens on **port 8080** (`api.SmartFridgeApiApplication`).
  - Example: `GET http://127.0.0.1:8080/api/inventory`
  - CORS is open for development (`/api/**`).

### Flutter app (`hello_flutter`)

- Start the API first (`bootRun`).
- Default base URL: `http://127.0.0.1:8080` (iOS Simulator / desktop). **Android Emulator** uses `http://10.0.2.2:8080` (see `lib/config/api_config.dart`).
- Override: `flutter run --dart-define=SMARTFRIDGE_API=http://YOUR_LAN_IP:8080`

Added two last sections as reminders that are needed for homework.

## LLM Disclosure 
Use chatGPT and cursor to fix frontend
Because I don't build component, so I find a lot of example and open source on github and flutter official document
I use chatgpt and search by myself to find some component, and I put them to cursor, describe the detail make it try to understand my figma frame
I use chatGPT give me some file name and variable name, because give them a name will spend me a lot of time
I use chatGPT generate some JavaDoc, and help me write comment, because my grammar is terrible
I use chatGPT to write restAPI, because I don't know how to write it


## References

Official references:
* StatefulWidget:
* https://api.flutter.dev/flutter/widgets/StatefulWidget-class.html
* State:
* https://api.flutter.dev/flutter/widgets/State-class.html
* LayoutBuilder:
* https://api.flutter.dev/flutter/widgets/LayoutBuilder-class.html
* Scaffold:
* https://api.flutter.dev/flutter/material/Scaffold-class.html
* Drawer:
* https://api.flutter.dev/flutter/material/Drawer-class.html
* SafeArea:
* https://api.flutter.dev/flutter/widgets/SafeArea-class.html
*
* Open source reference:
* Flutter samples:
* https://github.com/flutter/samples
* material_3_demo:
* https://github.com/flutter/samples/tree/main/material_3_demo
  */