<template>
  <div class="main-content">
    <div style="width: 70%; margin: 10px auto">
      <div style="font-size: 18px; height: 80px; line-height: 80px; border-bottom: 1px solid #e7e6e6">我参与的社团活动（{{ tableData.length }}）</div>
      <div style="margin-top: 20px">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="departmentName" label="社团名称"></el-table-column>
          <el-table-column prop="activityName" label="社团活动名称"></el-table-column>
          <el-table-column label="操作" width="180" align="center" v-if="user.role === 'USER'">
            <template v-slot="scope">
              <el-button plain type="primary" @click="del(scope.row.id)" size="mini">取消参与</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    let departmentId = this.$route.query.id,
    activityId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      tableData: []
    }
  },
  mounted() {
    this.loadParticipation()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadParticipation() {
      this.$request.get('/participation/selectMyParticipation').then(res => {
        if (res.code === '200') {
          this.tableData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定取消参与本次活动吗？', '确认取消', {type: "warning"}).then(response => {
        this.$request.delete('/participation/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$notify.success('操作成功')
            this.loadParticipation()
          } else {
            this.$notify.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
  }
}
</script>
