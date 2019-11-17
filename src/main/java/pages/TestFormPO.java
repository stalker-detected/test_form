package pages;

import org.openqa.selenium.By;
import utils.DriverManager;

import java.util.ArrayList;
import java.util.List;

public class TestFormPO extends BasePO {


    private static final String REL_URL = "/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/viewform";
    public TestFormPO() { super(REL_URL);}

    private final By BLOCKS_SEL = By.cssSelector(".freebirdFormviewerViewItemList > div");
    private final By SUBMIT_SEL = By.cssSelector(".quantumWizButtonPaperbuttonLabel.exportLabel");
    public static int index_mood;

    //відправити форму
    public void submit(){
        clickElement(SUBMIT_SEL);
    }

    public int getBlocksCount(){
        return DriverManager.getDriver().findElements(BLOCKS_SEL).size();
    }

    private List<Blocks> initFormsBlocks() {
        List<Blocks> productsInWishlist = new ArrayList<>();
        int i = 0;
        while (i < getBlocksCount()) {
            productsInWishlist.add(new Blocks(i+1));
            i++;
        }
        return productsInWishlist;
    }

    public Blocks getBlock(int i) {
        return initFormsBlocks().get(i - 1);
    }

}
