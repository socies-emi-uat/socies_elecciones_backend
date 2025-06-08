package com.socies.voto.dtos.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginAuthResponseDTO {
  Long id;
  String name;
  String rol;
  String email;
  String token;
}
