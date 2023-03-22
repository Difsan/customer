package org.customerapi.domain.collection;


import org.customerapi.domain.flower.Flower;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    @NotNull(message = "Name can't be null")
    private String name;

    @NotNull(message = "Last Name can't be null")
    private String lastName;

    @NotNull(message = "email can't be null")
    private String email;

    @NotNull(message = "Phone Number can't be null")
    private String phoneNumber;

    private List<Flower> flowers;

    public Customer( String name, String lastName, String email, String phoneNumber) {
        this.id = UUID.randomUUID().toString().substring(0,10);
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.flowers = new ArrayList<>();
    }
}
