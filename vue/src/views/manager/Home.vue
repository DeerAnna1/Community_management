<template>
  <div>
    <div class="card" style="padding: 20px; font-size: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <div style="display: flex; margin-top: 20px; grid-gap: 15px; margin-bottom: 10px">
        <div class="card item">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 5px">社团总数</div>
            <div style="font-weight: bold">{{ count.departmentCount }}</div>
          </div>
        </div>
        <div class="card item">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 5px">活动总数</div>
            <div style="font-weight: bold">{{ count.activityCount }}</div>
          </div>
        </div>
        <div class="card item">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 5px">评论总数</div>
            <div style="font-weight: bold">{{ count.commentCount }}</div>
          </div>
        </div>
    </div>
    <div style="display: flex; grid-gap: 15px">
      <div style="flex: 1;" class="card">
        <div id="pie" style="height: 400px"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

const pieOption = {
  title: {
    text: '社团活动分类统计',
    textStyle: {
      fontSize: 22,
      fontWeight: 'bold'
    },
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['50%', '55%'],
      radius: '70%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}
export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      count: {
        commentCount: 0,
        departmentCount: 0,
        activityCount: 0
      }
    }
  },
  mounted() {
    this.getCommentCount()

    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom);
    this.$request.get('/pieData').then(res => {
      pieOption.series[0].data = res.data || []
      pieChart.setOption(pieOption)
    })
  },
  methods: {
    getCommentCount() {
      this.$request.get('/count').then(res => {
        this.count = res.data || {}
      })
    }
  }
}
</script>

<style scoped>
.item {
  flex: 1;
  display: flex;
  align-items: center;
  height: 80px;
  background-color: #e6f1fe;
}
</style>