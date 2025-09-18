<template>
  <div>
    <div class="search" v-if="user.role === 'ADMIN'">
      <el-select v-model="departmentId" placeholder="请选择社团" style="width: 200px">
        <el-option v-for="item in departmentData" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd" v-if="user.role === 'USER'">新增</el-button>
      <el-button type="danger" plain @click="delBatch" v-if="user.role === 'ADMIN' ">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column label="宣传图片">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="活动名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="活动开始时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="timeEnd" label="活动结束时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="活动内容" show-overflow-tooltip>
          <template v-slot="scope">
            <el-button type="success" @click="viewEditor(scope.row.description)">查看内容</el-button>
          </template>
        </el-table-column>

        <el-table-column prop="departmentName" label="所属社团"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini" v-if="user.role === 'USER'">编辑</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
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


    <el-dialog title="信息" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close @close="cancel">
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="宣传图片">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="活动名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="活动介绍">
          <el-input type="textarea" :rows="5" v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <div style="margin-left: 100px">
          <el-date-picker v-model="form.timeEnd" type="date" placeholder="选择截止日期">
          </el-date-picker>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="社团介绍" :visible.sync="editorVisible" width="50%">
      <div v-html="this.viewData" class="w-e-text"></div>
    </el-dialog>

  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: "Department",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      rules: {
      },
      ids: [],
      headerData: [],
      departmentData: [],
      departmentId: null,
      viewData: null,
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
    viewEditor(description) {
      this.viewData = description
      this.editorVisible = true
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    cancel() {
      this.fromVisible = false
      location.href = '/activity'
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/activity/update' : '/activity/add',
            method: this.form.id ? 'PUT' : 'POST',//POST通常用于创建新资源或提交数据给服务器进行处理；PUT主要用于更新或替换服务器上的资源。
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
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/activity/delete/' + id).then(res => {
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
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/activity/delete/batch', {data: this.ids}).then(res => {
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
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/activity/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          if (this.tableData) {
            this.tableData = this.tableData.map(item => {
              item.timeEnd = moment(item.timeEnd).format('YYYY-MM-DD')
              return item
            })
          }
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }

      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把图片url保存到img里
      this.form.img = response.data
    },
  }

}
</script>

<style scoped>

</style>
