package com.core.api.modules.core.infrastructure.servicesImpl;

import com.core.api.application.services.CustomTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.core.api.modules.core.domain.models.User;
import com.core.api.modules.core.domain.ports.dataAccess.UserPersistence;
import com.core.api.modules.core.application.statuses.CoreStatus;
import com.core.api.modules.core.application.services.UserService;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserPersistence userPersistence;
    @Autowired
    private CustomTimeService customTimeService;
    public static final String SALT = BCrypt.gensalt();

    @Override
    public Set<User> getAll(Integer idUser, String userName, String email, Integer status, Boolean onlyGraph) {
        return userPersistence.getAll(idUser, userName, email, status, onlyGraph);
    }

    @Override
    public User getById(Integer idUser) {
        return userPersistence.getById(idUser);
    }

    @Override
    public User save(User user) {
        if (user.getIdUser() != null) {
            User existingUser = userPersistence.getById(user.getIdUser());
            if (existingUser.getIdUser() != null) {
                if (!existingUser.getPassword().equals(user.getPassword())) {
                    updatePasswordIfNecessary(existingUser, user);
                    return userPersistence.save(user);
                }
            }else {
                user.setIdUser(null);
            }
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), SALT));
        user.setCreateAt(customTimeService.getLocalDataTime());
        return userPersistence.save(user);
    }

    @Override
    public User enable(User user) {
        user.setStatus(CoreStatus.ENABLE);
        user.getPerson().setStatus(CoreStatus.ENABLE);
        user.setDisabledAt(null);
        return userPersistence.save(user);
    }

    @Override
    public User disable(User user) {
        user.setStatus(CoreStatus.DISABLE);
        user.getPerson().setStatus(CoreStatus.DISABLE);
        user.setDisabledAt(customTimeService.getLocalDataTime());
        return userPersistence.save(user);
    }

    @Override
    public User delete(User user) {
        user.setStatus(CoreStatus.DELETED);
        user.getPerson().setStatus(CoreStatus.DELETED);
        user.setDisabledAt(customTimeService.getLocalDataTime());
        return userPersistence.save(user);
    }

    @Override
    public User login(String userName, String password) {
        User user = userPersistence.login(userName);
        if( user.getIdUser() != null ) {
            boolean checkPass = BCrypt.checkpw(password, user.getPassword());
            if( !checkPass ) {
                return null;
            }
        }
        user.setLastLogin(customTimeService.getLocalDataTime());
        userPersistence.save(user);
        return user;
    }

    private void updatePasswordIfNecessary(User existingUser, User updatedUser) {
        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
            updatedUser.setPassword(BCrypt.hashpw(updatedUser.getPassword(), SALT));
        }
    }

}