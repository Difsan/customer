package org.customerapi.domain.dto;


import org.customerapi.domain.flower.Flower;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    @Id
    private String id;

    private String name;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Set<Flower> flowers = new HashSet<>();

}
