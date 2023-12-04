package com.springboot.blocket.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UpdateUserDto {
    private Optional<String> name;
    private Optional<String> email;
    private Optional<String> address;
/*    private Optional<String> role;*/
}
