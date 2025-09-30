<template>
  <div class="pageTitle">
    <div class="title">查看个人信息</div>
    <div class="subTitle">
      <el-icon>
        <HomeFilled />
      </el-icon>
      <RouterLink :to="'/main'" style="text-decoration: none; color: black;"> 首页 </RouterLink>
      <span style="color: gray;">
        <el-icon>
          <ArrowRight />
        </el-icon> 查看个人信息
      </span>
    </div>
  </div>

  <div class="container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>基础信息</span>
        </div>
      </template>

      <div class="card-body">
        <div class="info-container">
          <div class="info-column left-column">
            <div class="info-item">
              <label class="info-label">员工姓名：</label>
              <span class="info-content">{{ userprofile.profile.emp_name || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">入职日期：</label>
              <span class="info-content">{{ formatDate(userprofile.profile.hireDate) || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">所属部门：</label>
              <span class="info-content">{{ userprofile.profile.department || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">婚姻状态：</label>
              <span class="info-content">{{ userprofile.profile.maritalStatus || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">首次入职：</label>
              <span class="info-content">{{ formatDate(userprofile.profile.firstWorkDate) || '--' }}</span>
            </div>
          </div>
          <div class="info-divider"></div>
          <div class="info-column right-column">
            <div class="info-item">
              <label class="info-label">性别：</label>
              <span class="info-content">{{ userprofile.profile.gender || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">生日：</label>
              <span class="info-content">{{ formatDate(userprofile.profile.birthday) || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">职位：</label>
              <span class="info-content">{{ userprofile.profile.job || '--' }}</span>
            </div>
            <div class="info-item">
              <label class="info-label">邮箱：</label>
              <span class="info-content">{{ userprofile.profile.email || '--' }}</span>
            </div>
          </div>
        </div>
        <div class="avatar-group">
          <div class="avatar-wrapper">
            <img src="https://picsum.photos/200/300" alt="个人头像" class="avatar-img">
          </div>
        </div>
      </div>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header"><span>部门履历信息</span></div>
      </template>
      <el-table :data="userprofile.profile.departmentHistory" :border="true" size="large" style="width: 100%">
        <el-table-column prop="username" label="员工姓名" width="180" />
        <el-table-column prop="old_department" label="部门名称" width="180" />
        <el-table-column prop="start_date" label="开始日期" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.start_date) || '--' }}
          </template>
        </el-table-column>
        <el-table-column prop="end_date" label="结束日期">
          <template #default="scope">
            {{ formatDate(scope.row.end_date) || '--' }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header"><span>职位履历信息</span></div>
      </template>
      <el-table :data="userprofile.profile.jobHistory" :border="true" size="large" style="width: 100%">
        <el-table-column prop="username" label="员工姓名" width="180" />
        <el-table-column prop="old_job" label="职位名称" width="180" />
        <el-table-column prop="start_date" label="开始日期" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.start_date) || '--' }}
          </template>
        </el-table-column>
        <el-table-column prop="end_date" label="结束日期">
          <template #default="scope">
            {{ formatDate(scope.row.end_date) || '--' }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useCurUserStore } from '../stores/user';
import { storeToRefs } from 'pinia';
import { HomeFilled, ArrowRight } from '@element-plus/icons-vue';
import axios from '../utils/request'
import type { GetProfileRsp } from '@/models'
import { useRoute } from 'vue-router'

const store = useCurUserStore();
const { curUser } = storeToRefs(store);

let userprofile = ref<GetProfileRsp>({
  profile: {
    birthday: '',
    department: '',
    departmentHistory: [],
    email: '',
    firstWorkDate: '',
    gender: '',
    hireDate: '',
    job: '',
    jobHistory: [],
    maritalStatus: '',
    user_name: '',
    emp_name: '',
    id: 0,
    permission: '',
    salary: ''
  }
})

function formatDate(dateString: string) {
  return dateString ? dateString.split('T')[0] : ''
}

let route = useRoute();

onMounted(() => {
  axios.get(`profiles/${route.params.name}`).then(res => {
    userprofile.value = res.data
  })
})
</script>

<style scoped>
/* 容器样式 */
.container {
  padding: 5px 20px;
  background-color: rgb(133, 226, 200);
  margin: 10px 20px;
  border-radius: 15px;
}

/* 卡片样式 */
.container .el-card {
  margin: 20px 0;
  border: none;
  border-radius: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 页面标题样式 */
.pageTitle .title {
  font-size: larger;
  font-weight: 800;
  padding: 10px 20px;
  border-top-left-radius: 12px;
  background-image: linear-gradient(to right, #3A639B, #7BA7E0);
  background-size: 100% 100%;
  color: white;
}

.pageTitle .subTitle {
  font-size: medium;
  font-weight: bold;
  padding: 10px 20px;
  border-bottom-left-radius: 12px;
  background-image: linear-gradient(to right, #E0E8F0, #B3C6E0);
  background-size: 100% 100%;
  color: #333;
}

.card-header {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

/* 核心：双列信息容器布局 */
.card-body {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 30px;
  padding: 10px 10px;
}

/* 双列信息外层容器：控制左/中/右排列 */
.info-container {
  display: flex;
  align-items: flex-start;
  flex: 1;
}

/* 左右列通用样式：等宽分布 */
.info-column {
  width: 360px;
  display: flex;
  margin-top: 10px;
  flex-direction: column;
  gap: 25px;
}

/* 左侧列：单独控制，确保与右侧列对齐 */
.left-column {
  padding-right: 5px;
}

/* 右侧列：单独控制 */
.right-column {
  padding-left: 5px;
}

/* 中间分隔线：浅灰色竖线 */
.info-divider {
  width: 1px;
  background-color: #424242;
  height: auto;
  margin-right: -80px;
}

/* 单条信息：标签+内容横向排列 */
.info-item {
  display: flex;
  align-items: center;
  font-size: 18px;
}

/* 标签样式：固定宽度+右对齐+加粗 */
.info-label {
  width: 140px;
  text-align: right;
  font-weight: 600;
  color: #666;
  margin-right: 20px;
}

/* 内容样式：放大字体，提升可读性 */
.info-content {
  color: #333;
  line-height: 1.6;
  font-size: 16px;
}

/* 头像区域样式 */
.avatar-group {
  margin-right: 20px;
}

.avatar-wrapper {
  width: 200px;
  height: 260px;
  border-radius: 12px;
  overflow: hidden;
  border: 3px solid #4FB5D3;
  box-shadow: 0 6px 16px rgba(5, 5, 5, 0.7);
  padding: 6px;
  background-color: #ffffff;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

/* 响应式适配：小屏幕恢复单列 */
@media (max-width: 768px) {
  .card-body {
    flex-direction: column;
    gap: 30px;
    align-items: center;
    padding: 20px;
  }

  .info-container {
    flex-direction: column;
    align-items: center;
    width: 100%;
    max-width: 450px;
  }

  .info-column {
    width: 100%;
    gap: 18px;
    padding: 0;
  }

  .info-divider {
    display: none;
  }

  .info-label {
    width: 120px;
    font-size: 15px;
  }

  .info-content {
    font-size: 15px;
  }

  .avatar-wrapper {
    width: 180px;
    height: 240px;
  }
}
</style>