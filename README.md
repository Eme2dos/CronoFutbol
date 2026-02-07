# Cronómetro de Minutos

Aplicación Android de cronómetro que cuenta solo en minutos (no resetea a horas).

## Características

- ✅ Contador en formato minutos:segundos (62:30, 145:15, etc.)
- ✅ Iniciar desde cualquier minuto deseado
- ✅ Funcionamiento en segundo plano (con notificación permanente)
- ✅ Pausar y reiniciar
- ✅ Continúa funcionando al usar otras apps o salir al escritorio

## Compilación con GitHub Actions

Este proyecto está configurado para compilarse automáticamente mediante GitHub Actions.

### Cómo usar:

1. **Crear repositorio en GitHub**
   - Crea un nuevo repositorio en tu cuenta de GitHub
   - Puedes llamarlo "cronometro-minutos" o el nombre que prefieras

2. **Descargar gradle-wrapper.jar (IMPORTANTE)**
   ```bash
   # Antes de subir el código, descarga el gradle-wrapper.jar:
   curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar
   ```
   
   **Nota**: Este archivo es necesario para que Gradle funcione. Si no lo descargas antes, GitHub Actions fallará.

3. **Subir el código**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/TU-USUARIO/cronometro-minutos.git
   git push -u origin main
   ```
   
   **Alternativa**: Si no puedes descargar gradle-wrapper.jar localmente, puedes modificar el workflow de GitHub Actions para que lo descargue automáticamente. Agrega este paso antes de `Grant execute permission`:
   
   ```yaml
   - name: Download Gradle Wrapper
     run: |
       curl -L -o gradle/wrapper/gradle-wrapper.jar \
         https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar
   ```

3. **Compilación automática**
   - Al hacer push, GitHub Actions compilará automáticamente la app
   - Ve a la pestaña "Actions" en tu repositorio
   - Una vez completada la compilación, descarga el APK desde "Artifacts"

4. **Instalar el APK**
   - Descarga el archivo `app-release-unsigned.apk`
   - Transfiérelo a tu dispositivo Android
   - Habilita "Instalar apps de origen desconocido" si es necesario
   - Instala el APK

## Uso de la app

1. **Iniciar**: Presiona "Iniciar" para comenzar el cronómetro desde 0:00
2. **Establecer minuto inicial**: Presiona este botón para comenzar desde un minuto específico (ej: 45)
3. **Pausar**: Detiene el cronómetro sin reiniciarlo
4. **Reiniciar**: Vuelve el cronómetro a 0:00

La app seguirá funcionando en segundo plano. Verás una notificación que muestra el tiempo actual.

## Requisitos técnicos

- Android 8.0 (API 26) o superior
- Permisos: Servicio en primer plano, notificaciones

## Estructura del proyecto

- `MainActivity.kt`: Interfaz de usuario
- `CronometroService.kt`: Servicio que mantiene el cronómetro funcionando en segundo plano
- `.github/workflows/android-build.yml`: Configuración de GitHub Actions para compilación automática

## Notas importantes

⚠️ **APK sin firmar**: El APK generado no está firmado. Para distribución en Play Store necesitarías firmarlo.

⚠️ **Notificación persistente**: La app mostrará una notificación permanente mientras el servicio esté activo (esto es obligatorio en Android para servicios en primer plano).
