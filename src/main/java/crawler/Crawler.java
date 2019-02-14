package crawler;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nurie on 13.02.2019.
 */
public class Crawler {
    private String url;
    private int pageCount;
    private List<String> visitedPages = new LinkedList<String>();

    public String getUrl() {
        return url;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<String> getVisitedPages() {
        return visitedPages;
    }

    public int getCounter() {
        return counter;
    }

    private int counter = 0;


    public Crawler(String url, int pageCount) throws IOException {
        this.url = url;
        this.pageCount = pageCount;
        this.getPage(url);
    }

    public void getPage(String thisUrl) throws IOException {
        Document doc = null;
        boolean nextStep = true;
        System.out.println(thisUrl);
        try {
            doc = Jsoup.connect(thisUrl).get();
        } catch (Exception e) {
            nextStep = false;
        }
        if (nextStep && doc != null) {
            Elements content = doc.select("p");
            String ps = "";
            for (Element el : content) {
                ps = ps.concat(el.text().concat("\n"));
            }
            saveToFile(ps);

            Elements aElements = doc.select("a");

            for (Element el : aElements) {
                String href = el.attr("href");
                if (!href.startsWith(url)) {
                    if (!href.contains("http") && href.endsWith(".php")) {
                        href = url.concat(href);
                    } else {
                        continue;
                    }
                }
                if (!this.visitedPages.contains(href) && (href.startsWith(this.url) || href.endsWith(".php"))) {
                    visitedPages.add(href);
                    getPage(href);
                    if (visitedPages.size() >= pageCount) {
                        saveList();
                        break;
                    }
                }
            }
        }
    }


    private void saveToFile(String content) throws IOException {
        FileWriter fileWriter = new FileWriter(getFileName());
        fileWriter.write(content);
        fileWriter.close();
    }

    private String getFileName() {
        counter += 1;
        return "files/".concat(String.valueOf(counter)).concat(".txt");
    }

    private void saveList() throws IOException {
        FileWriter fileWriter = new FileWriter("files/".concat("List"));
        fileWriter.write(String.valueOf(visitedPages));
        fileWriter.close();
    }
}
