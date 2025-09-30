<template>
  <div class="pageTitle">
    <div class="title">主页</div>
    <div class="subTitle"><el-icon>
        <HomeFilled />
      </el-icon> 首页</div>
  </div>
  <div class="container">
    <el-card>
      <template #header>
        <div class="card-header">
          <el-badge :value="todoCount" class="item" :offset="[10, 5]">
            <span><el-icon>
              <Management />
            </el-icon> 待办事项</span>
          </el-badge>
        </div>
      </template>
      <div v-if="getToDoRsp.todos && getToDoRsp.todos.length > 0">
    <p
      v-for="item in getToDoRsp.todos"
      :key="item.code"
      class="text item clickable-item"
      @click="navigateToPage(item.code)"
    >
      {{ translateCode(item.code) + '：' + item.count }}
    </p>
  </div>
  <div v-else class="empty-todo">
    <el-empty description="暂无待办事项" :image-size="80" />
  </div>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span><el-icon>
              <Operation />
            </el-icon> 每日出勤人数</span>
        </div>
      </template>
    </el-card>
  </div>

</template>
<script setup lang="ts">
import type { GetToDoRsp } from '@/models';
import axios from '../utils/request';
import { onMounted, ref } from 'vue';
import router from '../router';

// 定义路由映射类型
type RouteMapType = {
  Vacation_Approve: string;
  Attendance_Approve: string;
  Init_WorkCalendar: string;
  Change_Password: string;
  [key: string]: string; // 添加索引签名
};

// 定义翻译映射类型
type CodeToChineseMapType = {
  Vacation_Approve: string;
  Attendance_Approve: string;
  Init_WorkCalendar: string;
  Change_Password: string;
  [key: string]: string; // 添加索引签名
};

const todoCount = ref<number>(0);
const getToDoRsp = ref<GetToDoRsp>({
  todos: [{
    code: '',
    count: 0
  }]
});

onMounted(() => {
  axios.get('/ToDoList').then(res => {
    getToDoRsp.value = res.data
    let count = 0
    getToDoRsp.value.todos.forEach(item => {
      count += item.count
    })
    todoCount.value = count
  })
});

// 路由映射
const routeMap: RouteMapType = {
  'Vacation_Approve': '/time/holiday_approval',
  'Attendance_Approve': '/time/attend_approval',
  'Init_WorkCalendar': '/time/calendar_setting',
  'Change_Password': '/update_login',
};

const navigateToPage = (code: string) => {
  const route = routeMap[code];
  if (route) {
    router.push(route);
  }
};

// 代码翻译映射
const codeToChineseMap: CodeToChineseMapType = {
  'Vacation_Approve': '请假申请',
  'Attendance_Approve': '考勤申请',
  'Init_WorkCalendar': '日历设定',
  'Change_Password': '修改密码',
};

const translateCode = (code: string) => {
  return codeToChineseMap[code] || code;
};
</script>

<style scoped>
.container {
  padding: 20px;
  border-radius: 15px;
  background-color: rgb(133, 226, 200);
  margin: 10px 20px;
  padding: 5px 20px;
}

.container .el-card {
  margin: 20px 5px;
  border-radius: 15px;
}

/* 标题栏：深灰蓝 → 浅灰蓝（从左到右，视觉上有"收缩聚焦"感） */
.pageTitle .title {
  font-size: larger;
  font-weight: 800;
  padding: 10px 20px;
  border-top-left-radius: 12px;
  background-image: linear-gradient(to right, #3A639B, #7BA7E0); /* 深灰蓝→浅灰蓝 */
  background-size: 100% 100%;
  color: white; /* 加白色文字，与深色渐变对比更清晰（原样式可能漏了文字色） */
}

/* 副标题栏：浅蓝灰 → 淡青蓝（辅助色，比标题浅2个层级，不抢焦点） */
.pageTitle .subTitle {
  font-size: medium;
  font-weight: bold;
  padding: 10px 20px;
  border-bottom-left-radius: 12px;
  background-image: linear-gradient(to right, #E0E8F0, #B3C6E0); /* 浅蓝灰→淡青蓝 */
  background-size: 100% 100%;
  color: #333; /* 深灰文字，与浅色渐变搭配不刺眼 */
}

.clickable-item {
  cursor: pointer;
  transition: background-color 0.3s;
  padding: 5px;
  border-radius: 4px;
}

.clickable-item:hover {
  background-color: #f0f0f0;
  color: #9abcf4;
}

.empty-todo {
  text-align: center;
  padding: 20px 0;
}
</style>