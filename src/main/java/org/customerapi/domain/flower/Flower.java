package org.customerapi.domain.flower;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Flower {

    private String id ;

    private String commonName;

    private String family;

    private String color;

    private String type;

    private String origin;

    private Boolean available;
}
