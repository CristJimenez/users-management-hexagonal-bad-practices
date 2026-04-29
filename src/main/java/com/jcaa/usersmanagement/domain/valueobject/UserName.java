package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserNameException;

import java.util.Objects;

public record UserName(String value) {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private static final String NULL_MESSAGE = "UserName cannot be null";
  private static final int MINIMUM_LENGTH = 3;

  public UserName {
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.requireNonNull() o Objects.isNull().
    // Para objetos siempre debe usarse Objects.isNull/nonNull, nunca operadores == o !=.
    if (Objects.isNull(value)) {
      throw new NullPointerException(NULL_MESSAGE);
    }
    final String normalizedValue = value.trim();
    // Clean Code - Regla 23 (Minimizar conocimiento disperso):
    // La validación del email está distribuida en múltiples lugares (regex, utils, anotaciones).
    // Esto dificulta el mantenimiento y genera inconsistencias.
    // La regla dice: una regla de negocio debe tener una única fuente de verdad.
    // Solución: centralizar la validación en este Value Object y eliminar duplicaciones externas.
    if (normalizedValue.isEmpty()) {
      throw InvalidUserNameException.becauseValueIsEmpty();
    }

    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidUserNameException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
