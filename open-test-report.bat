@echo off
REM Open both Extent and Allure reports from the report/results folder
echo ========================================
echo   Opening TNC Store Test Reports
echo ========================================

set "EXTENT_REPORT=%~dp0report\results\Extent\ExtentReport.html"
set "ALLURE_REPORT=%~dp0report\results\Allure\index.html"

echo [INFO] Checking for reports...

if exist "%EXTENT_REPORT%" (
    echo [SUCCESS] Found Extent Report
    start "" "%EXTENT_REPORT%"
    timeout /t 2 >nul
) else (
    echo [WARNING] Extent Report not found at %EXTENT_REPORT%
    echo Please run tests first: mvn clean test
)

if exist "%ALLURE_REPORT%" (
    echo [SUCCESS] Found Allure Report
    start "" "%ALLURE_REPORT%"
) else (
    echo [WARNING] Allure Report not found at %ALLURE_REPORT%
    echo Please run: mvn allure:report
)

echo.
echo [INFO] Reports opened in your default browser!
pause
