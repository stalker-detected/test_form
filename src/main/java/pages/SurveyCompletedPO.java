package pages;

import org.openqa.selenium.By;

public class SurveyCompletedPO extends BasePO {

    private static final String REL_URL = "/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/formResponse";
    public SurveyCompletedPO() { super(REL_URL);}

    private final By ASK_AGAIN_SEL = By.cssSelector(".freebirdFormviewerViewResponseLinksContainer a");

    //клікути на "Отправить ещё один ответ"
    public void clickAskAgain(){
        clickElement(ASK_AGAIN_SEL);
    }
}
