package org.libook;

import org.libook.api.APIController;

public class UserBooksProcessorMain {
    public static void main(String[] args) throws Exception {
        APIController apiController = new APIController();
        apiController.run();
    }
}
