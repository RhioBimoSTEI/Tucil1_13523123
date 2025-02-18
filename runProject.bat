@echo off
REM Run to compile all Java source files in the src folder into the bin folder.
REM Adjust the paths if structure is different.

javac -d bin -sourcepath src src\com\iqpuzzlersolver\Main.java

REM Check if compilation was successful.
IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b %ERRORLEVEL%
)

REM Run the program.
java -cp bin com.iqpuzzlersolver.Main

pause