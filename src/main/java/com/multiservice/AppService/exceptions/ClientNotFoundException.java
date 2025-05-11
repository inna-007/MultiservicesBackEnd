package com.multiservice.AppService.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String username) {
        super("Client introuvable pour l'utilisateur : " + username);
    }}