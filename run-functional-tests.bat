@echo off
echo ========================================
echo   TNC Store Functional Test Runner
echo ========================================
echo.

set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"

echo Available Test Functions:
echo.
echo [1] 🔥 Smoke Tests           - Critical tests only (Login, Register, Profile View)
echo [2] 🔐 Login Tests           - All login functionality tests
echo [3] 📝 Register Tests        - All registration functionality tests
echo [4] 🔒 Forgot Password Tests - Password recovery tests
echo [5] 👁️ Profile View Tests    - Profile viewing functionality
echo [6] ✏️ Profile Update Tests  - Profile modification tests
echo [7] 🔑 Password Change Tests - Password change functionality
echo [8] 🧪 All Authentication    - All auth-related tests (Login, Register, Forgot)
echo [9] 👤 All User Profile      - All profile-related tests (View, Update, Password)
echo [10] 🚀 All Functional Tests - Complete functional test suite
echo [0] ❌ Exit
echo.

set /p choice="Choose an option (0-10): "

if "%choice%"=="1" (
    echo Running Smoke Tests...
    call mvn clean test -Psmoke-functional
    goto :open_reports
)

if "%choice%"=="2" (
    echo Running Login Tests...
    call mvn clean test -Plogin
    goto :open_reports
)

if "%choice%"=="3" (
    echo Running Register Tests...
    call mvn clean test -Pregister
    goto :open_reports
)

if "%choice%"=="4" (
    echo Running Forgot Password Tests...
    call mvn clean test -Pforgot-password
    goto :open_reports
)

if "%choice%"=="5" (
    echo Running Profile View Tests...
    call mvn clean test -Pprofile-view
    goto :open_reports
)

if "%choice%"=="6" (
    echo Running Profile Update Tests...
    call mvn clean test -Pprofile-update
    goto :open_reports
)

if "%choice%"=="7" (
    echo Running Password Change Tests...
    call mvn clean test -Ppassword-change
    goto :open_reports
)

if "%choice%"=="8" (
    echo Running All Authentication Tests...
    call mvn clean test -Pauth
    goto :open_reports
)

if "%choice%"=="9" (
    echo Running All User Profile Tests...
    call mvn clean test -Pprofile
    goto :open_reports
)

if "%choice%"=="10" (
    echo Running All Functional Tests...
    call mvn clean test -Pfunctions
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

echo Checking for generated reports...

if exist "target\authentication-login_report.html" (
    echo Opening Login Test Report...
    start "" "target\authentication-login_report.html"
    timeout /t 2 >nul
)

if exist "target\authentication-register_report.html" (
    echo Opening Register Test Report...
    start "" "target\authentication-register_report.html"
    timeout /t 2 >nul
)

if exist "target\userprofile-view_report.html" (
    echo Opening Profile View Test Report...
    start "" "target\userprofile-view_report.html"
    timeout /t 2 >nul
)

if exist "target\tnc-store_report.html" (
    echo Opening General Test Report...
    start "" "target\tnc-store_report.html"
    timeout /t 2 >nul
)

if exist "target\surefire-reports\emailable-report.html" (
    echo Opening TestNG Summary Report...
    start "" "target\surefire-reports\emailable-report.html"
)

echo.
echo Available Reports:
echo 📊 Function-specific HTML Reports: target\*_report.html
echo 📋 TestNG Summary Report: target\surefire-reports\emailable-report.html
echo 📈 Excel Reports: target\*_TestResults_*.xlsx
echo 📷 Screenshots: target\screenshots\
echo.

:end
pause
