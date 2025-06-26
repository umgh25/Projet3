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

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RentalDto>>> getAllRentals() {
        Map<String, List<RentalDto>> response = new HashMap<>();
        response.put("rentals", rentalService.getAllRentals());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Integer id) {
        RentalDto rentalDto = rentalService.getRentalById(id);
        return rentalDto != null ? ResponseEntity.ok(rentalDto) : ResponseEntity.notFound().build();
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createRental(@ModelAttribute @Valid NewRentalDto rentalDto) {
        try {
            rentalService.createRental(rentalDto);
            return new ResponseEntity<>(new RentalMessageResponse("Rental created !"), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }



}
