import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Сценарий №1
 */
public class MyFirstTest {
    WebDriver driver;

    @Test
    public void test() {
        /**
         * 1) Перейти по ссылке http://www.rgs.ru
         */
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://rgs.ru");
        /**
         * 2) Выбрать пункт меню - Страхование
         */
        getClickDriverOnXpath("//*[@id='main-navbar-collapse']/ol[1]/li[2]");

        /**
         * 3)Выбрать категорию - ДМС //*[@id="rgs-main-menu-insurance-dropdown"]/div[1]/div[1]/div/div[1]/div[3]/ul/li[2]/a
         */
       getClickDriverOnXpath("//*[@id='rgs-main-menu-insurance-dropdown']//*[contains(text(), 'ДМС')]");
        /**
         * 4) Проверить наличие заголовка - Добровольное медицинское страхование
         */
        Assert.assertEquals(driver.getTitle(), "ДМС 2019 | Рассчитать стоимость добровольного медицинского страхования и оформить ДМС в Росгосстрах");

        /**
         * 5) Нажать на кнопку - Отправить заявку
         */
        getClickDriverOnXpath("//a[contains(text(),'Отправить заявку')]");

        WebDriverWait wait = new WebDriverWait(driver, 5, 200);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("modal-dialog")));

        /**
         * 6) Проверить, что открылась страница , на которой присутствует текст - Заявка на добровольное медицинское страхование
         */
        Assert.assertEquals(driver.findElement(By.xpath("//*[contains(text(),'Заявка на добровольное медицинское страхование')]")).getText(), "Заявка на добровольное медицинское страхование");

        /**
         *  7) Заполнить поля
         *  Имя
         *  Фамилия
         *  Отчество
         *  Регион
         *  Телефон
         *  Эл. почта - qwertyqwerty Комментарии
         *  Я согласен на обработку
         *
         *  8) Проверить, что все поля заполнены введенными значениями
         */
        fillInputByName("Фамилия", "Иванов");
        fillInputByName("Имя", "Иван");
        fillInputByName("Отчество", "Иванович");
        fillInputByName("Телефон", "(913) 145-65-89");
        fillInputByName("Эл. почта", "qwertyqwerty");


        WebElement commentE =  driver.findElement(By.xpath("//*[@id='applicationForm']/div[2]/div[8]/textarea"));
        String selenium_test = "Selenium test";
        commentE.sendKeys(selenium_test);
        Assert.assertEquals(commentE.getAttribute("value"), selenium_test);

        WebElement selectRegion = driver.findElement(By.xpath("//select[@name='Region']"));
        Select select = new Select(selectRegion);
        String valueRegion = "77";
        select.selectByValue(valueRegion);
        Assert.assertEquals(selectRegion.getAttribute("value"), valueRegion);

        getClickDriverOnXpath("//*[contains(text(),'Я согласен')]/preceding::input[@class='checkbox']");
        /**
         *9) Нажать Отправить
         */
        getClickDriverOnXpath("//button[contains(text(),'Отправить')]");

        /**
         *10) Проверить, что у Поля - Эл. почта присутствует сообщение об ошибке - Введите адрес электронной почты
         */
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Эл. почта')]/following::span[contains(@class, 'validation')]")));
        checkError("Эл. почта", "Введите адрес электронной почты");
        driver.close();
        driver.quit();
    }

    private void getClickDriverOnXpath(String s) {
        driver.findElement(By.xpath(s)).click();
    }

    public void fillInputByName(String name, String textTo) {
        String temp = "//*[text() = '%s']/following::input[1]";
        String fullXpath = String.format((temp), name);
        WebElement element = driver.findElement(By.xpath(fullXpath));
        element.sendKeys(textTo);
        new WebDriverWait(driver, 10).until(ExpectedConditions.textToBePresentInElementValue(element, textTo));
        String actualText = element.getAttribute("value");
        /**
         * 8) Проверить, что все поля заполнены введенными значениями
         */
        if (actualText.contains("+7")) {
            Assert.assertEquals(actualText, "+7 " + textTo);
        } else Assert.assertEquals(actualText, textTo);

    }

    public void checkError(String name, String textTo) {
        String temp = "//*[text() = '%s']/following::span[contains(@class, 'validation')]";
        String fullXpath = String.format((temp), name);
        String actualText = driver.findElement(By.xpath(fullXpath)).getText();
        Assert.assertEquals(textTo, actualText);
    }
}
