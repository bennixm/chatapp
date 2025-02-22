<template>
  <header class="main-navbar-element">
    <div class="mini-navbar-element" v-if="username">
      <div class="mini-nav-item"><router-link to="/find-people"><i class="fa fa-search" aria-hidden="true"></i></router-link></div>
      <div class="mini-nav-item"><router-link to="/new-chat"><i class="fa fa-users" aria-hidden="true"></i></router-link></div>
      <div class="mini-nav-item"><router-link to="/stats"><i class="fa fa-line-chart" aria-hidden="true"></i></router-link></div>
      <div class="mini-nav-item"><router-link to="/settings"><i class="fa fa-cog" aria-hidden="true"></i></router-link></div>
    </div>
    <div class="navbar-element">
      <div :class="['nav-header', 'user-login', { 'header-borderless': !username }]">
        <div class="user-img"></div>


        <router-link v-if="!username" to="/login">
          <div class="user-auth">Login</div>
        </router-link>
        <div class="header-users-notify-name" v-else>
          <div class="user-auth">{{ username }}</div>
          <button
              class="notification-button"
              :class="{'has-requests': friendRequests.length > 0}"
              @click="showFriendRequestsPopup"
              :disabled="friendRequests.length === 0">
            <span v-if="friendRequests.length > 0" class="nr-notifications">
                 {{ friendRequests.length }}
            </span>
            <i class="fa fa-bell" aria-hidden="true"></i>
          </button>
        </div>
      </div>
      <nav class="navbar" v-if="username">
        <div class="navbar-head-title"><span>Chats</span></div>
        <div class="chat-items custom-scrollbar">
          <router-link v-if="friends.length === 0" to="/new-chat">
            <div class="add-chat-item">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </div>
          </router-link>
          <router-link
              v-for="(friend, index) in friends"
              :key="index"
              :to="`/chat/${friend.userid}`">
            <div class="chat-item">
              <div class="chat-item-identity"></div>
              <div class="chat-item-content">
                <div class="user-name">{{ friend.username }}</div>
                <div class="user-chat">.....</div>
              </div>
            </div>
          </router-link>
        </div><div class="navbar-head-title"><span>AI</span></div>
        <div class="chat-items custom-scrollbar">
          <div class="chat-item ai-nav">
            <div class="chat-item-identity"></div>
            <div class="chat-item-content">
              <div class="user-name">AI</div>
              <div class="user-chat">How are you?</div>
            </div>
          </div>
        </div>
      </nav>
      <div class="user-logout" v-if="username">
        <button @click="logout" class="logout-btn">Logout</button>
      </div>
    </div>
  </header>
  <div v-if="isModalVisible" class="modal-overlay">
    <div class="modal">
      <div class="panel-page">
        <div class="header-page">
          <span class="title-page">Friend Requests</span>
        </div>
        <div class="content-page">
          <div class="search-section" v-if="friendRequests.length > 0">
            <div class="notification-section-content">
              <div class="display-user-item" v-for="request in friendRequests" :key="request.friendshipId">
                <div class="user-item-identity"></div>
                <div class="user-item-content">
                  <span class="username">{{ request.user.username }}</span>
                  <span class="email">sent you a friend request</span>
                </div>
                <div class="user-item-buttons">
                  <button class="send-request-button" @click="updateRequestStatus(request.friendshipId, true)"><i class="fa fa-user-plus" aria-hidden="true"></i> Accept</button>
                  <button class="send-request-button" @click="updateRequestStatus(request.friendshipId, false)"><i class="fa fa-user-times" aria-hidden="true"></i> Decline</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button @click="closeModal" class="cancel-button">Close</button>
        <button @click="acceptAllRequests" class="accept-button">Accept All</button>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import { mapGetters } from "vuex";
import { useRouter } from 'vue-router';
import axios from "axios";
import Swal from "sweetalert2";
import { eventBus } from '../events/eventBus';

export default {
  name: "NavbarComponent",
  data() {
    return {
      friends: [],
      friendRequests: [],
      isModalVisible: false,
    };
  },
  computed: {
    ...mapGetters(["getUserId"]),
  },
  created() {
    this.fetchFriends();
    this.fetchFriendRequests();
  },
  mounted() {
    eventBus.fetchFriendsEvent = this.fetchFriends;
    eventBus.fetchFriendRequestsEvent = this.fetchFriendRequests;
  },
  methods: {
    async fetchFriends() {
      try {
        const userId = parseInt(this.getUserId);

        if (isNaN(userId)) {
          console.error("Invalid userId:", this.getUserId);
          return;
        }

      const response = await axios.get("http://localhost:8085/api/friendship/getfriends", {
          params: { userId: userId },
        });

        this.friends = response.data || [];

      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error fetching friends.';
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: errorMessage,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      }
    },

    async fetchFriendRequests() {
      try {
        const userId = parseInt(this.getUserId);

        if (isNaN(userId)) {
          return;
        }

        const response = await axios.get("http://localhost:8085/api/friendship/getFriendRequests", {
          params: { userId: userId },
        });
        this.friendRequests = response.data || [];

      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error fetching friend requests.';
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: errorMessage,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      }
    },

    async updateRequestStatus(friendshipId, status) {
      try {
        await axios.post(`http://localhost:8085/api/friendship/updateStatusRequest/${friendshipId}`, {
          status: status
        });

        Swal.fire({
          icon: 'success',
          title: status ? 'Friend Request Accepted!' : 'Friend Request Declined!',
          text: status ? 'You are now friends.' : 'The request was declined.',
          timer: 2000,
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto: false
        });

        this.fetchFriendRequests();

      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error updating friend request status.';
        Swal.fire({
          icon: 'error',
          title: 'Sorry, your request failed',
          text: errorMessage,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      } finally {
        this.closeModal();
        window.location.reload();
      }
    },

    showFriendRequestsPopup() {
      if (this.friendRequests.length > 0) {
        this.isModalVisible = true;
      }
    },

    async acceptAllRequests() {
      try {
        await axios.post("http://localhost:8085/api/friendship/acceptAllRequests", null, {
          params: { userId: parseInt(this.getUserId) }
        });

        Swal.fire({
          icon: 'success',
          title: 'All Friend Requests Accepted!',
          text: 'You are now friends with all pending requests.',
          confirmButtonText: 'Okay',
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto: false
        });

        this.fetchFriendRequests();

      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Failed to accept all friend requests.';
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: errorMessage,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      } finally {
        this.closeModal();
        window.location.reload();
      }
    },

    closeModal() {
      this.isModalVisible = false;
    },
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const username = computed(() => store.state.username);

    const logout = () => {
      store.dispatch('logoutUser');
      router.push('/login');
    };

    return { username, logout };
  }
};
</script>