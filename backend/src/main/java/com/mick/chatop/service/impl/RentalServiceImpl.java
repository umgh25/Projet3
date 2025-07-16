package com.mick.chatop.service.impl;

import com.mick.chatop.dto.NewRentalDto;
import com.mick.chatop.dto.RentalDto;
import com.mick.chatop.dto.UpdateRentalDto;
import com.mick.chatop.entity.RentalEntity;
import com.mick.chatop.entity.UserEntity;
import com.mick.chatop.mapper.RentalMapper;
import com.mick.chatop.repository.RentalRepository;
import com.mick.chatop.repository.UserRepository;
import com.mick.chatop.service.RentalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service d'implémentation de la gestion des locations (rentals).
 * Cette classe permet de gérer les opérations liées aux locations,
 * Permet la création, la récupération, la mise à jour et la gestion d'images associées aux biens à louer.
 */
@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;

    @Value("${file.storage.location}")
    private String uploadDir;

    /**
     * Constructeur injectant les dépendances nécessaires.
     *
     * @param rentalRepository Repository des locations.
     * @param rentalMapper     Mapper de conversion entité/DTO.
     * @param userRepository   Repository des utilisateurs.
     */
    public RentalServiceImpl(RentalRepository rentalRepository, RentalMapper rentalMapper, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.userRepository = userRepository;
    }

    /**
     * Récupère la liste de toutes les locations.
     *
     * @return Liste de {@link RentalDto}.
     */
    @Override
    public List<RentalDto> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une location par son identifiant.
     *
     * @param id Identifiant de la location.
     * @return {@link RentalDto} si trouvé, sinon null.
     */
    @Override
    public RentalDto getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .map(rentalMapper::toDTO)
                .orElse(null);
    }

    /**
     * Crée une nouvelle location avec image (optionnelle) et utilisateur connecté.
     *
     * @param rentalDto Données de la location à créer.
     * @throws RuntimeException En cas d'erreur sur l'image ou utilisateur introuvable.
     */
    @Override
    public void createRental(NewRentalDto rentalDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Unauthenticated user"));

        RentalEntity rentalEntity = rentalMapper.toEntity(rentalDto);
        rentalEntity.setOwner(owner);

        try {
            rentalEntity.setPicture(getImageUrl(saveFile(rentalDto.picture())));
        } catch (IOException e) {
            throw new RuntimeException("There was a problem with the photo : " + e.getMessage());
        }

        LocalDateTime now = LocalDateTime.now();
        rentalEntity.setCreated_at(now);
        rentalEntity.setUpdated_at(now);

        rentalRepository.save(rentalEntity);
    }

    /**
     * Met à jour les informations d'une location existante, avec gestion facultative d'une nouvelle image.
     *
     * @param id               ID de la location à mettre à jour.
     * @param updateRentalDto  Données de mise à jour.
     * @throws NoSuchElementException Si la location n'existe pas.
     */
    @Override
    public void updateRental(Integer id, UpdateRentalDto updateRentalDto) {
        RentalEntity existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rental not found"));

        existingRental.setName(updateRentalDto.name());
        existingRental.setSurface(updateRentalDto.surface());
        existingRental.setPrice(updateRentalDto.price());
        existingRental.setDescription(updateRentalDto.description());
        existingRental.setUpdated_at(LocalDateTime.now());

        MultipartFile newPicture = updateRentalDto.picture();
        if (newPicture != null && !newPicture.isEmpty()) {
            removeOldFileIfNeeded(existingRental.getPicture());
            try {
                String savedFilename = saveFile(newPicture);
                existingRental.setPicture(getImageUrl(savedFilename));
            } catch (IOException e) {
                throw new RuntimeException("There was a problem with the new photo : " + e.getMessage());
            }
        }

        rentalRepository.save(existingRental);
    }

    /**
     * Construit une URL publique à partir du nom de fichier stocké localement.
     *
     * @param filename Nom du fichier image enregistré.
     * @return URL accessible via l'API.
     */
    private String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/rentals/image/")
                .path(filename)
                .toUriString();
    }

    /**
     * Sauvegarde un fichier image après validation du type et de l'extension.
     *
     * @param file Fichier Multipart à sauvegarder.
     * @return Nom du fichier sauvegardé.
     * @throws IOException En cas d'erreur d'accès disque.
     */
    private String saveFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.matches("(?i).+\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("File extension not allowed. Allowed: jpg, jpeg, png, gif.");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + originalName;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    /**
     * Supprime l'ancien fichier image associé à une location si présent.
     *
     * @param oldImageUrl URL publique de l'image à supprimer.
     */
    private void removeOldFileIfNeeded(String oldImageUrl) {
        if (oldImageUrl == null) return;

        String filename = Paths.get(URI.create(oldImageUrl).getPath()).getFileName().toString();
        Path fileToDelete = Paths.get(uploadDir).resolve(filename);

        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            System.err.println("Unable to delete old photo : " + e.getMessage());
        }
    }
}