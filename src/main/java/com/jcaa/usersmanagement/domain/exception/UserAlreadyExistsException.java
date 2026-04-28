package com.jcaa.usersmanagement.domain.exception;

public final class UserAlreadyExistsException extends DomainException {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private static final String ALREADY_EXISTS_MESSAGE = "A user with email '%s' already exists.";

  private UserAlreadyExistsException(final String message) {
    super(message);
  }

  public static UserAlreadyExistsException becauseEmailAlreadyExists(final String email) {
    return new UserAlreadyExistsException(String.format(ALREADY_EXISTS_MESSAGE, email));
  }
}
