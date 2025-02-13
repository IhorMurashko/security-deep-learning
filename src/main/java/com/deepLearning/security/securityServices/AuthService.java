package com.deepLearning.security.securityServices;

import com.deepLearning.security.dto.AuthCredentials;
import com.deepLearning.security.model.User;

import java.util.Map;

public interface AuthService {

    public Map<String, String> authenticate(AuthCredentials credentials);

    public boolean registration(AuthCredentials credentials);
}
