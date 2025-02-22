import { reactive } from 'vue';

export const eventBus = reactive({
    fetchFriendsEvent: null,
    fetchFriendRequestsEvent: null,
});
