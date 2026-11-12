@echo off
setlocal
cd /d "%~dp0"
if not exist "target\valiantus-sistema-1.0.0.jar" (
 echo ERRO: JAR nao encontrado. Rode Maven package primeiro.
 pause
 exit /b 1
)
where jpackage >nul 2>nul
if errorlevel 1 (
 echo ERRO: jpackage nao encontrado. Instale JDK 21.
 pause
 exit /b 1
)
if not exist "distribuicao" mkdir "distribuicao"
if exist "distribuicao\Valiantus" rmdir /s /q "distribuicao\Valiantus"
jpackage --type app-image --name Valiantus --app-version 1.0.0 --input target --main-jar valiantus-sistema-1.0.0.jar --main-class br.com.valiantus.Main --dest distribuicao --java-options "-Dfile.encoding=UTF-8"
if errorlevel 1 (
 echo ERRO: nao foi possivel empacotar. Confira as mensagens acima.
 pause
 exit /b 1
)
echo.
echo PRONTO! Abra distribuicao\Valiantus\Valiantus.exe
 echo Para apresentar em outro PC, copie a pasta Valiantus inteira.
pause
