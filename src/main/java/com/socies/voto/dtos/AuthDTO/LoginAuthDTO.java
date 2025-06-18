package com.socies.voto.dtos.AuthDTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginAuthDTO {

    @NonNull String login;

    @NonNull String password;
}
