<template>
  <el-calendar class="custom-calendar" :range="getCalendarRsp">
    <template #date-cell="{ data }">
      <div class="date-cell">
        <p :class="data.isSelected ? 'is-selected' : ''">
          {{ data.day.split('-').slice(1).join('-') }}
          {{ data.isSelected ? '✔️' : '' }}
        </p>
        <div
          class="rest-day-label"
          v-if="isRestDay(data.day)"
        >
          休
        </div>
      </div>
    </template>
  </el-calendar>
</template>

<style scoped>
.custom-calendar {
  background-color: #ffffff;
  margin: 20px;
  border-radius: 15px;
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow:
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.custom-calendar:hover {
  box-shadow:
    0 20px 50px rgba(0, 0, 0, 0.35),
    0 8px 20px rgba(0, 0, 0, 0.2) !important;
}

.is-selected {
  color: #1989fa;
}

.date-cell {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 5px;
}

.rest-day-label {
  font-size: 12px;
  color: #ff4d4f;
  font-weight: bold;
  background-color: #fff2f0;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
}

/* 日历样式优化 */
:deep(.el-calendar__header) {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-calendar__title) {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-calendar-day) {
  height: 80px;
  padding: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

:deep(.el-calendar-table thead th) {
  padding: 12px 0;
  color: #606266;
  font-weight: 600;
  background-color: #f8f9fa;
}

:deep(.el-calendar-table .el-calendar-day:hover) {
  background-color: #f5f7fa;
  cursor: pointer;
}

:deep(.el-calendar-table td.is-selected) {
  background-color: #f0f9ff;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .custom-calendar {
    margin: 10px;
  }
  
  :deep(.el-calendar__header) {
    padding: 15px;
    flex-direction: column;
    gap: 10px;
  }
  
  :deep(.el-calendar-day) {
    height: 60px;
    padding: 4px;
  }
}
</style>

<script setup lang="ts">
import type { GetWorkCalendarRsp } from '@/models';
import axios from '../utils/request';
import { inject, onMounted, ref } from 'vue';

const setPageTitle = inject<(title: string) => void>('setPageTitle');

// 日历数据
const getCalendarRsp = ref<GetWorkCalendarRsp>({
  days: 0,
  year: 0,
  workCalendar: [{
    date: '',
    dayType: ''
  }]
});

// 判断是否为休息日
const isRestDay = (date: string) => {
  // 查找对应的日期条目
  const dayEntry = getCalendarRsp.value.workCalendar.find(item => item.date === date);
  // 检查dayType是否为休息日
  return dayEntry ? dayEntry.dayType === '休息日' : false;
};

// 加载日历数据
const loadCalendarData = () => {
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  
  axios.get(`GetWorkCalendar/${year}`).then(res => {
    console.log('日历数据:', res.data);
    getCalendarRsp.value = res.data;
  }).catch(error => {
    console.error('获取日历数据失败:', error);
  });
};

onMounted(() => {
  if (setPageTitle) {
    setPageTitle('月度出勤记录');
  }
  loadCalendarData();
});
</script>