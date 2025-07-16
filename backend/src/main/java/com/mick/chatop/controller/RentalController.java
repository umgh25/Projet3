package com.mick.chatop.controller;

import com.mick.chatop.dto.NewRentalDto;
import com.mick.chatop.dto.RentalMessageResponse;
import com.mick.chatop.dto.RentalDto;
import com.mick.chatop.dto.UpdateRentalDto;
import com.mick.chatop.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Contrôleur REST pour la gestion des locations (rentals).
 * 
 * 
 * Fournit les opérations CRUD sur les annonces de location :
 * - Récupération de toutes les annonces
 * - Consultation d’une annonce par ID
 * - Création d’une nouvelle annonce (avec image)
 * - Récupération d’une image liée à une annonce
 * - Mise à jour d’une annonce
 * 
 * 
 * URL racine :</b> /api/rentals
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    /**
     * Constructeur injectant le service de gestion des rentals.
     * 
     * @param rentalService Service métier pour les annonces
     */
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Récupère la liste de toutes les annonces de location.
     *
     * @return Réponse contenant une map avec la clé "rentals" et une liste de RentalDto
     */
    @GetMapping
    public ResponseEntity<Map<String, List<RentalDto>>> getAllRentals() {
        Map<String, List<RentalDto>> response = new HashMap<>();
        response.put("rentals", rentalService.getAllRentals());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Récupère les détails d’une annonce de location par son ID.
     *
     * @param id Identifiant de l’annonce
     * @return L’annonce trouvée, ou un message d’erreur en cas d’échec
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Integer id) {
        try {
            RentalDto rental = rentalService.getRentalById(id);
            if (rental == null) {
                throw new RuntimeException("Rental introuvable");
            }
            return ResponseEntity.ok(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    /**
     * Crée une nouvelle annonce de location, avec envoi d’image via Multipart/form-data.
     *
     * @param rentalDto DTO contenant les champs de l’annonce à créer
     * @return Message de succès ou message d’erreur en cas d’échec
     */
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createRental(@ModelAttribute @Valid NewRentalDto rentalDto) {
        try {
            rentalService.createRental(rentalDto);
            return new ResponseEntity<>(new RentalMessageResponse("Rental created !"), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Récupère une image liée à une annonce de location.
     *
     * @param filename Nom du fichier image à récupérer
     * @return L’image sous forme de tableau d’octets, ou une erreur 404 si introuvable
     */
    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody ResponseEntity<byte[]> getRentalImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.notFound().build();
            }
            byte[] imageBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Met à jour les informations d’une annonce existante.
     *
     * @param id Identifiant de l’annonce à mettre à jour
     * @param updateRentalDto DTO contenant les nouvelles valeurs
     * @return Message de succès, ou erreur si l’annonce est introuvable ou si une exception survient
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRental(
            @PathVariable Integer id,
            @ModelAttribute @Valid UpdateRentalDto updateRentalDto) {
        try {
            rentalService.updateRental(id, updateRentalDto);
            return ResponseEntity.ok(new RentalMessageResponse("Rental updated !"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erreur: " + e.getMessage());
        }
    }
}