package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static pages.BasePO.clickElement;
import static pages.BasePO.getElementText;
import static utils.DriverManager.getDriver;

public class Blocks {

    private int index;
    private String BLOCKS_SEL;


    public Blocks(int index) {
        this.index = index;
        BLOCKS_SEL = ".freebirdFormviewerViewItemList > div:nth-of-type("+index+") ";
    }



    private final String TEXT_INPUT_SEL = "[jsname=\"YPqjbf\"]";
    private final String INPUT_TITLE_SEL = ".freebirdFormviewerViewItemsItemItemTitle.exportItemTitle.freebirdCustomFont";
    private final String ERROR_TEXT_SEL = ("[jsname=\"XbIQze\"]");
    private final String CHECKBOX_ITEMS_SEL ="[jscontroller=\"EcW08c\"]";
    private final String CHECKBOX_INPUT_SEL ="[jsname=\"ekGZBc\"]";

    public void setText(String text){
        getDriver().findElement(By.cssSelector(BLOCKS_SEL+TEXT_INPUT_SEL)).sendKeys(text);
    }

    public Blocks setMood(int indx){
        TestFormPO.index_mood  = indx;
        List<WebElement> moodList= getDriver().findElements(By.cssSelector(BLOCKS_SEL+CHECKBOX_ITEMS_SEL));
        clickElement(moodList.get(indx-1));
        return new Blocks(index);
    }

    //очистити введені дані
    public void clearBlock(){
        switch (index){
            case 1:
            case 3:
            case 2:
                getDriver().findElement(By.cssSelector(BLOCKS_SEL+TEXT_INPUT_SEL)).clear();
            break;
            case 4: {
                List<WebElement> moodList= getDriver().findElements(By.cssSelector(BLOCKS_SEL+CHECKBOX_INPUT_SEL));
                for(WebElement list:moodList){
                    if(list.isEnabled()){
                        setMood(TestFormPO.index_mood);
                    }
                }
                getDriver().findElement(By.cssSelector(BLOCKS_SEL+TEXT_INPUT_SEL)).clear();
                break;
            }
        }
    }

    public String getInputTitle(){
        return getElementText(By.cssSelector(BLOCKS_SEL+INPUT_TITLE_SEL));
    }

    public String getErrorText(){
        return getElementText(By.cssSelector(BLOCKS_SEL+ERROR_TEXT_SEL));
    }

    //отримати текст в текстових полях
    public String getInputText(){
        return getDriver().findElement(By.cssSelector(BLOCKS_SEL+TEXT_INPUT_SEL)).getAttribute("data-initial-value");
    }

}
