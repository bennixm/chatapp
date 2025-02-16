<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Find people</span>
    </div>
    <div class="content-page">
      <div class="search-section" v-if="!loading">
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
                  @click="sendFriendRequest(user.userid)" class="send-request-button">
                <i class="fa fa-user-plus" aria-hidden="true"></i> Add
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
import { useRouter } from 'vue-router';

export default {
  name: "SearchPage",
  setup(){
    const router = useRouter();
  },
  data() {
    return {
      users: [],
      allUsers: [],
      friends: [],
      loading: true,
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

        this.users = this.allUsers.filter((user) => {
          const isCurrentUser = user.userid === parseInt(this.getUserId);
          const isFriend = this.isFriend(user.userid);

          return !isCurrentUser && !isFriend;
        });

      } catch (error) {
        console.error("There was an error fetching the users:", error);
      } finally {
      this.loading = false;
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
        if (response.data.message === "You have accepted the friend request!") {
          setTimeout(() => {
            router.push({ name: 'NewChat' });
          }, 2000);
        } else {
          setTimeout(() => {
            this.fetchUsersAndFriends();
          }, 2000);
        }

      }  catch (error) {
        console.error("Error sending friend request:", error);

        const errorMessage = error.response?.data?.errorMessage || 'Error sending friend request.';

        Swal.fire({
          icon: 'error',
          title: 'Error!',
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
    }
  },
};
</script>

