package com.im.phone.server.request;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class UserRegisterRequest{
    private String password;
    private String phone;
    private String smscode;
}
