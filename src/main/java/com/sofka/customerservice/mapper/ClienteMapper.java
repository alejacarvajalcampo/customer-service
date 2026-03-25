package com.sofka.customerservice.mapper;

import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.dto.ClienteRequest;
import com.sofka.customerservice.dto.ClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "personaId", ignore = true)
    Cliente toEntity(ClienteRequest request);

    ClienteResponse toResponse(Cliente cliente);
}
