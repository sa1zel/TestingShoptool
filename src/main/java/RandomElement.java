import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomElement {
    private static List<String> elementsChecked = new LinkedList<>();
    private static Random rand;
    private static List<WebElement> randomElementsNoRepeat(List<WebElement> givenList) {
        List<WebElement> newElements = new LinkedList<>();
        rand = new Random();
        for (int i = 0; i < givenList.size(); i++) {
            int randomIndex = rand.nextInt(givenList.size());
            newElements.add(givenList.get(randomIndex));
            givenList.remove(randomIndex);
        }
        return newElements;
    }

    public static WebElement randomElement(List<WebElement> givenList){
        rand = new Random();
        return givenList.get(rand.nextInt(givenList.size()));
    }

    public static WebElement randElementNoRepeat(List<WebElement> givenList){
        List<WebElement> newElements = randomElementsNoRepeat(givenList);
        for(WebElement element : newElements){
            String name = element.getAttribute("name");
            if(isNoRepeat(name)){
                elementsChecked.add(name);
                return element;
            }
        }
        return null;
    }

    private static boolean isNoRepeat(String name) {
        for (String str : elementsChecked) {
            if (str.equals(name)) return false;
        }
        return true;
    }

}