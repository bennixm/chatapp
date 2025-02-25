<script setup>
import { ref } from "vue";
import { VueDataUi } from "vue-data-ui";
import "vue-data-ui/style.css";


// we create the configs here

const timepredictionConfig = ref({
  animationFrames: 60,
  animationValueStart: 0,
  backgroundColor: '#00000040',
  fontFamily: 'inherit',
  layoutClass: 'p-4 m-4 rounded-md shadow',
  layoutCss: '',
  prefix: '',
  suffix: '',
  title: 'Response time of User (minutes)',
  titleBold: true,
  titleColor: '#ffffff',
  titleClass: '',
  titleCss: '',
  titleFontSize: 16,
  useAnimation: true,
  valueBold: true,
  valueColor: '#009ca6',
  valueClass: '',
  valueCss: '',
  valueFontSize: 32,
  valueRounding: 0,
  formatter: null,
  analogDigits: {
    show: false,
    height: 40,
    color: '#1A1A1Aff',
    skeletonColor: '#E1E5E8'
  }
});

const sparklineConfig = ref(
    {"style":{"backgroundColor":"#00000040","chartWidth":350,"area":{"show":true,"color":"#009ca6"},"dataLabel":{"fontSize":44,"color":"#CCCCCC"},"line":{"color":"#009ca6"},"title":{"color":"#009ca6","text":"Monthly messages in 2024"}}}
);
const sparklineDataset = ref([]);
const moodConfig = ref({
    theme: '',
    style: {
      fontFamily: 'inherit',
      chart: {
        backgroundColor: '#00000040',
        color: '#ffffffff',
        layout: {
          grid: {
            show: true,
            stroke: '#81f6ffff',
            strokeWidth: 0.6
          },
          outerPolygon: {
            stroke: '#0aefffff',
            strokeWidth: 0.40000000000000013
          },
          dataPolygon: {
            color: '#009ca6ff',
            opacity: 60,
            gradient: {
              show: true,
              intensity: 5
            },
            stroke: '#6376DDff',
            strokeWidth: 0.6
          },
          smileys: {
            strokeWidth: 1,
            colors: {
              '1': '#a80000',
              '2': '#ff9f03',
              '3': '#ffd004',
              '4': '#9ac900',
              '5': '#059f00'
            }
          },
          dataLabel: {
            color: '#ffffffff',
            roundingValue: 0,
            roundingPercentage: 0,
            bold: true,
            prefix: '',
            suffix: '',
            formatter: null
          }
        },
        title: {
          text: 'Mood',
          color: '#ffffffff',
          fontSize: 20,
          bold: true,
          textAlign: 'center',
          paddingLeft: 0,
          paddingRight: 0,
          subtitle: {
            color: '#ffffffff',
            text: 'Mood of messages',
            fontSize: 16,
            bold: false
          }
        },
        legend: {
          show: true,
          bold: false,
          backgroundColor: '#000000a1',
          color: '#ffffffff',
          fontSize: 14,
          roundingValue: 0,
          roundingPercentage: 0
        }
      }
    },
    userOptions: {
      show: true,
      showOnChartHover: false,
      keepStateOnChartLeave: true,
      position: 'right',
      buttons: {
        tooltip: false,
        pdf: true,
        csv: true,
        img: true,
        table: true,
        labels: false,
        fullscreen: true,
        sort: false,
        stack: false,
        animation: false,
        annotator: true
      },
      buttonTitles: {
        open: 'Open options',
        close: 'Close options',
        pdf: 'Download PDF',
        csv: 'Download CSV',
        img: 'Download PNG',
        table: 'Toggle table',
        fullscreen: 'Toggle fullscreen',
        annotator: 'Toggle annotator'
      }
    },
    table: {
      show: false,
      responsiveBreakpoint: 400,
      columnNames: {
        series: 'Series',
        value: 'Value',
        percentage: 'Percentage'
      },
      th: {
        backgroundColor: '#FFFFFFff',
        color: '#00ced1ff',
        outline: 'none'
      },
      td: {
        backgroundColor: '#FFFFFFff',
        color: '#ffffffff',
        outline: 'none',
        roundingValue: 0,
        roundingPercentage: 0
      }
    }
  });

