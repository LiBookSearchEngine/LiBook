package org.libook.controller.sessions;

import org.libook.model.User;

public interface SessionHandler {
    String getSessionToken(User user);
    User hasValidSession(String session);
}
