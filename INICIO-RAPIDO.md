# Guía Rápida - Cronómetro de Minutos

## Opción 1: Compilación Automática en GitHub (RECOMENDADO)

Esta es la forma más fácil. GitHub Actions compilará la app automáticamente.

### Pasos:

1. **Crea un repositorio en GitHub**
   - Ve a https://github.com/new
   - Ponle un nombre (ej: "cronometro-minutos")
   - Márcalo como público o privado (tu elección)
   - NO inicialices con README, .gitignore, o licencia

2. **Sube todos estos archivos a GitHub**
   
   Desde tu computadora, en la carpeta del proyecto:
   ```bash
   git init
   git add .
   git commit -m "Primera versión del cronómetro"
   git branch -M main
   git remote add origin https://github.com/TU-USUARIO/TU-REPOSITORIO.git
   git push -u origin main
   ```
   
   (Reemplaza TU-USUARIO y TU-REPOSITORIO con tus datos)

3. **Espera a que compile**
   - Ve a tu repositorio en GitHub
   - Haz clic en la pestaña "Actions"
   - Verás un workflow ejecutándose
   - Espera 5-10 minutos a que termine

4. **Descarga el APK**
   - Una vez que el workflow termine (marca verde ✓)
   - Haz clic en el workflow completado
   - Baja hasta "Artifacts"
   - Descarga "app-release"
   - Descomprime el archivo ZIP
   - Dentro encontrarás `app-release-unsigned.apk`

5. **Instala en tu Android**
   - Transfiere el APK a tu teléfono
   - Abre el archivo en tu teléfono
   - Android te pedirá permiso para instalar apps de orígenes desconocidos
   - Acepta y instala

## Opción 2: Compilación Local

Si prefieres compilar en tu computadora:

### Requisitos:
- Android Studio instalado
- JDK 17 o superior

### Pasos:
1. Abre Android Studio
2. Selecciona "Open an Existing Project"
3. Navega a esta carpeta y ábrela
4. Espera a que Gradle sincronice (puede tardar varios minutos la primera vez)
5. Conecta tu teléfono Android o inicia un emulador
6. Presiona el botón "Run" (▶️) en Android Studio

## Solución de Problemas

### "gradle-wrapper.jar not found"

Si GitHub Actions falla con este error, el workflow ya está configurado para descargarlo automáticamente. Si sigue fallando, agrega manualmente el JAR:

```bash
# En tu computadora, ejecuta:
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar

# Luego sube el cambio:
git add gradle/wrapper/gradle-wrapper.jar
git commit -m "Agregar gradle wrapper JAR"
git push
```

### La app se cierra al abrirla

Verifica que tu Android sea versión 8.0 (API 26) o superior.

### La app no aparece en segundo plano

Asegúrate de:
1. Haber presionado "Iniciar"
2. Revisar el panel de notificaciones - debería aparecer ahí
3. Que tu Android no esté en modo de ahorro de batería agresivo

### El cronómetro se detiene en segundo plano

Algunos fabricantes (Xiaomi, Huawei, Samsung) tienen optimizaciones agresivas. Ve a:
- Configuración → Batería → Optimización de batería
- Encuentra "Cronómetro Minutos"
- Selecciona "No optimizar"

## Características de la App

✅ Cuenta solo en minutos (62:30, 145:15, etc.)
✅ Iniciar desde cualquier minuto
✅ Funciona en segundo plano
✅ Notificación permanente con el tiempo
✅ Botones: Iniciar, Pausar, Reiniciar
✅ Establecer minuto inicial

## ¿Necesitas Ayuda?

Si tienes problemas, revisa:
1. El README.md principal para detalles técnicos
2. La pestaña "Issues" en GitHub (si el repo es público)
3. Los logs en GitHub Actions para errores de compilación
