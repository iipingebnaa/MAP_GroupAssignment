import messaging from '@react-native-firebase/messaging';
import * as Notifications from 'expo-notifications';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Platform } from 'react-native';

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: true,
    shouldSetBadge: false,
  }),
});

class NotificationService {
  constructor() {
    this.fcmToken = null;
    this.notificationListener = null;
    this.responseListener = null;
  }

  async initialize() {
    await this.requestPermissions();
    await this.getFCMToken();
    this.setupListeners();
    this.setupBackgroundHandler();
  }

  async requestPermissions() {
    try {
      if (Platform.OS === 'android') {
        await Notifications.setNotificationChannelAsync('default', {
          name: 'default',
          importance: Notifications.AndroidImportance.MAX,
          vibrationPattern: [0, 250, 250, 250],
          lightColor: '#FF231F7C',
        });

        // Hockey-specific channels
        await Notifications.setNotificationChannelAsync('match_updates', {
          name: 'Match Updates',
          description: 'Live match scores and updates',
          importance: Notifications.AndroidImportance.HIGH,
          vibrationPattern: [0, 250, 250, 250],
          sound: 'default',
        });

        await Notifications.setNotificationChannelAsync('team_chat', {
          name: 'Team Chat',
          description: 'Team chat messages',
          importance: Notifications.AndroidImportance.DEFAULT,
          sound: 'default',
        });

        await Notifications.setNotificationChannelAsync('announcements', {
          name: 'Announcements',
          description: 'Important announcements',
          importance: Notifications.AndroidImportance.HIGH,
          sound: 'default',
        });
      }

      const authStatus = await messaging().requestPermission();
      const enabled =
        authStatus === messaging.AuthorizationStatus.AUTHORIZED ||
        authStatus === messaging.AuthorizationStatus.PROVISIONAL;

      if (!enabled) {
        console.log('Permission not granted');
        return false;
      }

      const { status: existingStatus } = await Notifications.getPermissionsAsync();
      let finalStatus = existingStatus;
      
      if (existingStatus !== 'granted') {
        const { status } = await Notifications.requestPermissionsAsync();
        finalStatus = status;
      }

      return finalStatus === 'granted';
    } catch (error) {
      console.error('Error requesting permissions:', error);
      return false;
    }
  }

  async getFCMToken() {
    try {
      const token = await messaging().getToken();
      this.fcmToken = token;
      await AsyncStorage.setItem('fcmToken', token);
      return token;
    } catch (error) {
      console.error('Error getting FCM token:', error);
      return null;
    }
  }

  setupListeners() {
    // Foreground message handler
    this.messageListener = messaging().onMessage(async remoteMessage => {
      console.log('Foreground message:', remoteMessage);
      await this.showLocalNotification(remoteMessage);
    });

    // Background message handler
    messaging().onNotificationOpenedApp(remoteMessage => {
      console.log('Notification caused app to open from background:', remoteMessage);
      this.handleNotificationPress(remoteMessage);
    });

    // Killed app handler
    messaging()
      .getInitialNotification()
      .then(remoteMessage => {
        if (remoteMessage) {
          console.log('Notification caused app to open from quit state:', remoteMessage);
          this.handleNotificationPress(remoteMessage);
        }
      });

    // Local notification listeners
    this.notificationListener = Notifications.addNotificationReceivedListener(notification => {
      console.log('Local notification received:', notification);
    });

    this.responseListener = Notifications.addNotificationResponseReceivedListener(response => {
      console.log('Notification response:', response);
      this.handleLocalNotificationPress(response);
    });
  }

  setupBackgroundHandler() {
    messaging().setBackgroundMessageHandler(async remoteMessage => {
      console.log('Background message:', remoteMessage);
      // Handle background messages here
    });
  }

  async showLocalNotification(remoteMessage) {
    const { notification, data } = remoteMessage;
    
    let channelId = 'default';
    if (data?.type === 'match_update') channelId = 'match_updates';
    else if (data?.type === 'team_chat') channelId = 'team_chat';
    else if (data?.type === 'announcement') channelId = 'announcements';

    await Notifications.scheduleNotificationAsync({
      content: {
        title: notification?.title || 'Hockey Update',
        body: notification?.body || 'You have a new update',
        data: data || {},
        sound: 'default',
        categoryIdentifier: channelId,
      },
      trigger: null,
    });
  }

  handleNotificationPress(remoteMessage) {
    const { data } = remoteMessage;
    // Navigate based on notification type
    this.navigateBasedOnNotification(data);
  }

  handleLocalNotificationPress(response) {
    const { data } = response.notification.request.content;
    this.navigateBasedOnNotification(data);
  }

  navigateBasedOnNotification(data) {
    if (!data) return;

    // You'll need to import your navigation service here
    // Example navigation logic:
    switch (data.type) {
      case 'match_update':
        // NavigationService.navigate('MatchDetails', { matchId: data.matchId });
        break;
      case 'team_chat':
        // NavigationService.navigate('TeamChat', { teamId: data.teamId });
        break;
      case 'announcement':
        // NavigationService.navigate('Announcements');
        break;
      default:
        // NavigationService.navigate('Home');
        break;
    }
  }

  // Send specific notification types
  async sendMatchUpdateNotification(matchId, message, score) {
    await this.showLocalNotification({
      notification: {
        title: 'Match Update',
        body: `${message} | Score: ${score}`,
      },
      data: {
        type: 'match_update',
        matchId,
      },
    });
  }

  async sendTeamChatNotification(teamId, senderName, message) {
    await this.showLocalNotification({
      notification: {
        title: `${senderName} in team chat`,
        body: message,
      },
      data: {
        type: 'team_chat',
        teamId,
      },
    });
  }

  async sendAnnouncementNotification(title, message) {
    await this.showLocalNotification({
      notification: {
        title,
        body: message,
      },
      data: {
        type: 'announcement',
      },
    });
  }

  cleanup() {
    if (this.messageListener) {
      this.messageListener();
    }
    if (this.notificationListener) {
      Notifications.removeNotificationSubscription(this.notificationListener);
    }
    if (this.responseListener) {
      Notifications.removeNotificationSubscription(this.responseListener);
    }
  }
}
