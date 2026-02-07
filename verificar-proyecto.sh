#!/bin/bash

echo "=================================="
echo "Verificación del Proyecto Android"
echo "=================================="
echo ""

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para verificar archivo
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1 - NO ENCONTRADO"
        return 1
    fi
}

# Función para verificar directorio
check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1/"
        return 0
    else
        echo -e "${RED}✗${NC} $1/ - NO ENCONTRADO"
        return 1
    fi
}

errors=0

echo "1. Verificando archivos de configuración de Gradle..."
check_file "build.gradle.kts" || ((errors++))
check_file "settings.gradle.kts" || ((errors++))
check_file "gradle.properties" || ((errors++))
check_file "gradlew" || ((errors++))
check_file "gradlew.bat" || ((errors++))
check_file "gradle/wrapper/gradle-wrapper.properties" || ((errors++))

echo ""
echo "2. Verificando estructura del módulo app..."
check_file "app/build.gradle.kts" || ((errors++))
check_file "app/proguard-rules.pro" || ((errors++))
check_file "app/src/main/AndroidManifest.xml" || ((errors++))

echo ""
echo "3. Verificando archivos Kotlin..."
check_file "app/src/main/java/com/cronometro/minutos/MainActivity.kt" || ((errors++))
check_file "app/src/main/java/com/cronometro/minutos/CronometroService.kt" || ((errors++))

echo ""
echo "4. Verificando recursos..."
check_file "app/src/main/res/layout/activity_main.xml" || ((errors++))
check_file "app/src/main/res/values/strings.xml" || ((errors++))
check_file "app/src/main/res/values/colors.xml" || ((errors++))
check_file "app/src/main/res/values/themes.xml" || ((errors++))

echo ""
echo "5. Verificando iconos..."
check_dir "app/src/main/res/mipmap-mdpi" || ((errors++))
check_dir "app/src/main/res/mipmap-hdpi" || ((errors++))
check_dir "app/src/main/res/mipmap-xhdpi" || ((errors++))
check_dir "app/src/main/res/mipmap-xxhdpi" || ((errors++))
check_dir "app/src/main/res/mipmap-xxxhdpi" || ((errors++))

echo ""
echo "6. Verificando GitHub Actions..."
check_file ".github/workflows/android-build.yml" || ((errors++))

echo ""
echo "7. Verificando documentación..."
check_file "README.md" || ((errors++))
check_file "INICIO-RAPIDO.md" || ((errors++))
check_file ".gitignore" || ((errors++))

echo ""
echo "=================================="
if [ $errors -eq 0 ]; then
    echo -e "${GREEN}✓ TODOS LOS ARCHIVOS ESTÁN EN SU LUGAR${NC}"
    echo ""
    echo "El proyecto está listo para subir a GitHub."
    echo ""
    echo "Advertencia sobre gradle-wrapper.jar:"
    if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
        echo -e "${YELLOW}⚠ gradle-wrapper.jar no está presente${NC}"
        echo "  Esto es normal. GitHub Actions lo descargará automáticamente."
        echo "  Si prefieres incluirlo ahora, ejecuta:"
        echo "  curl -L -o gradle/wrapper/gradle-wrapper.jar \\"
        echo "    https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar"
    else
        echo -e "${GREEN}✓ gradle-wrapper.jar presente${NC}"
    fi
    echo ""
    echo "Siguiente paso: Lee INICIO-RAPIDO.md para instrucciones de GitHub"
else
    echo -e "${RED}✗ FALTAN $errors ARCHIVO(S)${NC}"
    echo "Revisa los archivos marcados arriba antes de continuar."
fi
echo "=================================="
