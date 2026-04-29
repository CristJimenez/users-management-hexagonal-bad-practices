package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.SaveUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Set;

@Log
@RequiredArgsConstructor
public final class CreateUserService implements CreateUserUseCase {

  private final SaveUserPort saveUserPort;
  private final GetUserByEmailPort getUserByEmailPort;
  private final EmailNotificationService emailNotificationService;
  private final Validator validator;

  @Override
  public UserModel execute(final CreateUserCommand command) {
    // Clean Code - Regla 1 (Responsabilidad única):
    // El método execute realiza múltiples tareas
    // construcción del dominio, persistencia y notificación.
    // La regla dice: una función debe tener una sola responsabilidad.
    // Solución: dividir en métodos privados con responsabilidades claras.

    // Clean Code - Regla 2 (Funciones pequeñas):
    // El método es largo y mezcla múltiples pasos.
    // La regla dice: las funciones deben ser pequeñas y fáciles de entender.
    // Solución: extraer funciones como validateCommand, checkUserExists, buildUser, etc.

    // Clean Code - Regla 3 (Un solo nivel de abstracción):
    // Se mezclan operaciones de alto nivel (crear usuario) con detalles de bajo nivel
    // (new UserId, new UserEmail, etc.).
    // La regla dice: una función debe operar en un solo nivel de abstracción.
    // Solución: delegar la construcción del dominio a un mapper o fábrica.

    // Clean Code - Regla 9 (Comentarios innecesarios):
    // Se usan comentarios para explicar código que podría ser más expresivo.
    // La regla dice: el código debe ser autoexplicativo.
    // Solución: eliminar comentarios y mejorar nombres/métodos.

    // Clean Code - Regla 10 (Comentarios redundantes):
    // Comentarios como "guardar el usuario" o "retornar el usuario" no aportan valor.
    // Solución: eliminar comentarios redundantes.
    validateCommand(command);

    final UserEmail email = new UserEmail(command.email());
    ensureUserDoesNotExist(email);

    final UserModel userToSave = new UserModel(
        new UserId(command.id()),
        new UserName(command.name()),
        new UserEmail(command.email()),
        UserPassword.fromPlainText(command.password()),
        UserRole.fromString(command.role()),
        UserStatus.PENDING);

    final UserModel savedUser = saveUserPort.save(userToSave);

    notifyUser(savedUser, command.password());

    return savedUser;
  }

  private void validateCommand(final CreateUserCommand command) {
    final Set<ConstraintViolation<CreateUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureUserDoesNotExist(final UserEmail email) {
    if (getUserByEmailPort.getByEmail(email).isPresent()) {
      throw UserAlreadyExistsException.becauseEmailAlreadyExists(email.value());
    }
  }

  private void notifyUser(final UserModel user, final String password) {
    emailNotificationService.notifyUserCreated(user, password);
  }
}
