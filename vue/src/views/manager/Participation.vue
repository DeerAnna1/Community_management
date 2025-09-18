<template>
  <div>
    <div class="search">
      <el-select v-model="activityId" placeholder="请选择活动名称" style="width: 200px">
        <el-option v-for="item in activityData" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe >
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="学生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="所属社团" show-overflow-tooltip></el-table-column>
        <el-table-column prop="activityName" label="参与活动名称" show-overflow-tooltip></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="del(scope.row.id)" size="mini">移除成员</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Participation",
  data() {
    return {
      tableData: [],  // 所有的数据
      activityData: [],  // 所有活动数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      activityId: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      rules: {
      },
      ids: [],
    }
  },
  created() {
    this.load(1)
    this.loadActivity()
  },
  methods: {
    loadActivity(){
      this.$request.get('/activity/selectAll').then(res =>{
        if (res.code === '200'){
          this.activityData = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/participation/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          activityId: this.activityId,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定移除该成员吗？', '确认移除', {type: "warning"}).then(response => {
        this.$request.delete('/participation/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    reset() {
      this.activityId = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
