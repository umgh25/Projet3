package com.mick.chatop.service;

import com.mick.chatop.dto.NewRentalDto;
import com.mick.chatop.dto.RentalDto;
import com.mick.chatop.dto.UpdateRentalDto;

import java.util.List;

public interface RentalService {
    List<RentalDto> getAllRentals();
    RentalDto getRentalById(Integer id);
    void createRental(NewRentalDto rentalDto);
    void updateRental(Integer id, UpdateRentalDto updateRentalDto);
}
