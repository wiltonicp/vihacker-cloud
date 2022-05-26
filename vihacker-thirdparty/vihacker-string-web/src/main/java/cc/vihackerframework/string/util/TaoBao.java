package cc.vihackerframework.string.util;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Ranger on 2022/5/21.
 */
public class TaoBao {

    /**
     * 火狐浏览器驱动
     */
    public final static String webdriver = "/Users/wilton/software/firefox-geckodriver/geckodriver";

    public final static String SPIDER_URL = "https://detail.tmall.com/item.htm?spm=a230r.1.14.22.53de57e1Kl7oQx&id=673091255350&ns=1&abbucket=16&sku_properties=20105:103646;20122:15515349;1627207:44502";

    public static void spider(){
        WebDriver driver = null;
        //加载本地浏览器
        System.setProperty("webdriver.gecko.driver", webdriver);
        driver = new FirefoxDriver();

        /**
         * 要调起新版本的firefox，需要geckodriver驱动（未设置时java.lang.IllegalStateException报错）
         */
        driver.get(SPIDER_URL);
        String title = driver.getTitle();
        System.out.println(title);
    }

    public static void main(String[] args) {
        spider();
    }
}


