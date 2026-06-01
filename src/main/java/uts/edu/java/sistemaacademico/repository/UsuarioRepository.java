package uts.edu.java.sistemaacademico.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByCedula(String cedula);

    Optional<Usuario> findByCorreo(String correo);
}