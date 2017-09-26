import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static ArrayList<Review> reviewers = new ArrayList<Review>();

    public static void parseComments(Document doc) {
        Elements authorsNames = doc.getElementsByClass("pp-review-author-name");
//        Elements authorStars = doc.getElementsByClass("g-rating-stars");
//        System.out.println(authorStars.first().attr("content"));
        Elements authorsText = doc.getElementsByClass("pp-review-text");

        for (int i = 0; i < authorsNames.size(); i++) {
            reviewers.add(new Review(authorsNames.get(i).text(), authorsText.get(i).text()));
            System.out.println(authorsNames.get(i).text() + " - " + authorsText.get(i).text());
        }

    }

    public static void parseReviews(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        int reviews = Integer.parseInt(doc.getElementsByClass("m-tabs-i-comments").text());
        System.out.println("Amount of reviews: " + reviews);

        if (reviews > 10) {
            int pages = Integer.parseInt(Jsoup.connect(url).get().getElementsByClass("paginator-catalog-l-link").last().text());
            System.out.println("Pages of reviews: " + pages);
        }

        parseComments(doc);

    }

    public static void parseCategoryPage(String url, int i) throws IOException {
        Document doc = Jsoup.connect(url + "page=" + i + "/").get();
        Elements links = doc.select("div.g-i-tile-i-title").nextAll();
        for (Element link : links) {
            String linkHref = link.getAllElements().attr("href");
            if (linkHref.contains("http")) {
                System.out.println("\nlink: " + linkHref);
                parseReviews(linkHref);
            }
        }
    }

    public static void main(String[] args) {

        String url = "https://rozetka.com.ua/ua/tablets/c130309/filter/";

        try {
            int num = Integer.parseInt(Jsoup.connect(url).get().getElementsByClass("novisited paginator-catalog-l-link").last().text());
            System.out.println("Number of pages: "+ num);
            for (int i = 1; i <= 1; i++) {
                parseCategoryPage(url, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class Review {
    public String author = "", text = "", stars = "none";

    public String getText() {
        return author + " - " + text;
    }

    Review(String author, String stars, String text) {
        this.author = author;
        this.stars = stars;
        this.text = text;
    }

    Review(String author, String text) {
        this.author = author;
        this.stars = "";
        this.text = text;
    }

}