const moodDataset = ref({
  '1': 0,
  '2': 0,
  '3': 0,
  '4': 0,
  '5': 0
});

const donutConfig = ref({
  type: 'classic',
  responsive: false,
  theme: '',
  customPalette: [],
  useCssAnimation: true,
  useBlurOnHover: true,
  userOptions: {
    show: true,
    showOnChartHover: false,
    keepStateOnChartLeave: true,
    position: 'right',
    buttons: {
      tooltip: true,
      pdf: true,
      csv: true,
      img: true,
      table: true,
      labels: true,
      fullscreen: true,
      sort: false,
      stack: false,
      animation: false,
      annotator: true
    },
    buttonTitles: {
      open: 'Open options',
      close: 'Close options',
      tooltip: 'Toggle tooltip',
      pdf: 'Download PDF',
      csv: 'Download CSV',
      img: 'Download PNG',
      table: 'Toggle table',
      labels: 'Toggle labels',
      fullscreen: 'Toggle fullscreen',
      annotator: 'Toggle annotator'
    }
  },
  translations: {
    total: 'Total',
    average: 'Average'
  },
  table: {
    show: false,
    responsiveBreakpoint: 400,
    th: {
      backgroundColor: '#000000a1',
      color: '#ffffffff',
      outline: 'none'
    },
    td: {
      backgroundColor: '#000000a1',
      color: '#ffffffff',
      outline: 'none',
      roundingValue: 0,
      roundingPercentage: 0
    },
    columnNames: {
      series: 'Series',
      value: 'Value',
      percentage: 'Percentage'
    }
  },
  style: {
    fontFamily: 'inherit',
    chart: {
      useGradient: true,
      gradientIntensity: 40,
      backgroundColor: '#00000040',
      color: '#ffffffff',
      layout: {
        labels: {
          dataLabels: {
            show: true,
            useLabelSlots: false,
            hideUnderValue: 3,
            prefix: '',
            suffix: ''
          },
          value: {
            rounding: 0,
            show: true,
            formatter: null
          },
          percentage: {
            color: '#ffffffff',
            bold: true,
            fontSize: 18,
            rounding: 0,
            formatter: null
          },
          name: {
            color: '#ffffffff',
            bold: false,
            fontSize: 14
          },
          hollow: {
            show: true,
            total: {
              show: true,
              bold: false,
              fontSize: 18,
              color: '#d3d3d3ff',
              text: 'Total',
              offsetY: 1,
              value: {
                color: '#ffffffff',
                fontSize: 18,
                bold: true,
                suffix: '',
                prefix: '',
                offsetY: 0,
                rounding: 0,
                formatter: null
              }
            },
            average: {
              show: false,
              bold: false,
              fontSize: 18,
              color: '#d3d3d3ff',
              text: 'Average',
              offsetY: 0,
              value: {
                color: '#ffffffff',
                fontSize: 18,
                bold: true,
                suffix: '',
                prefix: '',
                offsetY: 0,
                rounding: 0,
                formatter: null
              }
            }
          }
        },
        donut: {
          strokeWidth: '50',
          borderWidth: '9',
          useShadow: false,
          shadowColor: 'rgba(255, 255, 255, 1)'
        }
      },
      comments: {
        show: true,
        showInTooltip: true,
        width: 100,
        offsetY: 0,
        offsetX: 0
      },
      legend: {
        show: true,
        bold: false,
        backgroundColor: '#000000a1',
        color: '#ffffffff',
        fontSize: 16,
        roundingValue: 0,
        roundingPercentage: 0
      },
      tooltip: {
        show: true,
        color: '#ffffffff',
        backgroundColor: '#FFFFFFff',
        fontSize: 14,
        customFormat: null,
        borderRadius: 4,
        borderColor: '#e1e5e8',
        borderWidth: 1,
        backgroundOpacity: 30,
        position: 'center',
        offsetY: 24,
        showValue: true,
        showPercentage: true,
        roundingValue: 0,
        roundingPercentage: 0
      },
      title: {
        text: 'General Stats',
        color: '#ffffffff',
        fontSize: 20,
        bold: true,
        textAlign: 'center',
        paddingLeft: 0,
        paddingRight: 0,
        subtitle: {
          color: '#A1A1A1ff',
          text: 'Platform data overview',
          fontSize: 16,
          bold: false
        }
      }
    }
  }
});
const donutDataset =  ref([
  {
    "name": "Users",
    "values": [
      0
    ]
  },
  {
    "name": "Messages",
    "values": [
      0
    ]
  },
  {
    "name": "Chats",
    "values": [
      0
    ]
  },
  {
    "name": "Friendships",
    "values": [
      0
    ]
  }
]);

