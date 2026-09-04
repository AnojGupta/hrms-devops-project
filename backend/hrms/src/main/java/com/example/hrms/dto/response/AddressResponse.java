package com.example.hrms.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
