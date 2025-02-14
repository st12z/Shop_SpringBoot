package com.thucjava.shopapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    private String old_password;
    private String new_password;
}
