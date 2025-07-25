package com.example.foroom.domain.model

data class Chat(
    val name: String,
    val emojiUrl: String,
    val creatorUsername: String,
    val likeCount: Int,
    val id: Int,
    val isFavorite: Boolean,
    val createdByCurrentUser: Boolean
)
