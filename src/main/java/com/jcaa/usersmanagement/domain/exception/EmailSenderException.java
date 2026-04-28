package com.jcaa.usersmanagement.domain.exception;

public final class EmailSenderException extends DomainException {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private static final String SMT_FAILED_MESSAGE = "No se pudo enviar el correo a '%s'. Error SMTP: %s";
  private static final String SEND_FAILED_MESSAGE = "La notificación por correo no pudo ser enviada.";

  // Clean Code - Regla 9 (Control de creación de excepciones):
  // Se exponen constructores públicos, permitiendo crear la excepción con cualquier mensaje,
  // rompiendo consistencia y control del dominio.
  // La regla dice: las excepciones de dominio deben crearse mediante factory methods
  // y tener constructores privados para garantizar mensajes consistentes.
  // Solución: hacer los constructores privados y forzar el uso de métodos estáticos.
  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    return new EmailSenderException(
        String.format(SMT_FAILED_MESSAGE, destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    return new EmailSenderException(SEND_FAILED_MESSAGE, cause);
  }
}
