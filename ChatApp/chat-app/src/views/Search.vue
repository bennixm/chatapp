<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Find people</span>
    </div>
    <div class="content-page">
      <div class="search-section">
        <div class="search-section-content" v-if="users.length">
          <div class="display-user-item" v-for="user in users" :key="user.userid">
            <div class="user-item-identity"></div>
            <div class="user-item-content">
              <span class="username">{{ user.username }}</span>
              <span class="email">{{ user.email }}</span>
            </div>
            <div class="user-item-buttons">
              <button
                  v-if="!isFriend(user.userid)"
                  @click="sendFriendRequest(user.userid)">
                <i class="fa fa-user-plus" aria-hidden="true"></i> Add Friend
              </button>
              <button
                  v-if="isFriend(user.userid)"
                  @click="startChat(user.userid)">
                <i class="fa fa-comments" aria-hidden="true"></i> Chat
              </button>
            </div>
          </div>
        </div>
        <div class="search-section-content" v-else>
          <span class="feedback-request">No users found.</span>
        </div>
        <div class="search-nav"></div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
import Swal from "sweetalert2";

export default {
  name: "SearchPage",
  data() {
    return {
      users: [],
      allUsers: [],
      friends: [],
    };
  },
  computed: {
    ...mapGetters(["getUserId"]),
  },
  created() {
    this.fetchUsersAndFriends();
  },
  methods: {
    async fetchUsersAndFriends() {
      try {
        const allUsersResponse = await axios.get(
            "http://localhost:8085/api/v1/user/allusers"
        );
        this.allUsers = allUsersResponse.data;

        await this.getUserFriends(this.getUserId);

        this.users = this.allUsers.filter(
            (user) => !this.isFriend(user.userid)
        );
      } catch (error) {
        console.error("There was an error fetching the users and friends:", error);
      }
    },

    async getUserFriends(userId) {
      try {
        const response = await axios.get("http://localhost:8085/api/friendship/getfriends", {
          params: { userId: userId },
        });
        this.friends = response.data;
      } catch (error) {
        console.error("Error fetching friends:", error);
      }
    },

    isFriend(userid) {
      return this.friends.some((friend) => friend.userid === userid);
    },

    async sendFriendRequest(receiverId) {
      try {
        const senderId = parseInt(this.getUserId);
        const response = await axios.post(
            'http://localhost:8085/api/friendship/send',
            {
              senderUserid: senderId,
              receiverUserid: receiverId,
            }
        );

        Swal.fire({
          icon: 'success',
          title: 'Friend Request Sent!',
          text: response.data.message,
        });

        this.fetchUsersAndFriends();
      } catch (error) {
        console.error("Error sending friend request:", error);

        Swal.fire({
          icon: 'error',
          title: 'Error!',
          text: 'Error sending friend request.',
        });
      }
    },

    startChat(userid) {
      this.$router.push({ name: "Chat", params: { userId: userid } });
    },
  },
};
</script>

<style scoped>
/* Add your styles here */
</style>
