package uts.edu.java.sistemaacademico.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "docentes")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocente;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "docente_materia",
        joinColumns = @JoinColumn(name = "idDocente"),
        inverseJoinColumns = @JoinColumn(name = "idMateria")
    )
    private List<Materia> materias;

    @OneToMany(mappedBy = "docente", fetch = FetchType.EAGER)
    private List<Nota> notas;

    public Docente() {}

    public Integer getIdDocente() { return idDocente; }
    public void setIdDocente(Integer idDocente) { this.idDocente = idDocente; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Materia> getMaterias() { return materias; }
    public void setMaterias(List<Materia> materias) { this.materias = materias; }

    public List<Nota> getNotas() { return notas; }
    public void setNotas(List<Nota> notas) { this.notas = notas; }
}