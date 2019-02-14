import crawler.Crawler;

import java.io.IOException;

/**
 * Created by nurie on 13.02.2019.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String URL = "http://developer.alexanderklimov.ru/";
        int pageCount = 40;
        Crawler crawler = new Crawler(URL, pageCount);
        System.out.println(crawler.getCounter());
    }
}