const candlestickConfig = ref( {"responsive":false,"theme":"","useCssAnimation":true,"style":{"fontFamily":"inherit","backgroundColor":"#00000040","color":"#ffffff","height":316,"width":512,"layout":{"padding":{"top":36,"right":48,"bottom":36,"left":48},"selector":{"color":"#ffffff","opacity":10},"grid":{"show":true,"stroke":"#e1e5e8","strokeWidth":0.5,"xAxis":{"dataLabels":{"show":true,"fontSize":10,"color":"#ffffff","offsetY":0,"bold":false,"rotation":0}},"yAxis":{"dataLabels":{"show":true,"fontSize":12,"color":"#ffffff","roundingValue":0,"offsetX":0,"bold":false,"steps":10,"prefix":"","suffix":""}}},"wick":{"stroke":"#ffffff","strokeWidth":0.5,"extremity":{"shape":"line","size":"auto","color":"#ffffff"}},"candle":{"borderRadius":1,"stroke":"#ffffff","strokeWidth":0.5,"colors":{"bearish":"#dc3912","bullish":"#2ca02c"},"gradient":{"show":true,"underlayer":"#FFFFFF"},"widthRatio":0.5}},"zoom":{"show":true,"color":"#CCCCCC","highlightColor":"#4A4A4A","fontSize":14,"useResetSlot":false,"startIndex":null,"endIndex":null,"enableRangeHandles":true,"enableSelectionDrag":true},"title":{"text":"Response time / hour","color":"#ffffff","fontSize":20,"bold":true,"textAlign":"center","paddingLeft":0,"paddingRight":0,"subtitle":{"color":"#A1A1A1","text":"Response Time by Hour of the Day","fontSize":16,"bold":false}},"tooltip":{"show":true,"color":"#ffffff","backgroundColor":"#FFFFFF","fontSize":14,"customFormat":null,"borderRadius":4,"borderColor":"#e1e5e8","borderWidth":1,"backgroundOpacity":100,"position":"center","offsetY":24,"roundingValue":0,"prefix":"","suffix":""}},"translations":{"period":"Period","open":"Open","high":"High","low":"Low","last":"Last","volume":"Volume"},"userOptions":{"show":true,"showOnChartHover":false,"keepStateOnChartLeave":true,"position":"right","buttons":{"tooltip":true,"pdf":true,"csv":true,"img":true,"table":true,"labels":false,"fullscreen":true,"sort":false,"stack":false,"animation":false,"annotator":true},"buttonTitles":{"open":"Open options","close":"Close options","tooltip":"Toggle tooltip","pdf":"Download PDF","csv":"Download CSV","img":"Download PNG","table":"Toggle table","fullscreen":"Toggle fullscreen","annotator":"Toggle annotator"}},"table":{"show":false,"responsiveBreakpoint":400,"th":{"backgroundColor":"#fafafa","color":"#ffffff","outline":"none"},"td":{"backgroundColor":"#FFFFFF","color":"#ffffff","outline":"none","roundingValue":2,"prefix":"","suffix":""}}});

const candlestickDataset = [];


