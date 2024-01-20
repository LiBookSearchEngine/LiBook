package org.libook.controller.loaders;

import org.libook.model.User;

public interface UserLoader {
    User getUser(String user, String Password);
}
