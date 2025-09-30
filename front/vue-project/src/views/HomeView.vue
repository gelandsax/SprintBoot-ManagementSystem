<template>
  <el-container class="layout-container-demo">
    <el-header style="text-align: right; font-size: 12px; border-bottom: 1px solid white;">
      <div class="logo">企业人事管理平台</div>
      <div class="toolbar">
        <el-dropdown trigger="hover">
          <span class="el-dropdown-link">
            <el-icon class="userIcon">
              <UserFilled />
            </el-icon>
            <span class="username">{{ curUser?.username }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :icon="UserFilled">
                <RouterLink :to="`/userinfo/${curUser?.username}`" style="text-decoration: none; color: inherit;">查看个人信息
                </RouterLink>
              </el-dropdown-item>
              <el-dropdown-item :icon="Back" @click="logout()"> 退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-container class="layout-container-bottom">

      <el-aside width="200px">
        <el-scrollbar>
          <!-- 注意：保留el-menu的router属性（用于路由联动激活），删除默认active-text-color（避免冲突） -->
          <el-menu router background-color="#588adc" text-color="white" :default-openeds="['1', '3']">
            <!-- 首页导航项 -->
            <el-menu-item index="/main">
              <template #title>
                <el-icon>
                  <House />
                </el-icon>首页
              </template>
            </el-menu-item>
            <!-- 查看个人信息导航项 -->
            <el-menu-item :index="`/userinfo/${curUser?.username}`"> <!-- 动态绑定带参数的路径 -->
              <template #title>
                <el-icon>
                  <Document />
                </el-icon>
                查看个人信息 <!-- 移除内部的 RouterLink，避免冲突 -->
              </template>
            </el-menu-item>
            <!-- 其他导航项保持不变 -->
            <el-menu-item index="/update_login">
              <template #title>
                <el-icon>
                  <Key />
                </el-icon>修改登录密码
              </template>
            </el-menu-item>
            <el-sub-menu index="4">
              <template #title>
                <el-icon>
                  <Calendar />
                </el-icon>时间管理
              </template>
              <div class="menu">
                <el-menu-item index="/time/attend"> 考勤打卡</el-menu-item>
                <el-menu-item index="/time/attend_approval" v-if="curUser?.userType === 'admin' || curUser?.userType === '考勤审批人'"> 审批考勤补签</el-menu-item>
                <el-menu-item index="/time/holiday"> 申请休假</el-menu-item>
                <el-menu-item index="/time/holidaylist"> 休假记录</el-menu-item>
                <el-menu-item index="/time/holiday_approval" v-if="curUser?.userType === 'admin' || curUser?.userType === '请假审批人'"> 审批休假</el-menu-item>
                <el-menu-item index="/time/monthly_attendance"> 月度出勤记录</el-menu-item>
                <el-menu-item index="/time/calendar_setting" v-if="curUser?.userType === 'admin'"> 日历设定</el-menu-item>
                <el-menu-item index="/time/approver_setting" v-if="curUser?.userType === 'admin'"> 审批人设定</el-menu-item>
                <el-menu-item index="/time/schedule_setting" v-if="curUser?.userType === 'admin'"> 人员排班设定</el-menu-item>
                <el-menu-item index="/time/schedule_type_setting" v-if="curUser?.userType === 'admin'"> 排班类型设定</el-menu-item>
                <el-menu-item index="/time/holiday_type_setting" v-if="curUser?.userType === 'admin'"> 假期类型设定</el-menu-item>
              </div>
            </el-sub-menu>
            <el-menu-item index="/emp_info_manage" v-if="curUser?.userType === 'admin'">
              <template #title>
                <el-icon>
                  <User />
                </el-icon>员工信息管理
              </template>
            </el-menu-item>
            <el-menu-item index="/dept_info_manage" v-if="curUser?.userType === 'admin'">
              <template #title>
                <el-icon>
                  <Reading />
                </el-icon>部门信息管理
              </template>
            </el-menu-item>
            <el-menu-item index="/job_info_manage" v-if="curUser?.userType === 'admin'">
              <template #title>
                <el-icon>
                  <Suitcase />
                </el-icon>职位信息管理
              </template>
            </el-menu-item>
            <div class="menu-padding"></div>
          </el-menu>
        </el-scrollbar>
      </el-aside>
      <el-main class="flexPage">
        <RouterView></RouterView>
        <el-footer>
          <p style="color: aliceblue;">@Copyright:版权所有 抄袭或模仿必不究 制作者：王岩 李天悦 李静哲 何宁</p>
        </el-footer>
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
// 补充导入House图标（首页使用，原代码可能遗漏）
import { House, Menu as IconMenu, Key, Message, Setting, Timer, Calendar, Document, User, Reading, Suitcase } from '@element-plus/icons-vue'
import axios from '../utils/request';
import { ArrowDown, Back, UserFilled } from '@element-plus/icons-vue'


import router from '../router';

import { useCurUserStore } from '../stores/user'
import { storeToRefs } from 'pinia'
const store = useCurUserStore()
const { curUser } = storeToRefs(store)

const item = {
  date: '2016-05-02',
  name: 'Tom',
  address: 'No. 189, Grove St, Los Angeles',
}
const tableData = ref(Array.from({ length: 20 }).fill(item))

function logout() {
  store.userLogOut()
  router.push('login').then(() =>{
    window.location.reload();
  })
}
</script>

<style scoped>
.layout-container-demo {
  overflow: hidden;
  height: 100vh;
}

.layout-container-demo .layout-container-bottom {
  height: 100vh;
}

.layout-container-demo .el-header {
  display: flex;
  justify-content: space-between;
  position: relative;
  background-color: #588adc;
  color: #ffffff;
}

.layout-container-demo .el-aside {
  color: #ffffff;
  height: 100vh;
  background: #537aba;
}

/* 1. 基础菜单样式 - 减弱的金色阴影 */
.layout-container-demo .el-menu {
  height: 100vh;
  border-right: none;
  /* 基础阴影：比用户名更淡的金色效果 */
  text-shadow: 0 1px 3px rgba(255, 215, 0, 0.3);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-smoothing: antialiased;
}

/* 2. 菜单图标样式同步 - 减弱金色 */
:deep(.el-menu-item .el-icon),
:deep(.el-sub-menu .el-icon) {
  text-shadow: 0 1px 3px rgba(255, 215, 0, 0.3);
  transition: text-shadow 0.3s ease-out;
}

/* 3. 菜单项默认状态 - 更柔和的阴影 */
:deep(.el-menu-item) {
  text-shadow: 0 1px 3px rgba(255, 215, 0, 0.2);
  transition: all 0.3s ease-out;
  /* 统一过渡动画 */
}

/* 4. 菜单项悬停状态 - 减弱的金色扩散 */
:deep(.el-menu-item:hover) {
  background-color: rgba(30, 64, 175, 0.8) !important;
  color: #FFD700 !important;
  text-shadow:
    0 0 4px rgba(255, 255, 200, 0.5),
    /* 弱化的高光 */
    0 0 8px rgba(255, 215, 0, 0.4),
    /* 范围缩小的金色 */
    0 0 12px rgba(255, 180, 0, 0.2);
  /* 极淡的外层光晕 */
}

/* 5. 激活状态 - 比悬停稍强但弱于用户名效果 */
:deep(.el-menu-item.is-active) {
  color: #FFD700 !important;
  background-color: #1E40AF !important;
  border-radius: 5px;
  text-shadow:
    0 0 5px rgba(255, 255, 200, 0.6),
    0 0 10px rgba(255, 215, 0, 0.5),
    0 0 15px rgba(255, 180, 0, 0.3);
}

/* 6. 子菜单样式同步 */
:deep(.el-sub-menu .el-menu-item) {
  background-color: #4b6fe6 !important;
}

:deep(.el-sub-menu .el-menu-item:hover) {
  background-color: rgba(30, 64, 175, 0.7) !important;
}

/* 其他原有样式保持不变 */
.layout-container-demo .el-footer {
  margin-bottom: 30px;
  text-align: center;
}

.layout-container-demo .el-main {
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}

.menu-padding {
  height: 100px;
}

.logo {
  height: 30px;
  font-size: 27px;
  display: flex;
  /* 保留原垂直居中逻辑 */
  align-items: center;
  margin-top: 15px;
  font-weight: bold;
  /* 保留原有的金色阴影和抗锯齿效果，保持视觉统一 */
  text-shadow: 0 2px 8px rgba(255, 215, 0, 0.6);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-smoothing: antialiased;
}

.flexPage {
  background-color: #a1c5ff;
}

.el-dropdown-link .el-icon {
  margin-left: 5px;
  font-size: 20px;
  color: #ffffff;
}

.example-showcase .el-loading-mask {
  z-index: 9;
}

.menu {
  background-color: #4b6fe6;
}

.username {
  color: white;
  font-size: 16px;
  margin-right: 10px;
  text-shadow:
    0 2px 4px rgba(255, 215, 0, 0.4),
    0 0 8px rgba(255, 215, 0, 0.6);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  transition: text-shadow 0.3s ease-out;
}

.username:hover {
  text-shadow:
    0 0 6px rgba(255, 255, 200, 0.8),
    0 0 12px rgba(255, 215, 0, 0.8),
    0 0 20px rgba(255, 215, 0, 0.6),
    0 0 30px rgba(255, 180, 0, 0.4);
  filter: brightness(1.1);
  /* 轻微上浮效果增强交互感 */
  transform: translateY(-8px);
  /* 鼠标切换为手型 */
  cursor: pointer;
}

.userIcon {
  margin-right: 10px;
  margin-top: 5px;
  font-size: 20px;
  text-shadow: 0 2px 8px rgba(255, 215, 0, 0.6);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-smoothing: antialiased;
}
</style>