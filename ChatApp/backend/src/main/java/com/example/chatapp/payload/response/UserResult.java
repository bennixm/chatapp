package com.example.chatapp.payload.response;

public sealed interface UserResult permits UserResult.Success, UserResult.Failure {

    public record Success(String username, Long userId) implements UserResult {}

    public record Failure(String message) implements UserResult {}
}
