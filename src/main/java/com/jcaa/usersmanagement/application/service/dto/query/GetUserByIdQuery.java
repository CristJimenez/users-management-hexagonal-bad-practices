package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;
// Clean Code - Regla 3 (Evitar redundancia innecesaria):
// Se utiliza @Builder en un record, lo cual es redundante porque los records
// ya proporcionan un constructor canónico.
// La regla dice: evitar añadir abstracciones innecesarias cuando el lenguaje ya provee una solución.
// Solución: eliminar @Builder.
public record GetUserByIdQuery(
    // VIOLACIÓN Regla 3: se usa mensaje personalizado en la constraint.
    // La regla indica dejar los mensajes por default — no usar atributo message=.
    @NotBlank String id) {

}
