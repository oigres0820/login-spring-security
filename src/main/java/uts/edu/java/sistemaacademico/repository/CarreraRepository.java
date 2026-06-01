package uts.edu.java.sistemaacademico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Carrera;

public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
}