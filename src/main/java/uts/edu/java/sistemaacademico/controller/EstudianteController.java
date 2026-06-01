package uts.edu.java.sistemaacademico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import uts.edu.java.sistemaacademico.model.*;
import uts.edu.java.sistemaacademico.repository.*;

import java.util.List;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private NotaRepository notaRepository;

    // --------------------------------------------------------
    // MÉTODO AUXILIAR
    // --------------------------------------------------------

    private Estudiante getEstudiante(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return estudianteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
    }

    // --------------------------------------------------------
    // INICIO
    // --------------------------------------------------------

    @Transactional
    @GetMapping("/inicio")
    public String inicio(Model model, Authentication authentication) {
        Estudiante estudiante = getEstudiante(authentication);

        List<Nota> todasLasNotas = notaRepository.findByEstudiante(estudiante);

        double promedioGeneral = 0;
        if (!todasLasNotas.isEmpty()) {
            double suma = todasLasNotas.stream()
                    .mapToDouble(Nota::getDefinitiva)
                    .sum();
            promedioGeneral = Math.round((suma / todasLasNotas.size()) * 100.0) / 100.0;
        }

        model.addAttribute("usuario", estudiante.getUsuario());
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("promedioGeneral", promedioGeneral);
        model.addAttribute("totalMaterias", estudiante.getMaterias().size());
        return "estudiante/inicio";
    }

    // --------------------------------------------------------
    // MATERIAS Y NOTAS
    // --------------------------------------------------------

    @Transactional
    @GetMapping("/materias")
    public String verMaterias(Model model, Authentication authentication) {
        Estudiante estudiante = getEstudiante(authentication);
        List<Materia> materias = estudiante.getMaterias();
        model.addAttribute("usuario", estudiante.getUsuario());
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("materias", materias);
        return "estudiante/materias";
    }

    @Transactional
    @GetMapping("/materia/{idMateria}/notas")
    public String verNotas(@PathVariable Integer idMateria,
                            Model model, Authentication authentication) {
        Estudiante estudiante = getEstudiante(authentication);

        Materia materia = estudiante.getMaterias().stream()
                .filter(m -> m.getIdMateria().equals(idMateria))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        Nota corte1 = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, 1)
                .orElse(new Nota());
        Nota corte2 = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, 2)
                .orElse(new Nota());
        Nota corte3 = notaRepository.findByEstudianteAndMateriaAndCorte(estudiante, materia, 3)
                .orElse(new Nota());

        double def1 = corte1.getDefinitiva() != null ? corte1.getDefinitiva() : 0;
        double def2 = corte2.getDefinitiva() != null ? corte2.getDefinitiva() : 0;
        double def3 = corte3.getDefinitiva() != null ? corte3.getDefinitiva() : 0;
        double definitivaFinal = Math.round(((def1 * 0.33) + (def2 * 0.33) + (def3 * 0.34)) * 100.0) / 100.0;

        model.addAttribute("usuario", estudiante.getUsuario());
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("materia", materia);
        model.addAttribute("corte1", corte1);
        model.addAttribute("corte2", corte2);
        model.addAttribute("corte3", corte3);
        model.addAttribute("definitivaFinal", definitivaFinal);
        return "estudiante/notas";
    }
}