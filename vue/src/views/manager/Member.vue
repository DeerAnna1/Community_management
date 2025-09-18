<template>
  <div>
    <!--只有管理员才能按社团名称进行搜索-->
    <div class="search" v-if="user.role === 'ADMIN'">
      <el-select v-model="departmentId" placeholder="请选择社团" style="width: 200px">
        <el-option v-for="item in departmentData" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>
    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="学生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="所属社团" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="申请说明"></el-table-column>

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
  name: "Member",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      rules: {
      },
      ids: [],
      departmentId: null,
      departmentData: []
    }
  },
  created() {
    this.load(1)
    this.loadDepartment()
  },
  methods: {
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/apply/selectMember', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定移除该成员吗？', '确认移除', {type: "warning"}).then(response => {
        this.$request.delete('/apply/delete/' + id).then(res => {
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
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    reset() {
      this.departmentId = null
      this.load(1)
    },
  }
}
</script>

<style scoped>

</style>
