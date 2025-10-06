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
if exist "target\allure-results-raw" rmdir /s /q "target\allure-results-raw"
if exist "target\site\allure-maven-plugin" rmdir /s /q "target\site\allure-maven-plugin"
if exist "report\results\Allure" rmdir /s /q "report\results\Allure"

echo [INFO] Creating report directories...
if not exist "report\results\Extent" mkdir "report\results\Extent"
if not exist "target\allure-results" mkdir "target\allure-results"
if not exist "report\results\Allure" mkdir "report\results\Allure"
echo [INFO] Running tests...
call mvn clean test -DskipTests=false

if %ERRORLEVEL% neq 0 (
    echo [WARNING] Some tests failed, but reports will still be generated.
)

echo.
echo [INFO] Generating Allure report (site)...
call mvn allure:report

echo [INFO] Copying generated HTML pages (Allure) into target\allure-results...
#if target site exists, copy only html files to target/allure-results
if exist "target\site\allure-maven-plugin" (
    xcopy /Y "target\site\allure-maven-plugin\*.html" "target\allure-results\" >nul
    echo [SUCCESS] HTML pages copied to target\allure-results\
) else (
    echo [WARNING] Allure generated site not found in target\site\allure-maven-plugin
)

echo [INFO] Also copying HTML pages into report\results\Allure for quick viewing...
if exist "target\site\allure-maven-plugin" (
    rem Copy the full site (html + assets) so index.html renders correctly in report\results\Allure
    xcopy /E /Y "target\site\allure-maven-plugin\*" "report\results\Allure\" >nul
    echo [SUCCESS] Full Allure site copied to report\results\Allure\
) else (
    echo [WARNING] Allure generated site not found; nothing to copy to report\results\Allure
)

echo.
echo [INFO] Opening reports in browser...

if exist "target\allure-results\ExtentReport.html" (
    echo [INFO] Opening Extent Report (if present)...
    start "" "target\allure-results\ExtentReport.html"
    timeout /t 2 >nul
) else (
    echo [INFO] Extent Report file not present in target\allure-results (it may have a timestamped name). Listing files:
    dir /b target\allure-results\*.html
)

if exist "report\results\Allure\index.html" (
    echo [INFO] Opening Allure Report index from report\results\Allure...
    start "" "report\results\Allure\index.html"
) else if exist "target\allure-results\index.html" (
    echo [INFO] Opening Allure Report index from target\allure-results...
    start "" "target\allure-results\index.html"
) else (
    echo [WARNING] Allure index.html not found in expected locations
)

echo.
echo ========================================
echo   Report Generation Complete!
echo ========================================
echo.
echo Available Reports:
echo 1. Extent Report: target\allure-results\ExtentReport.html
echo 2. Allure Report: report\results\Allure\index.html (or target\allure-results\index.html)
echo 3. TestNG Report: target\surefire-reports\emailable-report.html
echo.
pause
