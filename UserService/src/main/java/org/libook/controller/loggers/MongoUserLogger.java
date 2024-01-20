package org.libook.controller.loggers;

import org.libook.controller.connections.MongoConnection;
import org.libook.controller.loaders.MongoUserLoader;
import org.libook.controller.loaders.UserLoader;
import org.libook.controller.sessions.SessionHandler;
import org.libook.model.User;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class MongoUserLogger implements UserLogger{
    private final SessionHandler sessionHandler;
    private final UserLoader userLoader;
    private final MongoConnection datamartConnection;

    public MongoUserLogger(MongoConnection datamartConnection, SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
        this.userLoader = new MongoUserLoader(datamartConnection);
        this.datamartConnection = datamartConnection;
    }

    @Override
    public String logUser(String username, String password) {
        User user = userLoader.getUser(username, password);
        if (exists(user))
            return sessionHandler.getSessionToken(user);
        else
            throw new RuntimeException("User doesn't exists or bad credentials were given");
    }

    private boolean badCredentials(User user) {
        return user != null && user.username().equals("");
    }

    private boolean exists(User user) {
        return user != null && !badCredentials(user);
    }

    private User saveToDatamart(String username, String password) {
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Document userDocument = new Document()
                .append("username", username)
                .append("password", hashPassword)
                .append("documents", new ArrayList<>());

        datamartConnection.collection().insertOne(userDocument);
        return new User(username);
    }
}
