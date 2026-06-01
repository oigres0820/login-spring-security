package uts.edu.java.sistemaacademico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uts.edu.java.sistemaacademico.model.Semestre;

public interface SemestreRepository extends JpaRepository<Semestre, Integer> {
}