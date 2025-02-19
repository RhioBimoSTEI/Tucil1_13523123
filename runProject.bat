@echo off
REM Compile semua file Java dari folder src ke folder bin.

javac -d bin -sourcepath src src\com\iqpuzzlersolver\Main.java

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b %ERRORLEVEL%
)

REM Jalankan program
java -cp bin com.iqpuzzlersolver.Main

pause
