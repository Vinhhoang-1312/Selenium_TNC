@echo off
echo ========================================
echo   TNC Store Test Runner - Group Based
echo ========================================
echo.

set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"

echo Available Test Groups:
echo.
echo [1] 🔥 Smoke Tests          - Critical functionality only
echo [2] 🧪 Authentication Tests - All auth related tests
echo [3] 👤 User Profile Tests   - Profile management tests
echo [4] 📝 Signup Tests         - Registration specific tests
echo [5] 🔐 Login Tests          - Login specific tests
echo [6] 🔒 Password Tests       - Password related tests
echo [7] 🔄 Regression Tests     - Comprehensive testing
echo [8] 🚀 All Tests            - Complete test suite
echo [9] 🎯 Custom Group         - Enter custom group name
echo [0] ❌ Exit
echo.

set /p choice="Choose an option (0-9): "

if "%choice%"=="1" (
    echo Running Smoke Tests...
    call mvn clean test -Psmoke
    goto :open_reports
)

if "%choice%"=="2" (
    echo Running Authentication Tests...
    call mvn clean test -Pauth
    goto :open_reports
)

if "%choice%"=="3" (
    echo Running User Profile Tests...
    call mvn clean test -Pprofile
    goto :open_reports
)

if "%choice%"=="4" (
    echo Running Signup Tests...
    call mvn clean test -Psignup
    goto :open_reports
)

if "%choice%"=="5" (
    echo Running Login Tests...
    call mvn clean test -Plogin
    goto :open_reports
)

if "%choice%"=="6" (
    echo Running Password Tests...
    call mvn clean test -Ppassword
    goto :open_reports
)

if "%choice%"=="7" (
    echo Running Regression Tests...
    call mvn clean test -Pregression
    goto :open_reports
)

if "%choice%"=="8" (
    echo Running All Tests...
    call mvn clean test -Pall
    goto :open_reports
)

if "%choice%"=="9" (
    set /p custom_group="Enter group name (e.g., smoke,login): "
    echo Running Custom Group: %custom_group%
    call mvn clean test -Dgroups=%custom_group% -f testng-groups.xml
    goto :open_reports
)

if "%choice%"=="0" (
    echo Goodbye!
    goto :end
)

echo Invalid choice. Please try again.
pause
goto :start

:open_reports
echo.
echo ========================================
echo   Opening Test Reports
echo ========================================
echo.

if exist "target\tnc-store_report.html" (
    echo Opening HTML Report...
    start "" "target\tnc-store_report.html"
    timeout /t 2 >nul
)

if exist "target\surefire-reports\emailable-report.html" (
    echo Opening TestNG Report...
    start "" "target\surefire-reports\emailable-report.html"
)

echo.
echo Available Reports:
echo 📊 HTML Report: target\tnc-store_report.html
echo 📋 TestNG Report: target\surefire-reports\emailable-report.html
echo 📈 Excel Report: target\tnc-store_TestResults_*.xlsx
echo 📷 Screenshots: target\screenshots\
echo.

:end
pause
