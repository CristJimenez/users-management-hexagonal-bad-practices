package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserNameException extends DomainException {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private  static final String VALUE_EMPTY_MESSAGE = "The user name must not be empty.";
  private  static final String LENGTH_SHORT_MESSAGE = "The user name must have at least %d characters.";

  private InvalidUserNameException(final String message) {
    super(message);
  }

  public static InvalidUserNameException becauseValueIsEmpty() {
    return new InvalidUserNameException(VALUE_EMPTY_MESSAGE);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserNameException(
        String.format(LENGTH_SHORT_MESSAGE, minimumLength));
  }
}
