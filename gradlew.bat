@rem Gradle startup script for Windows
@if "%DEBUG%"=="" @echo off
@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal
set DIRNAME=%~dp0
set GRADLE_WRAPPER_JAR="%DIRNAME%gradle\wrapper\gradle-wrapper.jar"
"%JAVA_HOME%\bin\java.exe" -jar %GRADLE_WRAPPER_JAR% %*
