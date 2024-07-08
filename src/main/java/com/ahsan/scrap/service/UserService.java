package com.ahsan.scrap.service;


import java.util.List;

import com.ahsan.scrap.model.UserDtls;

public interface UserService {
    public UserDtls createUser(UserDtls user);
    public UserDtls createUserByAdmin(UserDtls user);
    public boolean checkUsername(String username);
    public List<UserDtls> getUserDtls();
    public UserDtls findUserById(Long id);
    public UserDtls updateUserDtls(UserDtls user);
}
