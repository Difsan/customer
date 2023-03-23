package org.customerapi.domain.flower;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Flower {

    private String id ;

    private String commonName;

    private String family;

    private String color;

    private String type;

    private String origin;

    private Boolean inStock;
}
