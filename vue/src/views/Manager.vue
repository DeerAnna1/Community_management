<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />

        <div class="title">后台管理系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
<!--            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />-->
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.name ||  '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <!-- 默认打开  -->
        <el-menu :collapse="isCollapse" :default-openeds="['info','days','user']" router style="border: none" :default-active="$route.path">

          <el-menu-item index="/home">
            <i class="el-icon-s-marketing"></i>
            <span slot="title">信息统计</span>
          </el-menu-item>

          <el-submenu index="info">
            <template slot="title">
              <i class="el-icon-s-cooperation"></i><span>信息管理</span>
            </template>
            <el-menu-item index="/department">社团信息</el-menu-item>
          </el-submenu>

          <el-submenu index="days">
            <template slot="title">
              <i class="el-icon-s-order"></i><span>日常管理</span>
            </template>
            <el-menu-item index="/apply">申请审批</el-menu-item>
            <el-menu-item index="/member">社团成员</el-menu-item>
            <el-menu-item index="/activity">社团活动</el-menu-item>
            <el-menu-item index="/participation">参与活动成员</el-menu-item>
            <el-menu-item index="/comment">社团活动评论</el-menu-item>

          </el-submenu>

          <el-submenu index="user" v-if="user.role === 'ADMIN'">
            <template slot="title">
              <i class="el-icon-s-custom"></i><span>用户管理</span>
            </template>
            <el-menu-item index="/admin">管理员信息</el-menu-item>
            <el-menu-item index="/user">学生信息</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      isCollapse: false
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('user') || '{}')   // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/adminPerson')
      }
      if (this.user.role === 'USER') {
        this.$router.push('/userPerson')
      }
    },
    logout() {
      localStorage.removeItem('user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
@import "../assets/css/manager.css";
</style>