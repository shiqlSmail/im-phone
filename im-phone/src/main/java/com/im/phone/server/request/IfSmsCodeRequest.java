package com.im.phone.server.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class IfSmsCodeRequest {
    private String phone;
    private String smscode;
}
