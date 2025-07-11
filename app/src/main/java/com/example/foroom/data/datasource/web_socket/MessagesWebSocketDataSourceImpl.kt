package com.example.foroom.data.datasource.web_socket

import com.example.foroom.data.model.MessageEntity
import com.example.foroom.data.model.request.SendMessageRequestEntity
import com.example.network.web_socket.ForoomWebSocketClient
import com.example.shared.model.Result
import kotlinx.coroutines.flow.Flow

class MessagesWebSocketDataSourceImpl(private val client: ForoomWebSocketClient) : MessagesWebSocketDataSource {

    override fun connect(): Flow<Result<Unit>> = client.connect()

    override fun disconnect() = client.disconnect()

    override fun onMessageReceived(): Flow<MessageEntity> =
        client.onReceived(MessageEntity::class.java)

    override fun sendMessage(sendMessageRequest: SendMessageRequestEntity): Flow<Result<Unit>> =
        client.sendMessage(sendMessageRequest)

    override fun joinGroup(groupName: String): Flow<Result<Unit>> = client.joinGroup(groupName)

    override fun leaveGroup(groupName: String): Flow<Result<Unit>> = client.leaveGroup(groupName)
}

