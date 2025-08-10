package com.MajorProject.Service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(String toUserType, Long userId, String message) {
        // This could be storing in DB, pushing to WebSocket, etc.
        System.out.println("Notify " + toUserType + " with ID " + userId + ": " + message);
    }
}

