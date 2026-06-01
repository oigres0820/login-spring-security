package uts.edu.java.sistemaacademico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import uts.edu.java.sistemaacademico.model.*;
import uts.edu.java.sistemaacademico.repository.*;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private DocenteRepository docenteRepository;
    @Autowired private NotaRepository notaRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private MateriaRepository materiaRepository;

    // --------------------------------------------------------
    // MÉTODO AUXILIAR
    // --------------------------------------------------------

    private Docente getDocente(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return docenteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
    }

    // --------------------------------------------------------
    // INICIO
    // --------------------------------------------------------

    @Transactional
    @GetMapping("/inicio")
    public String inicio(Model model, Authentication authentication) {
        Docente docente = getDocente(authentication);
        model.addAttribute("usuario", docente.getUsuario());
        model.addAttribute("totalMaterias", docente.getMaterias().size());
        model.addAttribute("materias", docente.getMaterias());
        return "docente/inicio";
    }

    // --------------------------------------------------------
    // MATERIAS Y ESTUDIANTES
    // --------------------------------------------------------

    @Transactional
    @GetMapping("/materia/{idMateria}/estudiantes")
    public String verEstudiantes(@PathVariable Integer idMateria,
                                  Model model, Authentication authentication) {
        Docente docente = getDocente(authentication);
        Materia materia = materiaRepository.findById(idMateria)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        List<Estudiante> estudiantes = estudianteRepository.findAll().stream()
                .filter(e -> e.getMaterias().stream()
                        .anyMatch(m -> m.getIdMateria().equals(idMateria)))
                .toList();

        model.addAttribute("usuario", docente.getUsuario());
        model.addAttribute("materia", materia);
        model.addAttribute("estudiantes", estudiantes);
        return "docente/estudiantes";
    }

    // --------------------------------------------------------
    // NOTAS
    // --------------------------------------------------------

    @Transactional
    @GetMapping("/materia/{idMateria}/estudiante/{idEstudiante}/notas")
    public String verNotas(@PathVariable Integer idMateria,
                            @PathVariable Integer idEstudiante,
                            Model model, Authentication authentication) {
        Docente docente = getDocente(authentication);
        Materia materia = materiaRepository.findById(idMateria)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Nota corte1 = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, 1)
                .orElse(new Nota());
        Nota corte2 = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, 2)
                .orElse(new Nota());
        Nota corte3 = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, 3)
                .orElse(new Nota());

        model.addAttribute("usuario", docente.getUsuario());
        model.addAttribute("materia", materia);
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("corte1", corte1);
        model.addAttribute("corte2", corte2);
        model.addAttribute("corte3", corte3);
        model.addAttribute("docente", docente);
        return "docente/notas";
    }

    @Transactional
    @PostMapping("/materia/{idMateria}/estudiante/{idEstudiante}/notas/guardar")
    public String guardarNotas(@PathVariable Integer idMateria,
                                @PathVariable Integer idEstudiante,
                                @RequestParam Double parcial1,
                                @RequestParam Double actividades1,
                                @RequestParam Double auto1,
                                @RequestParam Double parcial2,
                                @RequestParam Double actividades2,
                                @RequestParam Double auto2,
                                @RequestParam Double parcial3,
                                @RequestParam Double actividades3,
                                @RequestParam Double auto3,
                                Authentication authentication) {
        Docente docente = getDocente(authentication);
        Materia materia = materiaRepository.findById(idMateria).orElseThrow();
        Estudiante estudiante = estudianteRepository.findById(idEstudiante).orElseThrow();

        guardarCorte(estudiante, materia, docente, 1, parcial1, actividades1, auto1);
        guardarCorte(estudiante, materia, docente, 2, parcial2, actividades2, auto2);
        guardarCorte(estudiante, materia, docente, 3, parcial3, actividades3, auto3);

        return "redirect:/docente/materia/" + idMateria + "/estudiante/" + idEstudiante + "/notas";
    }

    private void guardarCorte(Estudiante estudiante, Materia materia, Docente docente,
                               int corte, double parcial, double actividades, double auto) {
        Nota nota = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, corte)
                .orElse(new Nota());
        nota.setEstudiante(estudiante);
        nota.setMateria(materia);
        nota.setDocente(docente);
        nota.setCorte(corte);
        nota.setParcial(parcial);
        nota.setActividades(actividades);
        nota.setAutoevaluacion(auto);
        double definitiva = (parcial * 0.6) + (actividades * 0.3) + (auto * 0.1);
        nota.setDefinitiva(Math.round(definitiva * 100.0) / 100.0);
        notaRepository.save(nota);
    }
}