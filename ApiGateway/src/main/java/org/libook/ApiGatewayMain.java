package org.libook;

import org.libook.api.APIController;

public class ApiGatewayMain {
    public static void main(String[] args) {
        APIController apiController = new APIController();
        apiController.run();
    }
}
