<template>
  <ChatWindow :receiverId="receiverId"/>
</template>

<script>
import ChatWindow from "./ChatWindow.vue";
import { useRoute } from 'vue-router';

export default {
  components: { ChatWindow },
  setup() {
    const route = useRoute();
    const receiverId = route.params.receiverId;
    return { receiverId };
  }
};
</script>
