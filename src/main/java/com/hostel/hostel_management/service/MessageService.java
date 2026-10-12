package com.hostel.hostel_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostel.hostel_management.entity.Message;
import com.hostel.hostel_management.entity.Role;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.MessageRepository;
import com.hostel.hostel_management.repository.UserRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(
            MessageRepository messageRepository,
            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Message sendMessage(
            Long senderId,
            Long receiverId,
            String content) {

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty.");
        }

        if (content.trim().length() > 2000) {
            throw new IllegalArgumentException(
                    "Message cannot exceed 2000 characters.");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException(
                    "You cannot send a message to yourself.");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sender not found."));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Receiver not found."));

        boolean senderIsWarden =
                sender.getRole() == Role.WARDEN
                || sender.getRole() == Role.ADMIN;

        boolean receiverIsWarden =
                receiver.getRole() == Role.WARDEN
                || receiver.getRole() == Role.ADMIN;

        boolean senderIsHosteller =
        sender.getRole() == Role.HOSTELER;

boolean receiverIsHosteller =
        receiver.getRole() == Role.HOSTELER;

        if (!(senderIsWarden && receiverIsHosteller)
                && !(senderIsHosteller && receiverIsWarden)) {
            throw new IllegalArgumentException(
                    "Messages are allowed only between "
                    + "hostellers and wardens.");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content.trim());

        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getConversation(Long userId) {
        return messageRepository
                .findBySender_IdOrReceiver_IdOrderByCreatedAtAsc(
                        userId, userId);
    }

    @Transactional(readOnly = true)
    public List<Message> getInbox(Long userId) {
        return messageRepository
                .findByReceiver_IdOrderByCreatedAtAsc(userId);
    }
}