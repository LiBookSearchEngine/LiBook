import org.libook.api.APIController;
import controller.sessions.cookie.SessionHazelcastHandler;

import javax.jms.JMSException;

public class UserServiceMain {
    public static void main(String[] args) throws JMSException {
        SessionHazelcastHandler sessionHandler = new SessionHazelcastHandler();
        APIController apiController = new APIController(sessionHandler);
        apiController.run();
    }
}
