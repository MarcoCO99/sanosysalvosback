package com.sanosysalvos.bff.bff.controller;


import com.sanosysalvos.bff.bff.model.PerfilResponse;
import com.sanosysalvos.bff.bff.services.BffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
<<<<<<< Updated upstream
@CrossOrigin(origins = "*")
=======
@CrossOrigin(
    origins = "https://localhost:3000",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
               RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true"  // ← si usas cookies/sessions
)
>>>>>>> Stashed changes
public class BffController {
    @Autowired
    private BffService bffService;

    // GET /API/PERFIL
    @GetMapping("/perfil/{firebaseUid}")
    public ResponseEntity<PerfilResponse> getPerfil(@PathVariable String firebaseUid) {
        PerfilResponse perfil = bffService.getPerfil(firebaseUid);
        return ResponseEntity.ok(perfil);
    }
<<<<<<< Updated upstream

    // GET /API/EXPLORAR
    @GetMapping("/explorar")
    public ResponseEntity<List<Map<String, Object>>> explorar() {
        List<Map<String, Object>> mascotas = bffService.getMascotasParaExplorar();
        return ResponseEntity.ok(mascotas);
=======
@GetMapping("/usuarios/firebase/{firebaseUid}")
public ResponseEntity<Map<String, Object>> getDatosDueño(@PathVariable String firebaseUid) {
    Map<String, Object> usuario = bffService.getUsuarioPorId(firebaseUid);
    if (usuario == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(usuario);
}

    @PostMapping("/registro") // Ruta que usa tu auth de Next.js
    public ResponseEntity<Map> registrarUsuario(@RequestBody Map<String, Object> datosUsuario) {
        return bffService.registrarUsuario(datosUsuario);
    }

    @PostMapping("/users") // Ruta de respaldo
    public ResponseEntity<Map> registrar(@RequestBody Map<String, Object> datos) {
        return bffService.registrarUsuario(datos);
    }

    @PutMapping("/usuarios/{userId}")
    public ResponseEntity<Map> actualizarUsuario(@PathVariable String userId, @RequestBody Map<String, Object> datosUsuario) {
        return bffService.actualizarUsuario(userId, datosUsuario);
    }

    // --- MASCOTAS ---

 @GetMapping("/explorar")
public ResponseEntity<List<Map<String, Object>>> explorar(
    @RequestParam(value = "recientes", required = false, defaultValue = "false") boolean recientes
) {
    // Le pasamos el booleano al método del servicio
    return ResponseEntity.ok(bffService.getMascotasParaExplorar(recientes));
}

    @GetMapping("/mascota/{id}")
    public ResponseEntity<Map<String, Object>> getMascotaPorId(@PathVariable String id) {
        try {
            Map<String, Object> mascota = bffService.getMascotaPorId(id);
            return ResponseEntity.ok(mascota);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
>>>>>>> Stashed changes
    }

    // POST /API/REPORTAR
    @PostMapping("/reportar")
    public ResponseEntity<Map> reportarMascota(@RequestBody Map<String, Object> datosMascota) {
        return bffService.reportarMascota(datosMascota);
    }

    // POST API/REGISTRO
    @PostMapping("/registro")
    public ResponseEntity<Map> registrarUsuario(@RequestBody Map<String, Object> datosUsuario) {
        return bffService.registrarUsuario(datosUsuario);
    }

    // PUT /API/USUARIOS/USUARIOID
    @PutMapping("/usuarios/{userId}")
    public ResponseEntity<Map> actualizarUsuario(
            @PathVariable String userId,
            @RequestBody Map<String, Object> datosUsuario) {
        return bffService.actualizarUsuario(userId, datosUsuario);
    }
}
