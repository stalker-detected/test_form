import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class FormsTests extends BaseTest {

    String mandatory_question = " Это обязательный вопрос.";
    String incorrect_email = "Укажите действительный адрес эл. почты";

    @BeforeClass
    public void openFirstPage(){
        testForm.open();
    }

    @Test
    public void enterNothing(){
        testForm.submit();
        assertEquals(testForm.getBlock(1).getErrorText(),mandatory_question);
        assertEquals(testForm.getBlock(2).getErrorText(),mandatory_question);
        assertEquals(testForm.getBlock(3).getErrorText(),mandatory_question);
        assertEquals(testForm.getBlock(4).getErrorText(),mandatory_question);

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
}
