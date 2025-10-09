package utils;

import org.testng.annotations.DataProvider;

public class TestDataProviders {

    private static final ExcelDataReader excelReader = new ExcelDataReader();

    @DataProvider(name = "loginCredentials")
    public Object[][] provideLoginData() {
        return excelReader.getSheetData("LoginData");
    }

    @DataProvider(name = "validLoginData")
    public Object[][] provideValidLoginData() {
        Object[][] allData = excelReader.getSheetData("LoginData");
        if (allData.length > 0) {
            return new Object[][] { allData[0] };
        }
        return new Object[0][0];
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] provideInvalidLoginData() {
        Object[][] allData = excelReader.getSheetData("LoginData");
        if (allData.length > 1) {
            Object[][] invalidData = new Object[allData.length - 1][];
            System.arraycopy(allData, 1, invalidData, 0, allData.length - 1);
            return invalidData;
        }
        return new Object[0][0];
    }

    @DataProvider(name = "registrationData")
    public Object[][] provideRegistrationData() {
        return excelReader.getSheetData("RegisterData");
    }

    @DataProvider(name = "searchData")
    public Object[][] provideSearchData() {
        return excelReader.getSheetData("SearchData");
    }

    public static String generateUniqueEmail() {
        long timestamp = System.currentTimeMillis();
        return "user" + timestamp + "@tnctest.com";
    }

    public static String generateUniqueEmail(String prefix) {
        long timestamp = System.currentTimeMillis();
        return prefix.toLowerCase() + timestamp + "@tnctest.com";
    }
}
