package org.customerapi.domain.dto;


import org.customerapi.domain.flower.Flower;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

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

    private List<Flower> flowers = new ArrayList<>();

}
