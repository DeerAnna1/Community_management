<template>
  <div>
    <div class="search">
      <el-select v-model="Status" placeholder="请选择审核状态" style="width: 200px">
        <el-option label="待审核" value="待审核"></el-option>
        <el-option label="审核通过" value="审核通过"></el-option>
        <el-option label="审核不通过" value="审核不通过"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="学生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="所属社团" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="申请说明"></el-table-column>
        <el-table-column prop="process" label="当前进度"></el-table-column>
        <el-table-column prop="status" label="审核状态"></el-table-column>
        <el-table-column prop="note" label="审核说明"></el-table-column>

        <el-table-column label="操作" width="180" align="center" v-if="user.role === 'USER'">
          <template v-slot="scope">
            <el-button plain type="primary" :disabled="scope.row.status !== '待审核'" @click="handleEdit(scope.row)" size="mini">审核</el-button>
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


    <el-dialog title="审核信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="status" label="审核状态">
          <el-select v-model="status" placeholder="请选择" style="width: 100%">
            <el-option label="审核通过" value="审核通过"></el-option>
            <el-option label="审核不通过" value="审核不通过"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="note" label="审核说明">
          <el-input v-model="form.note" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "Apply",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      Status: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      rules: {
        status: [
          {required: true, message: '请选择审核状态', trigger: 'blur'},
        ],
      },
      ids: [],
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.status = this.status
          this.form.process = '审核完成'
          this.$request({
            url: this.form.id ? '/apply/update' : '/apply/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/apply/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.Status,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.Status = null
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