// we create the methods here for setup

const fetchStats = async () => {
  try {

    const response = await axios.get("http://localhost:5001/stats");


    if (response.data) {
      // Map the response to update the donutDataset
      response.data.forEach((statData) => {
        switch (statData.stat) {
          case "users":
            donutDataset.value[0].values[0] = statData.count;
            break;
          case "messages":
            donutDataset.value[1].values[0] = statData.count;
            break;
          case "chats":
            donutDataset.value[2].values[0] = statData.count;
            break;
          case "friendships":
            donutDataset.value[3].values[0] = statData.count;
            break;
          default:
            break;
        }
      });
    }
  } catch (error) {
    console.error("Error fetching stats:", error);
  }
};

const fetchSparkLine = async () => {
  try {
    const response = await axios.get("http://localhost:5001/get_monthly_message_counts");
    if (response.data) {
      sparklineDataset.value = Object.keys(response.data).map(month => ({
        period: month,
        value: response.data[month]
      }));
    }
  } catch (error) {
    console.log("Failed to Fetch SparkLine: There was an error getting the messages count.");
  }
};

const fetchMoodData = async () => {
  try {
    const response = await axios.get("http://localhost:5001/get_message_sentiments");
    if (response.data) {
      // Map the response to update the moodDataset
      response.data.forEach((sentimentData) => {
        switch (sentimentData.sentiment) {
          case "very_positive":
            moodDataset.value['5'] = sentimentData.count;
            break;
          case "positive":
            moodDataset.value['4'] = sentimentData.count;
            break;
          case "neutral":
            moodDataset.value['3'] = sentimentData.count;
            break;
          case "negative":
            moodDataset.value['2'] = sentimentData.count;
            break;
          case "very_negative":
            moodDataset.value['1'] = sentimentData.count;
            break;
          default:
            break;
        }
      });
    }
  } catch (error) {
    console.error("Failed to fetch mood data:", error);
  }
};

const trainmodel =  async () => {
  try {
    const response = await axios.get("http://localhost:5001/train");
    if (response.data.message) {
      console.log("Model Training Started: Training is in progress.");
    }
  } catch (error) {
    console.log("Training Failed: Error occurred while training.");
  }
};

async function fetchGraphs() {
  try {
    const response = await axios.get("http://localhost:5001/generate_graph");
    console.log(response.data);  // Log the full response to inspect its structure

    Object.keys(response.data).forEach((graphKey) => {
      const graphData = response.data[graphKey];

      // Ensure that x, y, and y_error arrays exist and are not empty
      if (graphKey === "graph_1") {

        graphData.x.forEach((hour, index) => {
          const open = graphData.open[index];  // Mean response time (open value)
          const high = graphData.high[index];  // High estimate (mean + error/2)
          const low = graphData.low[index];  // Low estimate (mean - error/2)
          const close = graphData.close[index];  // Close is the same as open if there's no real close data, but you could update this if you have a close price for each period
          const volume = graphData.volume[index];  // Assuming you have volume data or else random volume

          // Format the time value as just the hour (e.g., '00:00', '01:00', etc.)
          const time = `${String(hour).padStart(2, '0')}`; // Only the hour part

          // Update candlestickDataset with the new values
          candlestickDataset[index] = [
            time,  // Timestamp (formatted with only the hour)
            open,  // Open value (Mean response time)
            high,  // High value (Mean + Error/2)
            low,   // Low value (Mean - Error/2)
            close, // Close value (same as open for simplicity unless you have real close data)
            volume // Volume (real or random fallback)
          ];
        });
      }
    });

  } catch (error) {
    console.error('Failed to fetch graphs data:', error);
  }
}



// we load the methods here with reload
trainmodel();
fetchSparkLine();
fetchMoodData();
fetchStats();
fetchGraphs();

</script>

