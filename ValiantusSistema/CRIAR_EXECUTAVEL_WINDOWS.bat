@echo off
setlocal
cd /d "%~dp0"
echo ===============================================
echo    VALIANTUS - GERADOR DE EXECUTAVEL WINDOWS
echo ===============================================
where java >nul 2>nul
if errorlevel 1 (
 echo ERRO: Java nao encontrado. Instale o JDK 21 e configure o PATH.
 pause
 exit /b 1
)
where jpackage >nul 2>nul
if errorlevel 1 (
 echo ERRO: jpackage nao encontrado. Use um JDK 21 completo.
 pause
 exit /b 1
)
where mvn >nul 2>nul
if errorlevel 1 (
 echo ERRO: Maven nao encontrado no PATH.
 echo Instale o Apache Maven ou execute Maven package pelo IntelliJ,
 echo e depois use o arquivo GERAR_APLICATIVO_APOS_MAVEN.bat.
 pause
 exit /b 1
)
call mvn -DskipTests clean package
if errorlevel 1 (
 echo ERRO: falha ao compilar com Maven.
 pause
 exit /b 1
)
call "%~dp0GERAR_APLICATIVO_APOS_MAVEN.bat"
