package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCredentialsException extends DomainException {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private static final String CREDENTIALS_INVALID_MESSAGE = "Correo o contraseña incorrectos.";
  private static final String USER_NOT_ACTIVE_MESSAGE = "Tu cuenta no está activa. Contacta al administrador.";

  private InvalidCredentialsException(final String message) {
    super(message);
  }

  public static InvalidCredentialsException becauseCredentialsAreInvalid() {
    return new InvalidCredentialsException(CREDENTIALS_INVALID_MESSAGE);
  }

  public static InvalidCredentialsException becauseUserIsNotActive() {
    return new InvalidCredentialsException(USER_NOT_ACTIVE_MESSAGE);
  }
}
