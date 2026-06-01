package uts.edu.java.sistemaacademico.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstudiante;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCarrera")
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSemestre")
    private Semestre semestre;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "estudiante_materia",
        joinColumns = @JoinColumn(name = "idEstudiante"),
        inverseJoinColumns = @JoinColumn(name = "idMateria")
    )
    private List<Materia> materias;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.EAGER)
    private List<Nota> notas;

    public Estudiante() {}

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }

    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }

    public List<Materia> getMaterias() { return materias; }
    public void setMaterias(List<Materia> materias) { this.materias = materias; }

    public List<Nota> getNotas() { return notas; }
    public void setNotas(List<Nota> notas) { this.notas = notas; }
}