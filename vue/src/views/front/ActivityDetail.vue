<template>
  <div class="main-content">
    <div style="width: 60%; margin: 20px auto">
      <div style="color: #333333; font-size: 20px; font-weight: 700">{{ activityData.name }}
        <el-button type="primary" @click="init">参与活动</el-button></div>
      <div style="margin-top: 10px; color: #767474">
        <span style="font-weight: 600; color: #404040">
          <a href="#" @click="navTo('/front/DepartmentDetail?id=' + activityData.departmentId)">
            {{activityData.departmentName}}</a>
        </span>
        <span style="margin-left: 50px">开始时间：{{ activityData.time }}</span>
        <span style="margin-left: 50px">结束时间：{{ activityData.timeEnd }}</span>
      </div>
      <div style="margin-top: 30px" v-html="activityData.description"></div>

      <div style="margin-top: 50px; font-size: 18px">欢迎评论~</div>
      <div style="margin-top: 20px">
        <el-input type="textarea" :rows="5" v-model="content"></el-input>
      </div>
      <div style="margin-top: 10px; text-align: right">
        <el-button type="primary" @click="submit(content)">提交</el-button>
      </div>

      <div style="margin-top: 30px; margin-bottom: 500px">
        <el-row v-for="item in commentData" style="margin-bottom: 50px" >
          <el-col :span="4">
            <div style="display: flex; align-items: center;">
              <img :src="item.userAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
              <div style="flex: 1; margin-left: 10px;font-size: 18px;">{{item.userName}}</div>
            </div>
          </el-col>
          <el-col :span="20">
            <div style="height: 50px; line-height: 50px">
              <el-row>
                <el-col :span="18" style="font-size: 18px;">{{item.content}}</el-col>
                <el-col :span="6" style="text-align: right">{{item.time}}</el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <el-dialog title="是否确认参与" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

export default {

  data() {
    let activityId = this.$route.query.id,
    departmentId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      activityData: {},
      activityId: activityId,
      departmentData: {},
      departmentId: departmentId,
      fromVisible:false,
      content:null,
      commentData: [],
    }
  },
  mounted() {
    this.loadActivity()
    this.loadComment()
  },
  methods: {
    submit(content) {
      let data = {
        userId: this.user.id,
        activityId: this.activityId,
        content: content,
      }
      this.$request.post('/comment/add', data).then(res => {
        if (res.code === '200') {
          this.$notify.success('评论成功')
          this.content = null
          this.loadComment()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    loadComment() {
      this.$request.get('/comment/selectAll?activityId=' + this.activityId).then(res => {
        if (res.code === '200') {
          this.commentData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    loadActivity() {
      this.$request.get('/activity/selectById/' + this.activityId).then(res => {
        if (res.code === '200') {
          this.activityData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    navTo(url) {
      location.href = url
    },
    init() {
      this.fromVisible = true
    },
    save() {
      let data1 = {
        userId: this.user.id,
        departmentId: this.activityData.departmentId,
        activityId: this.activityId,
        time: new Date().getTime(),
      }
      this.$request.post('/participation/add', data1).then(res => {
        if (res.code === '200') {//接口调用成功
          this.$notify.success('报名参与成功！')
          this.fromVisible = false
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
