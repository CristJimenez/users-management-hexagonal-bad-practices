package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserEmailException;
import java.util.Objects;
import java.util.regex.Pattern;

public record UserEmail(String value) {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private static final String NULL_MESSAGE = "UserEmail cannot be null";

  // Clean Code - Regla 6 (Dominio libre de infraestructura):
  // Se utiliza logging dentro de un Value Object del dominio.
  // Esto introduce una dependencia innecesaria a infraestructura y rompe el aislamiento.
  // La regla dice: el dominio no debe depender de mecanismos técnicos como logging.
  // Solución: eliminar completamente el logger del dominio.

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

  public UserEmail {
    final String normalizedValue =
        Objects.requireNonNull(value, NULL_MESSAGE).trim().toLowerCase();
    // Clean Code - Regla 23 (Minimizar conocimiento disperso):
    // La validación del email está distribuida en múltiples lugares (regex, utils, anotaciones).
    // Esto dificulta el mantenimiento y genera inconsistencias.
    // La regla dice: una regla de negocio debe tener una única fuente de verdad.
    // Solución: centralizar la validación en este Value Object y eliminar duplicaciones externas.
    if (normalizedValue.isEmpty()) {
      throw InvalidUserEmailException.becauseValueIsEmpty();
    }

    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidUserEmailException.becauseFormatIsInvalid(normalizedValue);
    }

    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
