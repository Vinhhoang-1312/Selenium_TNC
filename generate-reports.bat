@echo off
echo ========================================
echo   TNC Store Test Reports Generator
echo ========================================
echo.

set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"

echo [INFO] Cleaning previous test results...
if exist "target\surefire-reports" rmdir /s /q "target\surefire-reports"
if exist "target\allure-results" rmdir /s /q "target\allure-results"
if exist "target\allure-report" rmdir /s /q "target\allure-report"

echo [INFO] Running tests...
call mvn clean test -DskipTests=false

if %ERRORLEVEL% neq 0 (
    echo [WARNING] Some tests failed, but reports will still be generated.
)

echo.
echo [INFO] Generating reports...

echo [INFO] 1. ExtentReports is available at: target\ExtentReport.html
echo [INFO] 2. TestNG HTML Report is available at: target\surefire-reports\emailable-report.html

echo.
echo [INFO] Opening reports in browser...

if exist "target\ExtentReport.html" (
    echo [INFO] Opening ExtentReport...
    start "" "target\ExtentReport.html"
    timeout /t 2 >nul
)

if exist "target\surefire-reports\emailable-report.html" (
    echo [INFO] Opening TestNG Report...
    start "" "target\surefire-reports\emailable-report.html"
)

echo.
echo ========================================
echo   Report Generation Complete!
echo ========================================
echo.
echo Available Reports:
echo 1. ExtentReport (Rich HTML): target\ExtentReport.html
echo 2. TestNG Report (Summary): target\surefire-reports\emailable-report.html
echo 3. Detailed XML Reports: target\surefire-reports\TEST-*.xml
echo.
echo Screenshots are saved in: target\screenshots\
echo.
pause
