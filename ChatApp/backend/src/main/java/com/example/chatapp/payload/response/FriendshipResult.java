package com.example.chatapp.payload.response;

public sealed interface FriendshipResult permits FriendshipResult.Success, FriendshipResult.Failure {

    public record Success(String message) implements FriendshipResult {}

    public record Failure(String errorMessage) implements FriendshipResult {}
}
