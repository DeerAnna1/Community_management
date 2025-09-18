<template>
  <div class="main-content">
    <div style="width: 70%; margin: 10px auto">
      <div style="font-size: 18px; height: 80px; line-height: 80px; border-bottom: 1px solid #e7e6e6">我的申请记录（{{ tableData.length }}）</div>
      <div style="margin-top: 20px">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="departmentName" label="申请的社团"></el-table-column>
          <el-table-column prop="status" label="审核状态">
            <template v-slot="scope">
              <el-button v-if="scope.row.status === '审核通过'" type="success" size="mini">{{scope.row.status}}</el-button>
              <el-button v-if="scope.row.status === '审核不通过'"  size="mini"> {{scope.row.status}}</el-button>
              <el-button v-if="scope.row.status === '待审核'" type="warning" size="mini">{{scope.row.status}}</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="申请说明" show-overflow-tooltip></el-table-column>
          <el-table-column prop="process" label="当前进度"></el-table-column>
          <el-table-column prop="note" label="审核说明" show-overflow-tooltip></el-table-column>

          <el-table-column label="操作" width="180" align="center" v-if="user.role === 'USER'">
            <template v-slot="scope">
              <el-button plain type="primary" :disabled="scope.row.status !== '待审核'" @click="del(scope.row.id)" size="mini">撤销申请</el-button>
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
    let departmentId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      tableData: []
    }
  },
  mounted() {
    this.loadApply()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadApply() {
      this.$request.get('/apply/selectMyApply').then(res => {
        if (res.code === '200') {
          this.tableData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定撤销申请吗？', '确认撤销', {type: "warning"}).then(response => {
        this.$request.delete('/apply/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$notify.success('操作成功')
            this.loadApply()
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
