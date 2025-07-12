package com.mick.chatop.dto;


public record ErrorResponse(String error, int status, String reason) {
}