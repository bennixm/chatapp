<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Find people</span>
    </div>
    <div class="content-page">
      <div class="search-section" v-if="!loading">
        <div class="search-section-content" v-if="users.length">
          <div class="users-fetch">
            <div class="display-user-item" v-for="user in currentPageUsers" :key="user.userid">
              <div class="user-item-identity"></div>
              <div class="user-item-content">
                <span class="username">{{ user.username }}</span>
                <span class="email">{{ user.email }}</span>
              </div>
              <div class="user-item-buttons">
                <button
                    v-if="!isFriend(user.userid)"
                    @click="sendFriendRequest(user.userid)"
                    class="send-request-button">
                  <i class="fa fa-user-plus" aria-hidden="true"></i> Add
                </button>
              </div>
            </div>
          </div>
          <div class="pagination">
            <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">Previous</button>
            <span>{{ currentPage }} / {{ totalPages }}</span>
            <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">Next</button>
          </div>
        </div>
        <div class="search-section-content" v-else>
          <span class="feedback-request">No users found.</span>
        </div>
        <div class="search-nav">
          <div class="search-input">
            <input
                type="text"
                v-model="searchQuery"
                placeholder="Search by username"
                @input="filterUsers" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { mapGetters } from "vuex";
import { showSuccessAlert, showErrorAlert } from '@/utils/alertService';
import secureApi from '../secureApi';


export default {
  name: "SearchPage",
  data() {
    return {
      users: [],
      allUsers: [],
      filteredUsers: [],
      friends: [],
      loading: true,
      currentPage: 1,
      itemsPerPage: 8,
      searchQuery: '',
    };
  },
  computed: {
    ...mapGetters(["getUserId"]),
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    },
    currentPageUsers() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredUsers.slice(start, end);
    }
  },
  created() {
    this.fetchUsersAndFriends();
  },
  methods: {
    async fetchUsersAndFriends() {
      try {
        const allUsersResponse = await secureApi.get("/v1/user/allusers");
        this.allUsers = allUsersResponse.data;

        await this.getUserFriends(this.getUserId);

        this.users = this.allUsers.filter((user) => {
          const isCurrentUser = user.userid === parseInt(this.getUserId);
          const isFriend = this.isFriend(user.userid);
          return !isCurrentUser && !isFriend;
        });

        this.filteredUsers = [...this.users];
      } catch (error) {
        console.error("There was an error fetching the users:", error);
      } finally {
        this.loading = false;
      }
    },
    async getUserFriends(userId) {
      try {
        const response = await secureApi.get("/friendship/getfriends", {
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
        const response = await secureApi.post(
            '/friendship/send',
            {
              senderUserid: senderId,
              receiverUserid: receiverId,
            }
        );

        await showSuccessAlert('Friend Request Sent!', response.data.message);

        setTimeout(() => {
          this.fetchUsersAndFriends();
        }, 2000);
      } catch (error) {
        console.error("Error sending friend request:", error);
        const errorMessage = error.response?.data?.errorMessage || 'Error sending friend request.';
        await showErrorAlert('Error!', errorMessage);
      }
    },
    filterUsers() {
      if (this.searchQuery.trim() === '') {
        this.filteredUsers = [...this.users];
      } else {
        this.filteredUsers = this.users.filter(user =>
            user.username.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }
      this.currentPage = 1;
    },
    goToPage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
    }
  },
};
</script>