<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Statistics</span>
    </div>
    <div class="stats-content">
      <div class="content-page centered-content">
        <div class="centered-item">
          <VueDataUi
              component="VueUiDonut"
              :dataset="donutDataset"
              :config="donutConfig"
          />
        </div>
      </div>
      <div class="search-nav">
        <button @click="generateUsers">Generate 10 Users</button>
        <button @click="generateFriendships">Generate 50 Friendships</button>
        <button @click="generateChats">Generate 50 Chats</button>
        <button @click="generateMessages">Generate 200 Messages</button>
      </div>
    </div>
    <div class="stats-content">
      <div class="content-page">
        <div class="stats-item">
          <VueDataUi
              component="VueUiMoodRadar"
              :config="moodConfig"
              :dataset="moodDataset"
          />
        </div>
        <div class="stats-item">
          <div class="stats-item-small">
            <VueDataUi
                component="VueUiSparkline"
                :dataset="sparklineDataset"
                :config="sparklineConfig"
            />
          </div>
          <div class="stats-item-small" v-if="prediction !== null">
              <VueDataUi
                  component="VueUiKpi"
                  :config="timepredictionConfig"
                  :dataset="formattedPrediction"
              />
          </div>
        </div>
      </div>
      <div class="search-nav">
        <form @submit.prevent="predictResponseTime" class="form">
          <input v-model="sender_id" type="number" placeholder="Enter user id.." required />
          <button type="submit" class="predict-btn">Predict Response Time</button>
        </form>
      </div>
    </div>
    <div class="stats-content">
      <div class="content-page">
        <VueDataUi
            component="VueUiCandlestick"
            :dataset="candlestickDataset"
            :config="candlestickConfig"
        />
      </div>
      <div class="search-nav">
        <button @click="trainModel" class="train-btn" :disabled="isTraining">
          {{ isTraining ? "Training in Progress..." : "Train Model" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Swal from "sweetalert2";

export default {
  name: 'StatsPage',
  data() {
    return {
      user_count: 0,
      sender_id: "",
      prediction: null,
      graphs: null,
    };
  },
  computed: {
    formattedPrediction() {
      if (this.prediction === null) return null;
      return this.prediction / 60;
    }
  },
  methods: {
    showSuccess(message) {
      Swal.fire({
        icon: "success",
        title: "Success!",
        text: message,
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
    },
    showError(message) {
      Swal.fire({
        icon: "error",
        title: "Fail!",
        text: message,
        customClass: {
          popup: "custom-popup",
        },
        background: "#000000e0",
        color: "#ffffff",
        confirmButtonColor: "#009ca6",
        heightAuto: false
      });
    },

    async generateUsers() {
      try {
        const response = await fetch("http://localhost:5001/generate_users", { method: "POST" });
        const data = await response.json();
        this.showSuccess(data.message);
      } catch (error) {
        this.showError("Failed to generate users..");
      }
    },
    async generateFriendships() {
      try {
        const response = await fetch("http://localhost:5001/generate_friendships", { method: "POST" });
        const data = await response.json();
        this.showSuccess(data.message);
      } catch (error) {
        this.showError("Failed to generate friendships");
      }
    },
    async generateChats() {
      try {
        const response = await fetch("http://localhost:5001/generate_chats", { method: "POST" });
        const data = await response.json();
        this.showSuccess(data.message);
      } catch (error) {
        this.showError("Failed to generate chats");
      }
    },
    async generateMessages() {
      try {
        const response = await fetch("http://localhost:5001/generate_messages", { method: "POST" });
        const data = await response.json();
        this.showSuccess(data.message);
      } catch (error) {
        this.showError("Failed to generate chats");
      }
    },
    async predictResponseTime() {
      if (!this.sender_id) {
        Swal.fire({ icon: "warning", title: "Missing Input", text: "Please enter a User ID." });
        return;
      }
      try {
        const response = await axios.post("http://localhost:5001/predict_response_time", { sender_id: this.sender_id });
        this.prediction = Math.round(response.data.predicted_response_time);
      } catch (error) {
        this.showError("Prediction Failed: Ensure the model is trained and input is correct.");
      }
    }
  }
};
</script>
