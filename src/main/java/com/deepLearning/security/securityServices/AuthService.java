package com.deepLearning.security.securityServices;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.model.User;

public interface AuthService {

    public String authenticate(AuthCredentials credentials);

    public boolean registration(AuthCredentials credentials);
}
