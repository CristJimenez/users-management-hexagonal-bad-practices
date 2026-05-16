package com.jcaa.usersmanagement.application.service;

/**
 * Clean Code - Regla 13 (evitar clases utilitarias innecesarias):
 * Esta clase "Utils" agrupa métodos que en realidad pertenecen a sus respectivos objetos
 * de dominio (UserModel, UserRole, UserStatus) o a los servicios que los usan.
 * La regla dice: no crear clases Utils/Helper/Manager sin una razón sólida.
 * La lógica de negocio vive en los objetos de negocio, no en utilitarios genéricos.
 * Una clase llamada "UserValidationUtils" es señal de:
 *   - diseño pobre
 *   - lógica mal ubicada
 *   - falta de encapsulación en dominio o servicios
 * Clean Code - Regla 23 (minimizar conocimiento disperso):
 * Las reglas de validación de usuario están fragmentadas aquí en vez de estar
 * centralizadas en el propio UserModel o en un servicio de dominio dedicado.
 * Clean Code - Regla 12 (alta cohesión real):
 * Esta clase mezcla responsabilidades que no pertenecen al mismo concepto:
 *   - Validación de estado (isUserActive)
 *   - Validación de rol (isAdmin)
 *   - Validación de formato de email (isValidEmail)
 *   - Validación de contraseña (isValidPassword)
 *   - Verificación de permisos con parámetros mixtos (canPerformAction)
 * Sus métodos no trabajan sobre un mismo concepto o responsabilidad — son un
 * "contenedor de cosas relacionadas vagamente". Eso es exactamente baja cohesión.
 */
public final class UserValidationUtils {

  private UserValidationUtils() {
    throw new UnsupportedOperationException("Esta clase utilitaria no debe instanciarse");
  }
}


