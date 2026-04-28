package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import lombok.Value;

// Clean Code - Regla 15 (Inmutabilidad como preferencia de diseño):
// Se utiliza @Data, lo que genera setters públicos y permite modificar el estado
// del objeto desde cualquier parte del sistema, rompiendo el encapsulamiento.
// La regla dice: los objetos de dominio deben ser inmutables para proteger sus invariantes
// y evitar cambios de estado no controlados.
// Solución: reemplazar @Data por @Value o definir campos final sin setters,
// asegurando que cualquier cambio de estado se realice mediante métodos del dominio.
@Value
public class UserModel {

  UserId id;
  UserName name;
  UserEmail email;
  UserPassword password;
  UserRole role;
  UserStatus status;

  public static UserModel create(
      final UserId id,
      final UserName name,
      final UserEmail email,
      final UserPassword password,
      final UserRole role) {
    return new UserModel(id, name, email, password, role, UserStatus.PENDING);
  }

  public UserModel activate() {
    return new UserModel(id, name, email, password, role, UserStatus.ACTIVE);
  }

  public UserModel deactivate() {
    return new UserModel(id, name, email, password, role, UserStatus.INACTIVE);
  }

  // Clean Code - Regla 9 (Arquitectura Hexagonal - Dependencias hacia el dominio):
  // El modelo de dominio importa y utiliza una clase de infraestructura (UserEntity),
  // además de contener lógica de conversión hacia dicha entidad.
  // Esto acopla el dominio a la capa de persistencia y rompe la independencia del core.
  // La regla dice: las dependencias siempre deben apuntar hacia el dominio; el dominio
  // no debe conocer ni depender de detalles de infraestructura.
  // Solución: eliminar el import de UserEntity y el método toEntity() del dominio.
  // La conversión debe realizarse en la capa de infraestructura mediante un mapper.
}
