<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Statistics</span>
    </div>
    <div class="content-page">
      <p>Message Count: {{ messageCount }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'StatsPage',
  data() {
    return {
      messageCount: 0,
    };
  },
  mounted() {
    axios.get('http://localhost:5001/stats')
        .then(response => {
          this.messageCount = response.data.message_count;
        })
        .catch(error => {
          console.error("There was an error fetching the statistics:", error);
        });
  }
};
</script>
