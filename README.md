<p align="center">
  <a href="https://developer.android.com" target="_blank">
    <img src="https://raw.githubusercontent.com/kristerkari/react-native-svg-transformer/master/images/android-logo.svg" width="120" alt="Android Logo">
    <h1 align="center">Android Development 2026</h1>
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/OS-Android%2015%2B-3D?style=for-the-badge&logo=android&logoColor=white&color=3DDC84" alt="Platform">
  <img src="https://img.shields.io/badge/Java-11-blue?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/SDK-36-orange?style=for-the-badge&logo=android-studio&logoColor=white" alt="SDK">
  <img src="https://img.shields.io/badge/Database-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" alt="SQLite">
</p>

---

# App Móvil "Mundo Inclusivo" - Registro & Sincronización
### Módulo Móvil del Proyecto de Grado | UMSA

Esta aplicación móvil es el componente de campo del sistema integral desarrollado para **Cáritas Coroico**. Su función principal es permitir que los técnicos registren datos de beneficiarios en áreas rurales sin cobertura de red, garantizando que la información nunca se pierda.

## Funcionalidad Estrella: Sincronización Inteligente
La aplicación implementa una arquitectura **Offline-First**:
1. **Captura en Campo:** Los datos se guardan localmente en una base de datos **SQLite**.
2. **Detección de Red:** El sistema detecta automáticamente cuando el dispositivo tiene acceso a internet.
3. **Sincronización:** Mediante la librería **Volley**, la App envía los registros pendientes al servidor central (Laravel) mediante una API REST.

## Especificaciones Técnicas
* **Lenguaje:** Java 11
* **Lógica de Red:** Volley (Peticiones HTTP/JSON)
* **Persistencia Local:** SQLite
* **Interfaz de Usuario:** Material Design Components (Google)
* **Compatibilidad:** Min SDK 28 (Android 9) / Target SDK 36 (Android 15)

## Estructura del Código
* `app/src/main/java`: Lógica de negocio, adaptadores de base de datos y controladores de Volley.
* `app/src/main/res`: Recursos visuales, layouts responsivos y estilos Material.
* `build.gradle`: Configuración de dependencias y versiones de SDK.

## Instalación y Uso
1. Clonar el repositorio: `git clone https://github.com/hansel-bustamante/mundo-inclusivo-android.git`
2. Abrir el proyecto en **Android Studio (Ladybug o superior)**.
3. Configurar la URL del servidor en la clase de constantes (ajustar la IP de tu servidor Laravel).
4. Sincronizar Gradle y ejecutar en un emulador o dispositivo físico.

---
**Desarrollado por:** Hansel Alain Bustamante Callisaya  
*Mención Ingeniería de Sistemas Informáticos - Universidad Mayor de San Andrés*
