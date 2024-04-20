package com.zxpan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String topic = null;
        boolean isValidInput = false;
        while (!isValidInput) {
            System.out.println("Please enter a topic (press Enter to end):");
            topic = scanner.nextLine();
            if (!topic.isEmpty()) {
                isValidInput = true;
            } else {
                System.out.println("The input cannot be empty, please re-enter.");
            }
        }
        cochraneLibraryCrawler(topic);
        scanner.close();
    }

    private static void cochraneLibraryCrawler(String topic) {
        System.out.println("Searching for topic : " + topic);
        String inputUrl = "http://www.cochranelibrary.com/home/topic-and-review-group-list.html?page=topic";
        try {
            Document doc = Jsoup.connect(inputUrl).get();
            Elements reviewElements = doc.select(".browse-by-list-item");
            System.out.println("----------------------------------------------");
            String topicReviewsUrl = getTopicReviewsUrl(reviewElements, topic);
            if (topicReviewsUrl == null) {
                System.err.println("The topic you entered does not exist, please check the spelling and pay attention to the space");
                return;
            }

            // discover all of the reviews:
            Document docByTopic = Jsoup.connect(topicReviewsUrl).get();
            // Total Reviews for topic
            int topicTotalReviewCount = Integer.valueOf(docByTopic.select(".results-number").text());
            System.out.println("Total reviews' count : " + topicTotalReviewCount);

            // Total pages for topic
//            int topicPages = Integer.valueOf(docByTopic.select(".pagination-page-list-item").last().text());
            int topicPages = (int) Math.ceil(topicTotalReviewCount * 1.0 / 25);
            System.out.println("Total pages : " + topicPages);

            final String baseUrl = "https://www.cochranelibrary.com";
            List<Article> articleList = parseReviews(baseUrl, topic, topicPages, topicReviewsUrl);

            // format
            String textData = articleList.stream().map(Article::toString).collect(Collectors.joining("\n"));
            //Writing to file.
            writeTextDataIntoFile(textData);
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeTextDataIntoFile(String textToWrite) {
        String fileName = "cochrane_reviews.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(textToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Article> parseReviews(String baseUrl, String topic, int topicPages, String topicReviewsUrl) {
        List<Article> articleList = new ArrayList<>();
        try {
            for (int i = 1; i <= topicPages; i++) {
                String curUrl = topicReviewsUrl + "&cur=" + i;
                Elements curPageElements = Jsoup.connect(curUrl).get().select(".search-results-item");
                for (Element curElement : curPageElements) {
                    String url = baseUrl + curElement.select(".result-title a").attr("href");
//                    System.out.println(url);
                    String title = curElement.select(".result-title").text();
                    String author = curElement.select(".search-result-authors").text();
                    String date = curElement.select(".search-result-metadata-block .search-result-date").text();
                    articleList.add(new Article(url, topic, title, author, date));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articleList;
    }

    private static String getTopicReviewsUrl(Elements elements, String topic) {
        String topicReviewsUrl = null;
        for (Element element : elements) {
            if (element.select(".btn-link").text().toLowerCase().equals(topic.toLowerCase())) {
                topicReviewsUrl = element.select("a").attr("href");
            }
        }
        return topicReviewsUrl;
    }
}
