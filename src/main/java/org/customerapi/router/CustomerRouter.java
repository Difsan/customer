package org.customerapi.router;


import org.customerapi.domain.dto.CustomerDTO;
import org.customerapi.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CustomerRouter {

    @Bean
    public RouterFunction<ServerResponse> getAllCustomers (GetAllCustomersUseCase getAllCustomersUseCase){
        return route(GET("/customers"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getAllCustomersUseCase.get(), CustomerDTO.class))
                        .onErrorResume(throwable -> ServerResponse.noContent().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> getCustomerById(GetCustomerByIdUseCase getCustomerByIdUseCase){
        return route(GET("/customers/{id}"),
                request -> getCustomerByIdUseCase.apply(request.pathVariable("id"))
                        .flatMap(customerDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(customerDTO))
                        //.onErrorResume(throwable -> ServerResponse.notFound().build())
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveCustomer (SaveCustomerUseCase saveCustomerUseCase){
        return route(POST("/customers").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(CustomerDTO.class)
                        .flatMap(customerDTO -> saveCustomerUseCase.save(customerDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(throwable.getMessage())))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> updateCustomer(UpdateCustomerUseCase updateCustomerUseCase){
        return route(PUT("/customers/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(CustomerDTO.class)
                        .flatMap(customerDTO -> updateCustomerUseCase.update(request.pathVariable("id"),
                                        customerDTO)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(throwable.getMessage()))
                        ));
    }

    @Bean
    public RouterFunction<ServerResponse> deleteCustomer (DeleteCustomerUseCase deleteCustomerUseCase){
        return route(DELETE("/customers/{id}"),
                request -> deleteCustomerUseCase.apply(request.pathVariable("id"))
                        .thenReturn(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("Customer deleted"))
                        .flatMap(serverResponseMono -> serverResponseMono)
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }




}
