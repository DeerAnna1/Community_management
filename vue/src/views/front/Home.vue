<template>
  <div class="main-content">
    <div style="width: 70%; margin: 20px auto">
      <!--   轮播图   -->
      <el-carousel height="300px" style="border-radius: 10px">
        <el-carousel-item v-for="item in carousel" :key="item">
          <img :src="item" alt="" style="width: 100%; height: 300px; border-radius: 10px">
        </el-carousel-item>
      </el-carousel>

      <div>
        <div style="margin: 20px 0 0 0; width: 130px; background-color: #18cbfd; height: 30px; line-height: 30px; text-align: center; font-size: 18px; color: white; font-weight: bold; border-radius: 20px">全部社团</div>

        <div style="margin-top: 15px">
          <el-row :gutter="20">
            <el-col :span="5" v-for="item in departmentData">
              <img :src="item.img" alt="" style="border:1px solid #cccccc;
              width: 100%; height: 166px; border-radius: 50%"
                   @click="navTo('/front/departmentDetail?id=' + item.id)">
              <div style="text-align: center; margin-top: 10px; font-size: 17px">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>

      </div>
      <div style="margin: 20px 0 20px 0; width: 130px; background-color: #18cbfd; height: 30px; line-height: 30px; text-align: center; font-size: 18px; color: white; font-weight: bold; border-radius: 20px">社团活动</div>
        <div style="margin: 20px 0 20px 0" v-for="item in activityData">
          <el-row :guter="40">
            <el-col :span="3.5">
              <img :src="item.img" alt="" style="width: 100%; height:120px;border-radius: 10px">
            </el-col>
            <el-col :span="16">
              <div style="margin: 7px 0 0 0;padding:0 10px;font-weight: 600;font-size: 18px;color: #404040;
              white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
                <a href="#" @click="navTo('/front/activityDetail?id='+item.id)">{{item.name}}</a>
              </div>
              <div style="margin: 10px 0 0 0;padding:0 10px;font-size: 15px;color: #404040;overflow :hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp:3;">{{item.description}}</div>
            </el-col>
            <el-col :span="5.5">
              <div style="height: 80px"></div>
              <div style="text-align: right;padding:0 15px;font-size: 15px;color: #8d8a8a;">{{item.time}}</div>
            </el-col>
          </el-row>
        </div>
<!-- 分页     -->
        <div style="text-align: right">
          <div class="pagination">
            <el-pagination
                background
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-size="pageSize"
                layout="prev, next"
                :total="total">
            </el-pagination>
          </div>
        </div>
    </div>

  </div>
</template>

<script>

export default {

  data() {
    return {
      carousel: [
        require('@/assets/imgs/lun-1.png'),
        require('@/assets/imgs/lun-2.png'),
        require('@/assets/imgs/lun-3.png'),
      ],
      departmentData: [],
      activityData: [],
      pageNum:1,//当前的页码
      pageSize:2,//每页显示的数目
      total:0
    }
  },
  mounted() {
    this.loadDepartment()
    this.loadActivity()
  },
  methods: {
    loadActivity(pageNum){
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/activity/selectPage2', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if (res.code === '200') {
          this.activityData = res.data?.list
          // console.log(res.data?.list)
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    navTo(url) {
      location.href = url
    },
    handleCurrentChange(pageNum){
      this.loadActivity(pageNum)
    }
  }
}
</script>
<style scoped>
.el-col-5{
  width: 20%;
  max-width: 20%;
  padding: 10px 10px;
}
</style>

