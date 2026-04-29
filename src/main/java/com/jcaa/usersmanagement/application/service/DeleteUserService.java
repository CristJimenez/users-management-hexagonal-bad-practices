package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteUserPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class DeleteUserService implements DeleteUserUseCase {

  // Clean Code - Regla 6 (Uso de logging):
  // Se define manualmente un Logger en lugar de usar @Log de Lombok,
  // generando inconsistencia con el resto del código.
  // La regla dice: mantener consistencia en herramientas y evitar código repetitivo.
  // Solución: eliminar el Logger o usar @Log si realmente se necesita.

  private final DeleteUserPort deleteUserPort;
  private final GetUserByIdPort getUserByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteUserCommand command) {
    // Clean Code - Regla 6 (Manejo adecuado de excepciones):
    // Se utiliza un try-catch que captura Exception sin posibilidad real de recuperación.
    // La excepción se loguea y se vuelve a lanzar sin agregar valor.
    // La regla dice: no capturar excepciones si no puedes manejarlas.
    // Solución: eliminar el bloque try-catch y dejar que la excepción se propague.

    validateCommand(command);
    final UserId userId = UserApplicationMapper.fromDeleteCommandToUserId(command);
    ensureUserExists(userId);
    deleteUserPort.delete(userId);
  }

  private void validateCommand(final DeleteUserCommand command) {
    final Set<ConstraintViolation<DeleteUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureUserExists(final UserId userId) {
    getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }
}
