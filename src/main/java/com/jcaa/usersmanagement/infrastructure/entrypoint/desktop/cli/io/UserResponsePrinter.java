package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-10s : %s%n";
  private static final String NO_USERS_FOUND_MESSAGE = "  No users found.";

  private final ConsoleIO console;

  public void print(final UserResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID",     response.id());
    console.printf(ROW_FORMAT, "Name",   response.name());
    console.printf(ROW_FORMAT, "Email",  response.email());
    console.printf(ROW_FORMAT, "Role",   response.role());
    // Clean Code - Regla 16: se llama al auxiliar que tiene la cadena if/else larga
    console.printf(ROW_FORMAT, "Status", UserStatus.fromString(response.status()).getDisplayLabel());
    console.println(SEPARATOR);
  }

  public void printList(final List<UserResponse> users) {
    // VIOLACIÓN Regla 5: si GetAllUsersService retorna null (lista vacía → null),
    // esta llamada a users.isEmpty() lanza NullPointerException en tiempo de ejecución.
    // Ningún método debe retornar null — se deben usar colecciones vacías.
    if (users.isEmpty()) {
      console.println(NO_USERS_FOUND_MESSAGE);
      return;
    }
    console.printf("%n  Total: %d user(s)%n", users.size());
    users.forEach(this::print);
  }

  // Clean Code - Regla 27 (código listo para leer, no solo para compilar):
  // Este método usa Optional + streams anidados + reduce para hacer algo que
  // puede describirse como "mostrar los usuarios o un aviso de vacío".
  // La implementación castiga al lector sin aportar ningún beneficio real.
  // Sin explicación oral del autor es imposible deducir su intención en segundos.
  public void printSummary(final List<UserResponse> users) {
    if (users.isEmpty()) {

      console.println(NO_USERS_FOUND_MESSAGE);

      return;
    }

    users.forEach(user ->
            console.println(
                    String.format(
                            "  %s (%s)",
                            user.name(),
                            UserStatus
                                    .fromString(user.status())
                                    .getDisplayLabel())));
  }
}