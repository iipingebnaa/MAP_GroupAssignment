import database from '@react-native-firebase/database';
import messaging from '@react-native-firebase/messaging';
import AsyncStorage from '@react-native-async-storage/async-storage';

class RealtimeService {
  constructor() {
    this.listeners = new Map();
    this.messageHandlers = new Map();
  }

  // ========================
  // LIVE MATCH UPDATES
  // ========================

  subscribeToMatchUpdates(matchId, callback) {
    const reference = database().ref(`/matches/${matchId}/updates`);
    
    const listener = reference.on('value', snapshot => {
      const data = snapshot.val();
      const updates = data ? Object.values(data).sort((a, b) => a.timestamp - b.timestamp) : [];
      callback(updates);
    });

    this.listeners.set(`match_${matchId}`, { reference, listener });
    
    return () => {
      reference.off('value', listener);
      this.listeners.delete(`match_${matchId}`);
    };
  }

  async addMatchUpdate(matchId, update) {
    try {
      const newUpdateRef = database().ref(`/matches/${matchId}/updates`).push();
      await newUpdateRef.set({
        id: newUpdateRef.key,
        ...update,
        timestamp: database.ServerValue.TIMESTAMP
      });
      return newUpdateRef.key;
    } catch (error) {
      console.error('Error adding match update:', error);
      throw error;
    }
  }

  async updateMatchScore(matchId, homeScore, awayScore) {
    try {
      await database().ref(`/matches/${matchId}/score`).set({
        home: homeScore,
        away: awayScore,
        lastUpdated: database.ServerValue.TIMESTAMP
      });
    } catch (error) {
      console.error('Error updating match score:', error);
      throw error;
    }
  }

  subscribeToMatchScore(matchId, callback) {
    const reference = database().ref(`/matches/${matchId}/score`);
    
    const listener = reference.on('value', snapshot => {
      const score = snapshot.val();
      callback(score);
    });

    this.listeners.set(`score_${matchId}`, { reference, listener });
    
    return () => {
      reference.off('value', listener);
      this.listeners.delete(`score_${matchId}`);
    };
  }

  // ========================
  // REAL-TIME CHAT
  // ========================

  subscribeToTeamChat(teamId, callback) {
    const reference = database().ref(`/chats/teams/${teamId}/messages`);
    
    const listener = reference.on('value', snapshot => {
      const data = snapshot.val();
      const messages = data ? Object.values(data).sort((a, b) => a.timestamp - b.timestamp) : [];
      callback(messages);
    });

    this.listeners.set(`chat_${teamId}`, { reference, listener });
    
    return () => {
      reference.off('value', listener);
      this.listeners.delete(`chat_${teamId}`);
    };
  }

  async sendChatMessage(teamId, message, userId, userName, userAvatar = null) {
    try {
      const newMessageRef = database().ref(`/chats/teams/${teamId}/messages`).push();
      await newMessageRef.set({
        id: newMessageRef.key,
        text: message,
        userId,
        userName,
        userAvatar,
        timestamp: database.ServerValue.TIMESTAMP,
        type: 'text'
      });
      
      // Update last message info for the chat
      await database().ref(`/chats/teams/${teamId}/lastMessage`).set({
        text: message,
        userName,
        timestamp: database.ServerValue.TIMESTAMP
      });
      
      return newMessageRef.key;
    } catch (error) {
      console.error('Error sending chat message:', error);
      throw error;
    }
  }

  async sendImageMessage(teamId, imageUrl, userId, userName, userAvatar = null) {
    try {
      const newMessageRef = database().ref(`/chats/teams/${teamId}/messages`).push();
      await newMessageRef.set({
        id: newMessageRef.key,
        imageUrl,
        userId,
        userName,
        userAvatar,
        timestamp: database.ServerValue.TIMESTAMP,
        type: 'image'
      });
      return newMessageRef.key;
    } catch (error) {
      console.error('Error sending image message:', error);
      throw error;
    }
  }

  // ========================
  // LIVE EVENT FEED
  // ========================

  subscribeToEventFeed(callback) {
    const reference = database().ref('/events/feed');
    
    const listener = reference.limitToLast(50).on('value', snapshot => {
      const data = snapshot.val();
      const events = data ? Object.values(data).sort((a, b) => b.timestamp - a.timestamp) : [];
      callback(events);
    });

    this.listeners.set('event_feed', { reference, listener });
    
    return () => {
      reference.off('value', listener);
      this.listeners.delete('event_feed');
    };
  }

  async addEventFeedItem(item) {
    try {
      const newItemRef = database().ref('/events/feed').push();
      await newItemRef.set({
        id: newItemRef.key,
        ...item,
        timestamp: database.ServerValue.TIMESTAMP
      });
      return newItemRef.key;
    } catch (error) {
      console.error('Error adding event feed item:', error);
      throw error;
    }
  }

  // ========================
  // USER PRESENCE
  // ========================

  async setUserOnline(userId, userData) {
    try {
      const userRef = database().ref(`/presence/${userId}`);
      await userRef.set({
        ...userData,
        online: true,
        lastSeen: database.ServerValue.TIMESTAMP
      });
      
      // Set up offline detection
      userRef.onDisconnect().update({
        online: false,
        lastSeen: database.ServerValue.TIMESTAMP
      });
    } catch (error) {
      console.error('Error setting user online:', error);
    }
  }

  async setUserOffline(userId) {
    try {
      await database().ref(`/presence/${userId}`).update({
        online: false,
        lastSeen: database.ServerValue.TIMESTAMP
      });
    } catch (error) {
      console.error('Error setting user offline:', error);
    }
  }

  subscribeToUserPresence(userIds, callback) {
    const reference = database().ref('/presence');
    
    const listener = reference.on('value', snapshot => {
      const presence = snapshot.val() || {};
      const filteredPresence = {};
      
      userIds.forEach(userId => {
        if (presence[userId]) {
          filteredPresence[userId] = presence[userId];
        }
      });
      
      callback(filteredPresence);
    });

    this.listeners.set('user_presence', { reference, listener });
    
    return () => {
      reference.off('value', listener);
      this.listeners.delete('user_presence');
    };
  }

  // ========================
  // ANNOUNCEMENTS
  // ========================

  subscribeToAnnouncements(callback) {
    const reference = database().ref('/announcements');
    
    const listener = reference.orderByChild('timestamp').limitToLast(20).on('value', snapshot => {
      const data = snapshot.val();
      const announcements = data ? Object.values(data).sort((a, b) => b.timestamp - a.timestamp) : [];
      callback(announcements);
    });

    this.listeners.set('announcements', { reference, listener });
    
    return () => {
      reference.off('value', listener);
      this.listeners.delete('announcements');
    };
  }

  async addAnnouncement(announcement) {
    try {
      const newAnnouncementRef = database().ref('/announcements').push();
      await newAnnouncementRef.set({
        id: newAnnouncementRef.key,
        ...announcement,
        timestamp: database.ServerValue.TIMESTAMP
      });
      return newAnnouncementRef.key;
    } catch (error) {
      console.error('Error adding announcement:', error);
      throw error;
    }
  }

  // ========================
  // CLEANUP
  // ========================

  unsubscribeAll() {
    this.listeners.forEach(({ reference, listener }) => {
      reference.off('value', listener);
    });
    this.listeners.clear();
  }

  unsubscribe(key) {
    const listenerData = this.listeners.get(key);
    if (listenerData) {
      const { reference, listener } = listenerData;
      reference.off('value', listener);
      this.listeners.delete(key);
    }
  }
}

export default new RealtimeService();
