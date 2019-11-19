import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.BasePO;
import utils.DriverManager;

import static org.testng.Assert.*;

public class FormsTests extends BaseTest {

    private String mandatory_question = "Это обязательный вопрос.";
    private String incorrect_email = "Укажите действительный адрес эл. почты";

    @BeforeMethod
    public void openFirstPage(){
        testForm.open();
        acceptAlert();
    }

    @Test
    public void enterNothing(){
        testForm.submit();
        assertTrue(testForm.getBlock(1).getErrorText().contains(mandatory_question));
        assertTrue(testForm.getBlock(2).getErrorText().contains(mandatory_question));
        assertTrue(testForm.getBlock(3).getErrorText().contains(mandatory_question));
        assertTrue(testForm.getBlock(4).getErrorText().contains(mandatory_question));

    }

    @Test
    public void enterEverythingCorrectly(){
        testForm.getBlock(1).setText("test@gmail.com");
        testForm.getBlock(2).setText("23.12.1992");
        testForm.getBlock(3).setText("Юрій");
        testForm.getBlock(4).setMood(5).setText("Комент");
        testForm.submit();
        assertTrue(surveyCompleted.isOpen());
    }


    @Test
    public void checkTitleBlocks(){
        assertEquals(testForm.getBlock(1).getInputTitle(),"Адрес электронной почты *");
        assertEquals(testForm.getBlock(2).getInputTitle(),"Your age: *");
        assertEquals(testForm.getBlock(3).getInputTitle(),"Your name: *");
        assertEquals(testForm.getBlock(4).getInputTitle(),"How is your mood? *");
    }

    @Test
    public void enterAndClearTestData(){
        testForm.getBlock(1).setText("voronin@gmail.com");
        testForm.getBlock(1).clearBlock();
        assertTrue(testForm.getBlock(1).getErrorText().contains(mandatory_question));

        testForm.getBlock(2).setText("23.06.1919");
        testForm.getBlock(2).clearBlock();
        assertTrue(testForm.getBlock(2).getErrorText().contains(mandatory_question),"Якщо стерти Ваш вік, то не пишеться що це обов'язкове питання!");

        testForm.getBlock(3).setText("Петро");
        testForm.getBlock(3).clearBlock();
        assertTrue(testForm.getBlock(3).getErrorText().contains(mandatory_question));

        testForm.getBlock(4).setMood(4);
        testForm.getBlock(4).clearBlock();
        assertTrue(testForm.getBlock(4).getErrorText().contains(mandatory_question));
    }

    @DataProvider(name = "invalid emails")
    public Object[][] invalidEmailProvider() {
        return new Object[][]{
                //According with RFC 2822
                {"plainaddress"},
                {"#@%^%#$@#$@#.com"},
                {"@domain.com"},
                {"Joe Smith <email@domain.com>"},
                {"email@domain@domain.com"},
                {".email@domain.com"},
                {"email@domain.com (Joe Smith"},
                {"email@111.222.333.44444"},
                {"email@domain..com"},
                //According with www.w3resource.com source
                {"mysite.ourearth.com"},//@ is not present
                {"mysite@.com.my"},// tld (Top Level domain) can not start with dot "."
                {"@you.me.net"},//No character before @
                {"mysite123@gmail.b"},// ".b" is not a valid tld
                {"mysite@.org.org"},//tld can not start with dot "."
                {".mysite@mysite.org"},//an email should not be start with "."
                {"mysite()*@gmail.com"},// here the regular expression only allows character, digit, underscore, and dash
                {"mysite..1234@yahoo.com"},//double dots are not allowed
        };
    }

    @Test(dataProvider = "invalid emails")
    public void enterInvalidEmail(String email) {
        System.out.println(email);
        testForm.getBlock(1).setText(email);
        testForm.submit();
        assertTrue(testForm.getBlock(1).getErrorText().contains(incorrect_email),"валідатор емейла пропустив невалідну пошту");
        acceptAlert();
    }

    @DataProvider(name = "valid email")
    public Object[][] validEmailProvider() {
        return new Object[][]{
                //According with www.w3resource.com source
                {"mysite@ourearth.com"},
                {"my.ownsite@ourearth.org"},
                {"mysite@you.me.net"},
                //According with RFC 2822
                {"email@domain.com"},
                {"firstname.lastname@domain.com"},
                {"email@subdomain.domain.com"},
                {"firstname+lastname@domain.com"},
                {"1234567890@domain.com"},
                {"email@domain-one.com"},
                {"email@domain.name"},
                {"email@domain.co.jp"},
                {"firstname-lastname@domain.com"},
        };
    }

    @Test(dataProvider = "valid email")
    public void enterValidEmail(String email) {
        System.out.println(email);
        testForm.getBlock(1).setText(email);
        testForm.submit();
        assertEquals(testForm.getBlock(1).getErrorText(), "");
        acceptAlert();
    }

    @Test
    public void checkEmptyInputs(){
        assertEquals(testForm.getBlock(1).getInputText(),"");
        assertEquals(testForm.getBlock(2).getInputText(),"");
        assertEquals(testForm.getBlock(3).getInputText(),"");
        assertEquals(testForm.getBlock(4).getInputText(),"");
    }

    @Test
    public void cannotEnterNegativeAge(){
        testForm.getBlock(2).setText("23.11.8000");
        testForm.submit();
        assertNotEquals(testForm.getBlock(2).getErrorText(),(""),"Форма дає ввести дату народження яка більша теперішнього числа");
    }

    @Test
    public void checkOther(){
        testForm.getBlock(4).setMood(5);
        testForm.submit();
        assertTrue(testForm.getBlock(4).getErrorText().contains(mandatory_question));
        testForm.getBlock(4).setText(" ");
        assertTrue(testForm.getBlock(4).getErrorText().contains(mandatory_question));
        testForm.getBlock(4).setText("TEST");
        assertEquals(testForm.getBlock(4).getErrorText(),"");
    }

    @Test
    public void testThatCantChooseTwoMoods(){
        testForm.getBlock(1).setText("test@gmail.com");
        testForm.getBlock(2).setText("01.01.2000");
        testForm.getBlock(3).setText("Юрій");
        testForm.getBlock(4).setMood(1);
        testForm.getBlock(4).setMood(2);
        testForm.submit();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(testForm.isOpen(),"Не можна вибирати 2 настрої!");
    }

    @Test
    public void testButtonOperationSubmitAnotherReply() {
        testForm.getBlock(1).setText("test@gmail.com");
        testForm.getBlock(2).setText("23.12.1992");
        testForm.getBlock(3).setText("Юрій");
        testForm.getBlock(4).setMood(5).setText("Комент");
        testForm.submit();
        surveyCompleted.clickAskAgain();
        assertTrue(testForm.isOpen());
    }

    @Test
    public void checkAlertNotificationIfRefreshPage(){
        testForm.getBlock(1).setText("test@gmail.com");
        testForm.getBlock(2).setText("23.03.1963");
        testForm.getBlock(3).setText("Анатолій");
        testForm.getBlock(4).setMood(1);
        refreshPage();
        assertTrue(alertIsVisible());
        acceptAlert();
    }

    @Test
    public void checkAlertNotificationIfClosePage() {
        testForm.getBlock(1).setText("test@gmail.com");
        testForm.getBlock(2).setText("23.03.1963");
        testForm.getBlock(3).setText("Анатолій");
        testForm.getBlock(4).setMood(1);
        DriverManager.getDriver().navigate().to("https://www.google.com.ua/");
        assertTrue(alertIsVisible());
        acceptAlert();
    }





}
