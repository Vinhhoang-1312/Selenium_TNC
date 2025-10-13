# 🏪 TNC Store Automation Testing Framework

# 🏪 TNC Store Automation Testing Framework

## Overview

This project is an automated testing framework for the TNC Store website, built with Selenium WebDriver, TestNG, Allure, and SLF4J. It supports scalable, maintainable, and parallelizable UI test automation for e-commerce flows.

## Features

- Page Object Model (POM) for maintainable test code
- Parallel test execution with TestNG
- Allure reporting integration
- Data-driven testing (Excel, JSON)
- Utilities for screenshots, waits, and reporting
- Maven-based build and dependency management

## Project Structure

```
TNC_S/
├── pom.xml                # Maven build file
├── README.md              # Project documentation
├── requirements.txt       # Python dependencies (for data tools)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── utils/     # Core Java utilities (driver, config, reporting)
│   │   └── resources/     # Main resources (application.properties, testdata.xlsx)
│   └── test/
│       ├── java/
│       │   ├── core/      # Base test, driver factory
│       │   ├── helpers/   # Helper classes (e.g., PopupHandler)
│       │   ├── listeners/ # TestNG listeners
│       │   ├── pages/     # Page Object classes (LoginPage, CartPage, etc.)
│       │   ├── reports/   # Reporting utilities
│       │   ├── tests/     # Test classes (LoginTests, CartTests, etc.)
│       │   └── utils/     # Additional utilities (e.g., write_xlsx_data_resources.py)
│       └── resources/     # Test resources (allure.properties, testdata, etc.)
├── testng.xml             # TestNG suite configuration
├── *.xml                  # Other TestNG suite files
├── target/                # Build and test output
└── allure-results/        # Allure results (generated)
```

## Key Technologies

- **Java 21**
- **Selenium WebDriver 4**
- **TestNG 7**
- **Allure 2**
- **SLF4J** (logging)
- **Apache POI** (Excel data)
- **Maven** (build & dependency management)

## How to Run Tests

1. **Install Java 21+ and Maven**
2. `mvn clean test` to run all tests (default: Chrome browser)
3. Use `-DsuiteXmlFile=your-suite.xml` or Maven profiles to run specific suites (see `pom.xml`)
4. Generate Allure report:

- `allure serve target/allure-results` (Allure CLI required)

## Test Suite Organization

- Test classes are in `src/test/java/tests/` (e.g., `LoginTests.java`, `CartTests.java`)
- Page Objects are in `src/test/java/pages/`
- Utilities and helpers are in `core/`, `helpers/`, `utils/`, and `reports/`
- Test data is in `src/test/resources/testdata/` and Excel/JSON files

## Configuration

- **Browser**: Set via TestNG parameter or `config.properties`
- **Test data**: Excel (`testdata.xlsx`), JSON, or Java classes
- **Allure**: Results in `target/allure-results/`, config in `src/test/resources/`

## Coding Conventions

- **Packages**: lowercase, meaningful (e.g., core, helpers, pages)
- **Classes**: PascalCase (e.g., LoginPage, CartTests)
- **Methods/Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE
- **Test Classes**: End with `Tests` (e.g., LoginTests)
- **Test Methods**: methodName_condition_expectedResult
- **Indentation**: 4 spaces
- **No unnecessary comments** (especially Vietnamese)

## Best Practices

- Use assertions from TestNG in test classes
- No hardcoded test data in scripts
- Driver managed by DriverFactory (Singleton/ThreadLocal)
- Parallel execution via TestNG XML
- Clean, DRY, and reusable code
- No sensitive info in logs
- No errors (Alt+F6) before closing PR
- In PowerShell, use `;` instead of `&&` for command chaining

## Dependencies (Maven)

See `pom.xml` for all dependencies. Key ones:

- Selenium Java
- TestNG
- Allure TestNG
- SLF4J
- Apache POI (Excel)

## Python Utilities

Some data tools/scripts use Python (see `requirements.txt`). Install with:

```
pip install -r requirements.txt
```

## Contact

For questions, contact the TNC Store QA team.

- **Variables**: camelCase, concise (e.g., userName, expectedTitle)
- **Constants**: SNAKE_CASE, all uppercase (e.g., BASE_URL, DEFAULT_TIMEOUT)
- **Test Classes**: End with `Test` (e.g., LoginTest)
- **Test Methods**: methodName_condition_expectedResult (e.g., login_withValidCredentials_shouldSucceed)

### Page Object Model (POM)

- One class per page in `pages/`
- Shared locators in `BasePage.java`, page-specific locators in each page class
- Action methods are atomic (one action per method)
- No assertions in page classes; return data/state for assertions in test classes

### Test Design

- Tests are independent and can run standalone
- No shared state between tests
- Use `@BeforeMethod` and `@AfterMethod` for WebDriver setup/teardown
- SmokeTests only verify framework basics (browser launch, login)

### Test Data & Config

- `config.properties`: Base URL, browser, timeout
- `testdata.json`: Common test data
- `data/`, `testdata/`: Multiple data sets (JSON/CSV/Excel)
- No hardcoded test data in test scripts

### Logging & Reporting

- Use Log4j or SLF4J for logging
- Custom reports in `report/`
- Log format: `[TIMESTAMP] [LEVEL] [CLASS] - message`
- Do not log sensitive info (e.g., passwords)

### Code Style

- Indentation: 4 spaces (no tabs)
- Line length ≤ 120 characters
- Braces `{}` on the same line: `if (isLoggedIn) { doSomething(); }`
- Comments only when logic is not self-explanatory
- Avoid magic numbers; use constants

### Best Practices

- Use assertions from TestNG/JUnit in test classes
- Retry failed tests (listener in `listener/`)
- Driver managed by DriverFactory (Singleton/ThreadLocal)
- Parallel execution configured in TestNG XML
- Code must be clean, DRY, and reusable
- Don't have to write comment( especially Vietnamese) if the script easy to understand( prefer not to write any comment
  in code)
- Make sure there is no error(problems alt+f6) in the whole project before closing conversation
- Do not use && interminal , use ; instead
