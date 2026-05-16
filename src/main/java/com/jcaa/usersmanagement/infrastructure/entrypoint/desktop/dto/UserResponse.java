package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

// Clean Code - Regla 2 (Uso adecuado de estructuras de datos):
// Se utiliza una clase mutable con @Data para representar un DTO de salida.
// Esto introduce setters innecesarios y permite modificar el objeto después de creado.
// La regla dice: los DTO deben ser simples contenedores de datos, preferiblemente inmutables.
// Solución: usar un record para representar el DTO de salida.
// Clean Code - Regla 15 (Inmutabilidad como preferencia de diseño):
// El uso de @Data genera setters públicos, permitiendo cambiar el estado del objeto
// desde cualquier parte del sistema.
// La regla dice: los objetos deben ser inmutables cuando no hay necesidad de mutabilidad.
// Solución: eliminar setters utilizando record o @Value.
public record UserResponse (
        String id,
        String name,
        String email,
        String role,
        String status
) {}
