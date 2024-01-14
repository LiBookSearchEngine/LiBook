package org.crawler;

import org.crawler.controllers.CrawlerController;

public class Main {
    public static void main(String[] args) throws Exception {
        CrawlerController controller = new CrawlerController();
        controller.run();
    }
}