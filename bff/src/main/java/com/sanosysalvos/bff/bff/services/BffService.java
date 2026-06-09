package com.sanosysalvos.bff.bff.services;


import com.sanosysalvos.bff.bff.model.PerfilResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BffService {

    @Autowired
    private RestTemplate restTemplate;

    // Lee las URLs desde application.properties
    @Value("${ms.users.url}")
    private String msUsersUrl;

    @Value("${ms.pets.url}")
    private String msPetsUrl;

<<<<<<< Updated upstream
    // -------------------------------------------------------
    // PERFIL: combina usuario + sus mascotas en una sola respuesta
    // -------------------------------------------------------
=======
    @Value("${ms.notification.url}")
    private String msNotificationUrl;

    // --- 1. PERFIL ---
>>>>>>> Stashed changes
    public PerfilResponse getPerfil(String firebaseUid) {
        // 1. Llama a ms-users para obtener el usuario
        Map<String, Object> usuario = restTemplate.getForObject(
                msUsersUrl + "/api/users/firebase/" + firebaseUid,
                Map.class
        );

        // 2. Llama a ms-pets para obtener todas las mascotas
        //    (filtramos por userId en el BFF)
        List<Map<String, Object>> todasLasMascotas = restTemplate.exchange(
                msPetsUrl + "/api/pets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();

        // 3. Filtra solo las mascotas del usuario
        String userId = usuario != null ? (String) usuario.get("id") : null;
        List<Map<String, Object>> mascotasDelUsuario = todasLasMascotas != null
                ? todasLasMascotas.stream()
                .filter(m -> userId != null && userId.equals(m.get("userId")))
                .toList()
                : List.of();

        // 4. Combina y retorna
        PerfilResponse perfil = new PerfilResponse();
        perfil.setUsuario(usuario);
        perfil.setMascotas(mascotasDelUsuario);
        return perfil;
    }

<<<<<<< Updated upstream
    // -------------------------------------------------------
    // EXPLORAR: lista todas las mascotas (para la página de explorar)
    // -------------------------------------------------------
    public List<Map<String, Object>> getMascotasParaExplorar() {
        return restTemplate.exchange(
                msPetsUrl + "/api/pets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();
    }

    // -------------------------------------------------------
    // REPORTAR: recibe datos del frontend y los envía a ms-pets
    // -------------------------------------------------------
=======
    // --- 2. EXPLORAR ---
    public List<Map<String, Object>> getMascotasParaExplorar(boolean recientes) {
        String url = msPetsUrl + "/api/pets?recientes=" + recientes;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> mascotas = response.getBody();
        return mascotas;
    }

    // --- 3. DETALLE ---
    public Map<String, Object> getMascotaPorId(String id) {
        return restTemplate.getForObject(msPetsUrl + "/api/pets/" + id, Map.class);
    }

    // --- 4. REPORTAR (CORREGIDO: CONVIERTE user_id a UUID) ---
>>>>>>> Stashed changes
    public ResponseEntity<Map> reportarMascota(Map<String, Object> datosMascota) {
        // 🔥 CONVERTIR user_id de String a UUID si existe
        if (datosMascota.containsKey("user_id")) {
            Object userIdObj = datosMascota.get("user_id");
            if (userIdObj instanceof String) {
                try {
                    UUID userId = UUID.fromString((String) userIdObj);
                    datosMascota.put("user_id", userId);
                    System.out.println("BFF: user_id convertido a UUID: " + userId);
                } catch (IllegalArgumentException e) {
                    System.err.println("BFF: user_id inválido: " + userIdObj);
                    datosMascota.remove("user_id");
                }
            }
        }
        
        // 🔥 También manejar si el campo se llama "userId" o "userUid"
        if (datosMascota.containsKey("userId") && !datosMascota.containsKey("user_id")) {
            Object userIdObj = datosMascota.get("userId");
            if (userIdObj instanceof String) {
                try {
                    UUID userId = UUID.fromString((String) userIdObj);
                    datosMascota.put("user_id", userId);
                    System.out.println("BFF: userId convertido a UUID: " + userId);
                } catch (IllegalArgumentException e) {
                    System.err.println("BFF: userId inválido: " + userIdObj);
                }
            }
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(datosMascota, headers);
        
        System.out.println("BFF: Enviando a ms-pets: " + datosMascota);
        
        return restTemplate.postForEntity(msPetsUrl + "/api/pets", request, Map.class);
    }

    // -------------------------------------------------------
    // REGISTRO: crea un nuevo usuario en ms-users
    // -------------------------------------------------------
    public ResponseEntity<Map> registrarUsuario(Map<String, Object> datosUsuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(datosUsuario, headers);
        return restTemplate.postForEntity(msUsersUrl + "/api/users", request, Map.class);
    }

    // -------------------------------------------------------
    // ACTUALIZAR PERFIL: actualiza datos del usuario en ms-users
    // -------------------------------------------------------
    public ResponseEntity<Map> actualizarUsuario(String userId, Map<String, Object> datosUsuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(datosUsuario, headers);
<<<<<<< Updated upstream
=======
        String url = msUsersUrl + "/api/users/firebase/" + userId;
        return restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
    }

    // --- 7. ELIMINAR MASCOTA ---
    public void eliminarMascota(String id) {
        restTemplate.delete(msPetsUrl + "/api/pets/" + id);
    }

    // --- 8. ACTUALIZAR MASCOTA ---
    public Map<String, Object> actualizarMascota(String id, Map<String, Object> datosMascota) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(datosMascota, headers);
>>>>>>> Stashed changes
        return restTemplate.exchange(
                msUsersUrl + "/api/users/" + userId,
                HttpMethod.PUT,
                request,
                Map.class
        );
    }
<<<<<<< Updated upstream
}
=======

    // --- 9. BUSCAR DUEÑO POR UID ---
    public Map<String, Object> getUsuarioPorId(String uid) {
        try {
            String url = msUsersUrl + "/api/users/firebase/" + uid;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            System.err.println("Error en BFF al obtener usuario: " + e.getMessage());
            return null;
        }
    }

    // --- 10. NOTIFICACIÓN ---
    public void enviarNotificacionAvistamiento(Map<String, String> payload) {
        try {
            String petId = payload.get("petId");
            String mensajeUsuario = payload.get("mensaje");

            Map<String, Object> mascota = getMascotaPorId(petId);
            if (mascota == null) {
                System.err.println("BFF: No se encontró la mascota ID: " + petId);
                return;
            }

            String ownerUid = (String) mascota.get("userUid");
            if (ownerUid == null) {
                ownerUid = (String) mascota.get("user_id");
            }
            String nombreMascota = (String) mascota.get("nombre");

            Map<String, Object> dueño;
                try {
                dueño = restTemplate.getForObject(msUsersUrl + "/api/users/" + ownerUid, Map.class);
                } catch (Exception e) {
                         System.err.println("BFF: Error al obtener dueño por UUID: " + e.getMessage());
                        dueño = null;
                }

            String emailDueño = dueño.get("email").toString();

            Map<String, String> notificationRequest = new HashMap<>();
            notificationRequest.put("email", emailDueño);
            notificationRequest.put("mascota", nombreMascota);
            notificationRequest.put("mensaje", mensajeUsuario);

            String urlFinal = msNotificationUrl + "/api/notifications/send-test";
            System.out.println("BFF: Enviando notificación a " + urlFinal + " para: " + emailDueño);

            ResponseEntity<String> response = restTemplate.postForEntity(urlFinal, notificationRequest, String.class);
            System.out.println("BFF: Respuesta ms-notification: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("BFF: Falló el flujo de notificación: " + e.getMessage());
            throw e;
        }
    }
}
>>>>>>> Stashed changes
