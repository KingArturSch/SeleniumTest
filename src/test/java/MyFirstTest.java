import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
        WebElement dropdownBtn = driver.findElement(By.xpath("//div[contains(@id,'main-navbar-collapse')]//li[contains(@class, 'dropdown')]//a[contains(text(),'Страхование')]"));
        dropdownBtn.click();

        /**
         * 3)Выбрать категорию - ДМС
         */
        WebElement dmsBtn = driver.findElement(By.xpath("//a[contains(text(),'ДМС') and not (contains(text(), 'Полис'))]"));
        dmsBtn.click();
        /**
         * 4) Проверить наличие заголовка - Добровольное медицинское страхование
         */
        Assert.assertEquals(driver.getTitle(), "ДМС 2019 | Рассчитать стоимость добровольного медицинского страхования и оформить ДМС в Росгосстрах");

        /**
         * 5) Нажать на кнопку - Отправить заявку
         */
        WebElement openIssuranceFrom = driver.findElement(By.xpath("//a[contains(text(),'Отправить заявку')]"));
        openIssuranceFrom.click();

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
        driver.findElement(By.xpath("//*[@id='applicationForm']/div[2]/div[8]/textarea")).sendKeys("Selenium test");

        WebElement selectRegion = driver.findElement(By.xpath("//select[@name='Region']"));
        Select select = new Select(selectRegion);
        // выводит список из выбора
//        select.getOptions().stream().map(WebElement::getText).collect(Collectors.toList());
//        Arrays.asList(selectRegion.getText().split("/n"));

        select.selectByIndex(7);
        driver.findElement(By.xpath("//*[contains(text(),'Я согласен')]/preceding::input[@class='checkbox']")).click();


        /**
         *9) Нажать Отправить
         */
        driver.findElement(By.xpath("//button[contains(text(),\"Отправить\")]")).click();

        /**
         *10) Проверить, что у Поля - Эл. почта присутствует сообщение об ошибке - Введите адрес электронной почты
         */
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Эл. почта')]/following::span[contains(@class, 'validation')]")));
        checkError("Эл. почта", "Введите адрес электронной почты");
        driver.close();
        driver.quit();
    }

    public void fillInputByName(String name, String textTo) {
        String temp = "//*[text() = '%s']/following::input[1]";
        String fullxpath = String.format((temp), name);
        WebElement element = driver.findElement(By.xpath(fullxpath));
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
        String fullxpath = String.format((temp), name);
        String actualText = driver.findElement(By.xpath(fullxpath)).getText();
        Assert.assertEquals(textTo, actualText);
    }
}
