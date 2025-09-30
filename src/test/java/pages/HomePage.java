package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class HomePage extends BasePage {
    @FindBy(css = "div.product-box a.product-title")
    private List<WebElement> productLinks;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickFirstProduct() {
        waitForPageLoad();
        if (!productLinks.isEmpty()) {
            productLinks.get(0).click();
        } else {
            throw new RuntimeException("No products found on homepage");
        }
    }
}

