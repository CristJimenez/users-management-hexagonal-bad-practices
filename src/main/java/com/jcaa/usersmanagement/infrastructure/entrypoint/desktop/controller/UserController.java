package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.LoginRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.UserDesktopMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class UserController {

  private final CreateUserUseCase createUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;
  private final GetUserByIdUseCase getUserByIdUseCase;
  private final GetAllUsersUseCase getAllUsersUseCase;
  private final LoginUseCase loginUseCase;

  public List<UserResponse> listAllUsers() {
    // Clean Code - Regla 4 (Nombres descriptivos):
    // Se utiliza la abreviatura "usrs", lo cual reduce la claridad del código.
    // La regla dice: los nombres deben ser claros, completos y expresar intención.
    // Solución: renombrar la variable a "users".
    final var users = getAllUsersUseCase.execute();
    return UserDesktopMapper.toResponseList(users);
  }

  public UserResponse findUserById(final String id) {
    // Clean Code - Regla 20 (Objeto antes que primitivo cuando el concepto lo merezca):
    // Se utiliza String para representar un identificador de usuario, ignorando el value object UserId.
    // Esto permite valores inválidos y debilita el contrato del método.
    // La regla dice: usar tipos del dominio en lugar de primitivos para representar conceptos importantes.
    // Solución: recibir un UserId o convertir inmediatamente el String a UserId en el mapper.
    final var query = UserDesktopMapper.toGetByIdQuery(id);
    final var user = getUserByIdUseCase.execute(query);
    return UserDesktopMapper.toResponse(user);
  }

  public UserResponse createUser(final CreateUserRequest request) {
    // Clean Code - Regla 9 (Arquitectura Hexagonal - Aislamiento del entrypoint):
    // El controlador construye directamente comandos de la capa de aplicación (CreateUserCommand,
    // DeleteUserCommand, LoginCommand), acoplándose a detalles internos.
    // La regla dice: el entrypoint no debe conocer ni construir objetos internos de la aplicación.
    // Solución: delegar la conversión de request a command a un mapper (UserDesktopMapper).
    final var command = UserDesktopMapper.toCreateCommand(request);
    final var user = createUserUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }

  public UserResponse updateUser(final UpdateUserRequest request) {
    final var command = UserDesktopMapper.toUpdateCommand(request);
    final var user = updateUserUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }

  public void deleteUser(final String id) {
    // Clean Code - Regla 9 (Arquitectura Hexagonal - Aislamiento del entrypoint):
    // El controlador construye directamente comandos de la capa de aplicación (CreateUserCommand,
    // DeleteUserCommand, LoginCommand), acoplándose a detalles internos.
    // La regla dice: el entrypoint no debe conocer ni construir objetos internos de la aplicación.
    // Solución: delegar la conversión de request a command a un mapper (UserDesktopMapper).
    final var command = UserDesktopMapper.toDeleteCommand(id);
    deleteUserUseCase.execute(command);
  }

  public UserResponse login(final LoginRequest request) {
    // Clean Code - Regla 9 (Arquitectura Hexagonal - Aislamiento del entrypoint):
    // El controlador construye directamente comandos de la capa de aplicación (CreateUserCommand,
    // DeleteUserCommand, LoginCommand), acoplándose a detalles internos.
    // La regla dice: el entrypoint no debe conocer ni construir objetos internos de la aplicación.
    // Solución: delegar la conversión de request a command a un mapper (UserDesktopMapper).
    final var command = UserDesktopMapper.toLoginCommand(request);
    final var user = loginUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }
}
