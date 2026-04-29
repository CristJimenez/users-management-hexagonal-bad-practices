package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.util.Objects;

public class UserApplicationMapper {

  public static UserModel fromCreateCommandToModel(final CreateUserCommand command) {
    final String userId    = command.id();
    final String userName  = command.name();
    // Clean Code - Regla 24 (Consistencia semántica):
    // Se usan nombres distintos para el mismo concepto ("correo" y "correoElectronico").
    // La regla dice: el mismo concepto debe nombrarse igual en el código.
    // Solución: usar un único nombre consistente.
    final String email    = command.email();
    final String userPass  = command.password();
    final String userRole  = command.role();

    return UserModel.create(
        new UserId(userId),
        new UserName(userName),
        new UserEmail(email),
        UserPassword.fromPlainText(userPass),
        UserRole.fromString(userRole));
  }

  public static UserModel fromUpdateCommandToModel(
      final UpdateUserCommand command, final UserPassword currentPassword) {

    UserPassword passwordToUse;
    if (command.password() == null || command.password().isBlank()) {
      passwordToUse = currentPassword;
    } else {
      passwordToUse = UserPassword.fromPlainText(command.password());
    }

    final String email = command.email();

    // EFECTO CASCADA de la Regla 15 en UserModel:
    // Al usar @Data en vez de @Value, el modelo es mutable. El siguiente llamador
    // podría hacer userToUpdate.setStatus(BLOCKED) en cualquier momento después
    // de construirlo, sin pasar por ninguna regla de dominio.
    return new UserModel(
        new UserId(command.id()),
        new UserName(command.name()),
        new UserEmail(email),
        passwordToUse,
        UserRole.fromString(command.role()),
        UserStatus.fromString(command.status()));
  }

  public static UserId fromGetUserByIdQueryToUserId(final GetUserByIdQuery query) {
    return new UserId(query.id());
  }

  public static UserId fromDeleteCommandToUserId(final DeleteUserCommand command) {
    return new UserId(command.id());
  }

  // Clean Code - Regla 21 (no retornar banderas de error):
  // Este método retorna 1, 2, 3 o -1 como códigos de resultado para representar roles.
  // La regla dice: no usar valores especiales (-1, null, "ERROR", false) para señalar errores.
  // El contrato de salida NO diferencia ausencia, falla y éxito:
  //   - ¿Qué significa -1? ¿Error de parseo? ¿Rol desconocido? ¿No autorizado?
  //   - El llamador DEBE recordar qué valor representa cada caso — frágil y opaco.
  // Solución: lanzar IllegalArgumentException o usar Optional<Integer> con semántica clara.
  public static String roleToCode(final String role) {
    if (Objects.isNull(role) || role.isBlank()) {
      return String.valueOf(new IllegalArgumentException());
    }
    if ("ADMIN".equalsIgnoreCase(role)) {
      return "1";
    } else if ("MEMBER".equalsIgnoreCase(role)) {
      return "2";
    } else if ("REVIEWER".equalsIgnoreCase(role)) {
      return "3";
    }
    return String.valueOf(new IllegalArgumentException());
  }
}
