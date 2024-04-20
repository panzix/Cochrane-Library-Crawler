package com.zxpan;

import org.jsoup.internal.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @ClassName Article
 * @Description TODO
 * @Author Panzi
 * @Date 2024/4/19 15:18
 * @Version 1.0
 */
public class Article {
    private String url;
    private String topic;
    private String title;
    private String author;
    private String date;

    public Article(String topic) {
        this.topic = topic;
    }

    public Article(String url, String topic, String title, String author, String publicationDate) {
        this.url = url;
        this.topic = topic;
        this.title = title;
        this.author = author;
        this.date = dateParser(publicationDate);
    }

    public static String dateParser(String inputDate) {
        if (StringUtil.isBlank(inputDate)) return inputDate;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = inputDateFormat.parse(inputDate);
            String formattedDate = outputDateFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }

    @Override
    public String toString() {
        return url + " | " + topic + " | " + title + " | " + author + " | " + date + "\n\n";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
