import React from 'react';

interface NotificationProps {
    notification: { message: string, type: 'success' | 'error', visible: boolean };
}

const Notification: React.FC<NotificationProps> = ({ notification }) => {
    return (
        <div className={`notification ${notification.type} ${!notification.visible ? 'hidden' : ''}`}>
            {notification.message}
        </div>
    );
}

export default Notification;