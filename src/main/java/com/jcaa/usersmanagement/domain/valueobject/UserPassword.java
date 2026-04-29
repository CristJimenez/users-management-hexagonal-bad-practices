package com.jcaa.usersmanagement.domain.valueobject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.jcaa.usersmanagement.domain.exception.InvalidUserPasswordException;
import java.util.Objects;

public final class UserPassword {

  // Clean Code - Regla 10 (Evitar literales mágicos):
  // Los mensajes están hardcodeados directamente en los métodos.
  // Esto dificulta mantenimiento y reutilización.
  // La regla dice: los textos deben definirse como constantes.
  // Solución: extraer los mensajes a constantes privadas estáticas.

  private static final String NULL_MESSAGE = "Password cannot be null";
  private static final String HASH_NULL_MESSAGE = "Password hash cannot be null";
  private static final String PLAIN_NULL_MESSAGE = "Plain password cannot be null";
  private static final int MINIMUM_LENGTH = 8;
  private static final int BCRYPT_COST = 12;

  private final String value;

  private UserPassword(final String value) {
    this.value = value;
  }

  /**
   * Crea un UserPassword desde texto plano: valida y aplica hash BCrypt. Usar cuando el usuario
   * crea o cambia su contraseña.
   */
  public static UserPassword fromPlainText(final String plainText) {
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.isNull() o Objects.requireNonNull()
    if (Objects.isNull(plainText)) {
      throw new NullPointerException(NULL_MESSAGE);
    }
    final String normalizedValue = plainText.trim();
    // Clean Code - Regla 23 (Minimizar conocimiento disperso):
    // La validación del email está distribuida en múltiples lugares (regex, utils, anotaciones).
    // Esto dificulta el mantenimiento y genera inconsistencias.
    // La regla dice: una regla de negocio debe tener una única fuente de verdad.
    // Solución: centralizar la validación en este Value Object y eliminar duplicaciones externas.
    if (normalizedValue.isEmpty()) {
      throw InvalidUserPasswordException.becauseValueIsEmpty();
    }

    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidUserPasswordException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
    final String hash = BCrypt.withDefaults().hashToString(BCRYPT_COST, normalizedValue.toCharArray());
    return new UserPassword(hash);
  }

  /**
   * Crea un UserPassword desde un hash ya almacenado en base de datos. No re-valida ni re-hashea.
   */
  public static UserPassword fromHash(final String hash) {
    Objects.requireNonNull(hash, HASH_NULL_MESSAGE);
    return new UserPassword(hash);
  }


  /** Verifica un texto plano contra el hash BCrypt almacenado. */
  public boolean verifyPlain(final String plainText) {
    final String normalizedPlain =
        Objects.requireNonNull(plainText, PLAIN_NULL_MESSAGE).trim();
    final BCrypt.Result result = BCrypt.verifyer().verify(normalizedPlain.toCharArray(), value);
    return result.verified;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof UserPassword userPassword)) return false; // NOSONAR: rama instanceof no testeable sin warnings
    return Objects.equals(value, userPassword.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

}
