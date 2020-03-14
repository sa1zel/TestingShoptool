import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Test
public class MainClass {
    private static WebDriver driver;
    private static Actions actions;
    private static WebDriverWait wait;


    @BeforeTest
    public void before() {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\user\\IdeaProjects\\TestingShoptool\\drivers\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\IdeaProjects\\TestingShoptool\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS)
                .setScriptTimeout(15, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    @Test
    public void firstTest() {
        int numberOfItems = 3;
        driver.get("https://shoptool.com.ua/");
        WebElement element = driver.findElement(By.partialLinkText("Электроинструмент"));
        actions.moveToElement(element).build().perform();
        driver.findElement(By.partialLinkText("Перфораторы")).click();
        String elementsXPath = "//div[@id='categories_view_pagination_contents']//div[contains(@class,\"ty-product-labels__item--discount\")]/ancestor::form";
        for (int i = 0; i < 3; i++) {
            ExpectedCondition<WebElement> condition = ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//span[@class='ty-pagination__selected'][text()='%s']", i + 1)));
            for (int j = 0; j < numberOfItems; j++) {
                wait.until(condition);
                List<WebElement> discountElements = driver.findElements(By.xpath(elementsXPath));
                WebElement nextElement = RandomElement.randElementNoRepeat(discountElements);
                if (nextElement == null) break;
                wait.until(ExpectedConditions.elementToBeClickable(nextElement));
                nextElement.click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-left']")));
                List<WebElement> discount = driver.findElements(By.xpath("//div[@class='col-left']//span[contains(@id,\"price_update\")]//span[@class='ty-strike']"));
                Assert.assertEquals(discount.size(), 1, "No element there");
                driver.navigate().back();
            }

            WebElement nextPageButton = driver.findElement(By.className("ty-pagination__right-arrow"));
            wait.until(ExpectedConditions.elementToBeClickable(nextPageButton));
            nextPageButton.click();
        }
    }

    @Test
    public void secondTest() {
        driver.get("https://shoptool.com.ua/");
        WebElement element = driver.findElement(By.partialLinkText("Электроинструмент"));
        actions.moveToElement(element).build().perform();
        driver.findElement(By.linkText("Дрели")).click();
        for (int i = 0; i < 3; i++) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//span[@class='ty-pagination__selected'][text()='%s']", i + 1))));
            List<WebElement> items = driver.findElements(By.xpath("//div[@id='categories_view_pagination_contents']//*[@class='ut2-gl__item ']/form"));

            WebElement randElement = RandomElement.randomElement(items);
            System.out.println(randElement.getAttribute("name"));

            WebElement addToCart = driver.findElement(By.xpath(String.format("//form[@name='%s']//span[text()='Купить']/ancestor::button", randElement.getAttribute("name"))));
            System.out.println(addToCart.getAttribute("id"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", addToCart);
            actions.moveToElement(randElement).build().perform();
            wait.until(ExpectedConditions.elementToBeClickable(addToCart));
            addToCart.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cm-notification-content")));
            driver.findElement(By.className("cm-notification-close")).click();

            WebElement nextPageButton = driver.findElement(By.className("ty-pagination__right-arrow"));
            wait.until(ExpectedConditions.visibilityOf(nextPageButton));
            nextPageButton.click();
        }
        driver.findElement(By.xpath("//span[text()='Корзина']/parent::a")).click();
        driver.findElement(By.xpath("//div[@class='ty-float-left']/a")).click();

        List<WebElement> cartItems = driver.findElements(By.xpath("//table[@class=\"ty-cart-content ty-table\"]/tbody/tr"));
        Assert.assertEquals(cartItems.size(), 3);

        String total = driver.findElement(By.id("sec_cart_total")).getText();
        System.out.println(total);

        List<WebElement> delete = driver.findElements(By.xpath("//*[@title='Удалить']"));
        RandomElement.randomElement(delete).click();

        String newTotal = driver.findElement(By.id("sec_cart_total")).getText();
        Assert.assertNotEquals(total, newTotal);
    }

    //    Перейти в раздел "Электроинструменты" / "Шуруповерты"
//    Вывести "Наименование" всех товаров у которых есть тикет "Makita"
//    Проверить что тикет "Makita" только у товаров в наименовании которых есть слово "Makita" Делаем для 1, 3, 5-й страничек
//    Переходим в карточку одного из товаров "Makita" и проверяем Bread crumbs
    @Test
    public void thirdTest() {
        String ticket = "INTERTOOL";
        List<WebElement> webElements = null;
        driver.get("https://shoptool.com.ua/");
        WebElement element = driver.findElement(By.partialLinkText("Электроинструмент"));
        actions.moveToElement(element).build().perform();


        driver.findElement(By.partialLinkText("Шлифмашины")).click();
        for (int i = 0; i < 3; i++) {
            int page = i * 2 + 1;
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//span[@class='ty-pagination__selected'][text()='%s']", page))));
            webElements = driver.findElements(By.xpath(String.format("//div[@id='categories_view_pagination_contents']//img[@alt='%s']/ancestor::form//div[@class='ut2-gl__name']", ticket)));
            if (webElements.size() > 0) {
                for (WebElement el : webElements) {
                    String itemName = el.getText();
                    System.out.println(itemName);
                    Assert.assertTrue(itemName.contains(ticket));
                }
            }
//            WebElement pagPage = driver.findElement(By.xpath("//span[@class='ty-pagination__selected']"));
//            System.out.println(pagPage.getText());
            if (page != 5) {
                WebElement nextPage = driver.findElement(By.xpath(String.format("//div[@class='ty-pagination__items']/a[@data-ca-page='%s']", page + 2)));
                wait.until(ExpectedConditions.elementToBeClickable(nextPage));
                nextPage.click();
            }
        }
        for (int i = 0; i < 3; i++) {
            webElements = driver.findElements(By.xpath(String.format("//div[@id='categories_view_pagination_contents']//img[@alt='%s']/ancestor::form//div[@class='ut2-gl__name']", ticket)));
            if (webElements.size() == 0) {
                driver.navigate().back();
                if (i == 2) Assert.fail("No Such elements");
            } else break;
        }
        webElements.get(0).click();
        System.out.println(driver.findElement(By.xpath("//div[contains(@class,\"breadcrumbs\")]")).getText());
        List<WebElement> breadCrumbs = driver.findElements(By.xpath("//div[contains(@class,\"breadcrumbs\")]/a"));
        Assert.assertTrue(breadCrumbs.size() > 0);
        Assert.assertEquals(breadCrumbs.get(0).getText(), "Главная");
        Assert.assertEquals(breadCrumbs.get(1).getText(), "Каталог");
        Assert.assertEquals(breadCrumbs.get(2).getText(), "Электроинструмент");
        Assert.assertEquals(breadCrumbs.get(3).getText(), "Шлифмашины");
        Assert.assertTrue(breadCrumbs.get(5).getText().equalsIgnoreCase(ticket));

    }

    @Test
    public void fourthTest() {
        List<Item> items = new LinkedList<>();
        String discountXPath = "//div[@id='categories_view_pagination_contents']//div[text()='Aкция']/ancestor::form";
        driver.get("https://shoptool.com.ua/");
        WebElement element = driver.findElement(By.partialLinkText("Электроинструмент"));
        actions.moveToElement(element).build().perform();
        driver.findElement(By.partialLinkText("Шуруповерты")).click();
        int pages = Integer.parseInt(driver.findElement(By.xpath("//div[@class='ty-pagination__items']/a[last()]")).getAttribute("data-ca-page"));
        for (int i = 0; i < pages; i++) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//span[@class='ty-pagination__selected'][text()='%s']", i + 1))));
            List<WebElement> elementsName = driver.findElements(By.xpath(String.format("%s//div[@class='ut2-gl__name']", discountXPath)));
            List<WebElement> elementsDiscount = driver.findElements(By.xpath(String.format("%s//div[@class='ty-product-labels__content']/em", discountXPath)));
            List<WebElement> elementsOldPrice = driver.findElements(By.xpath(String.format("%s//span[@class='ty-strike']//span[contains(@id, \"sec\")]", discountXPath)));
            List<WebElement> elementsPrice = driver.findElements(By.xpath(String.format("%s//span[@class='ty-price-num'][1]", discountXPath)));
            for (int j = 0; j < elementsName.size(); j++) {
                String name = elementsName.get(j).getText();
                String oldPriceStr = elementsOldPrice.get(j).getText().replace(",", "");
                double oldPrice = Double.parseDouble(oldPriceStr);
                String priceStr = elementsPrice.get(j).getText().replace(",", "");
                double price = Double.parseDouble(priceStr);
                String discountStr = elementsDiscount.get(j).getText().replace("%", "");
                int discount = Integer.parseInt(discountStr);
                items.add(new Item(name, oldPrice, price, discount));
            }
            WebElement nextPageButton = driver.findElement(By.className("ty-pagination__right-arrow"));
            wait.until(ExpectedConditions.visibilityOf(nextPageButton));
            nextPageButton.click();
        }
        List<Item> randItems = RandomElement.randomElementsNoRepeat(items, 10);
        for (Item item : items) {
            double guessPrice = item.getOldPrice() * (100 - item.getDiscount())/100;
            double realPrice = item.getNewPrice();
            String name = item.getName();
            Assert.assertEquals(guessPrice, realPrice, name);
        }

    }

    @AfterTest
    public void after() {
        driver.close();
    }
}
