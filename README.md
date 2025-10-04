# 🏪 TNC Store Automation Testing Framework

## Project Overview

Automation testing framework for TNC Store website using Selenium WebDriver, TestNG, and ExtentReports.
Enterprise-grade framework for scalable, maintainable, and collaborative test automation.

## Project Structure

```
TNC_S/
├── pom.xml
├── README.md
├── AUTOMATION_TESTING_BEST_PRACTICES.md
├── *.bat (test run scripts)
├── report/
│   └── screenshots/
├── test-output/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── commons/
│   │       │   ├── DriverFactory.java
│   │       │   └── Driver_Factory.java
│   │       ├── config/
│   │       │   └── TNCStoreConfig.java
│   │       ├── helpers/
│   │       │   └── PageHelpers.java
│   │       ├── listener/
│   │       │   ├── TestListener.java
│   │       │   └── listener.java
│   │       ├── locators/
│   │       ├── pages/
│   │       │   └── BasePage.java
│   │       └── reports/
│   │           └── report.java
│   └── test/
│       ├── java/
│       │   ├── config/
│       │   ├── helpers/
│       │   │   ├── AuthenticationMethodVerificationHelper.java
│       │   │   ├── BaseTest.java
│       │   │   ├── ConfigReader.java
│       │   │   ├── ExcelReader.java
│       │   │   ├── ExtentManager.java
│       │   │   ├── NetworkResponseHelper.java
│       │   │   ├── PopupHandler.java
│       │   │   ├── ReportManager.java
│       │   │   ├── ScreenshotUtils.java
│       │   │   ├── TestDataProvider.java
│       │   │   └── WaitUtils.java
│       │   ├── model/
│       │   │   ├── AuthenticationTestData.java
│       │   │   ├── CartTestData.java
│       │   │   ├── ProductDetailTestData.java
│       │   │   ├── SearchTestData.java
│       │   │   └── Model.java
│       │   ├── pages/
│       │   │   ├── AuthenticationPage.java
│       │   │   ├── HomePage.java
│       │   │   └── ...
│       │   └── test/
│       │       ├── authentication/
│       │       ├── userprofile/
│       │       ├── cart/
│       │       ├── search/
│       │       ├── productdetail/
│       │       └── checkout/
│       └── resources/
│           ├── config.properties
│           ├── extent-config.css
│           ├── extent-config.xml
│           ├── testdata.json
│           ├── data/
│           │   ├── ... (test data files)
│           ├── qa/
│           └── testdata/
│               └── ...
└── target/
```

## Key Folders & Files

- **commons/**: Core driver and listener utilities
- **config/**: Project configuration (URLs, timeouts, etc.)
- **helpers/**: Test utilities, base classes, reporting, waits, data providers
- **listener/**: TestNG listeners for reporting and retry
- **locators/**: (if used) Centralized element locators
- **pages/**: Page Object Model classes (one class per page)
- **model/**: Test data models (POJOs)
- **test/**: Test classes, organized by feature/module
- **resources/**: Config, test data, and reporting templates
- **report/**: Test reports and screenshots
- **test-output/**: TestNG output
- **target/**: Build output

## Coding Conventions

### Naming

- **Packages**: lowercase, meaningful (e.g., commons, helpers, pages)
- **Classes**: PascalCase (e.g., LoginPage, CartTest)
- **Methods**: camelCase, descriptive (e.g., clickLoginButton, enterEmail)
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