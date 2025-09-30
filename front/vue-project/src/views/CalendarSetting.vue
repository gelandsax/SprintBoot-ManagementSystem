<template>
  <el-dialog v-model="calendarDialogVisible" title="选择年份" width="800">
    <el-calendar :range="calendarRange" v-model="value">
      <template #date-cell="{ data }">
        <div class="date-cell" @click="handleDateClick(data.day)">
          <p :class="data.isSelected ? 'is-selected' : ''">
            {{ data.day.split('-').slice(2).join('-') }}
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
    <template #footer>
      <el-button @click="calendarDialogVisible = false">关闭</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="yearSelectVisible" title="选择年份" width="300">
    <el-select v-model="selectedYear" placeholder="请选择年份">
      <el-option
        v-for="item in availableYears"
        :key="item"
        :label="item"
        :value="item"
      />
    </el-select>
    <template #footer>
      <el-button @click="yearSelectVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmYearSelection">确定</el-button>
    </template>
  </el-dialog>

  <div class="container">
    <el-calendar :range="getCalendarRsp">
      <template #date-cell="{ data }">
        <div class="date-cell">
          <p :class="data.isSelected ? 'is-selected' : ''">
            {{ data.day.split('-').slice(2).join('-') }}
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
    <div class="setting-button-container">
      <el-button class="setting-button" @click="openYearSelector">设置假期</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GetWorkCalendarRsp } from '@/models';
import axios from '../utils/request';
import { computed, inject, onMounted, ref, watch } from 'vue';

const yearSelectVisible = ref(false)
const selectedYear = ref(new Date().getFullYear())
const value = ref(new Date())

// Generate available years (current year and next 3 years)
const availableYears = computed(() => {
  const currentYear = new Date().getFullYear()
  return [currentYear, currentYear + 1, currentYear + 2, currentYear + 3]
})

// Method to handle "设置假期" button click
const openYearSelector = () => {
  yearSelectVisible.value = true
}

// Method to confirm year selection
const confirmYearSelection = () => {
  yearSelectVisible.value = false
  // Set the calendar range to the selected year
  calendarRange.value = {
    start: new Date(selectedYear.value, 0, 1),
    end: new Date(selectedYear.value, 11, 31)
  }
  // Set the calendar view to the first month of the selected year
  value.value = new Date(selectedYear.value, 0, 1)
  // Show the calendar dialog
  calendarDialogVisible.value = true
  // Load calendar data
  loadCalendarData(selectedYear.value)
}

// 检查日历数据是否完整
const isCalendarComplete = (year: number, days: number) => {
  // Leap year calculation
  const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
  const expectedDays = isLeapYear ? 366 : 365;
  return days >= expectedDays;
};

// 加载日历数据
const loadCalendarData = (year: number) => {
  axios.get(`GetWorkCalendar/${year}`).then(res => {
    // Check if the calendar data is complete based on leap year calculation
    if (!isCalendarComplete(year, res.data.days)) {
      // Calendar data is incomplete, initialize it
      axios.post(`InitWorkCalendar/${year}`).then(() => {
        axios.get(`GetWorkCalendar/${year}`).then(res => {
          getCalendarRsp.value = res.data
        })
      })
    } else {
      // Calendar data is complete, use it directly
      getCalendarRsp.value = res.data
    }
  }).catch(() => {
    // If get request fails, initialize the calendar
    axios.post(`InitWorkCalendar/${year}`).then(() => {
      axios.get(`GetWorkCalendar/${year}`).then(res => {
        getCalendarRsp.value = res.data
      })
    })
  })
}

const setPageTitle = inject<(title: string) => void>('setPageTitle')
const getCalendarRsp = ref<GetWorkCalendarRsp>({
  days: 0,
  year: 0,
  workCalendar: [{
    date: '',
    dayType: ''
  }]
});

const isRestDay = (date: string) => {
  // Find the corresponding date entry in workCalendar array
  const dayEntry = getCalendarRsp.value.workCalendar.find(item => item.date === date);
  // Check if the dayType indicates a rest day
  return dayEntry ? dayEntry.dayType === '休息日' : false;
};

const calendarDialogVisible = ref(false)
const calendarRange = ref<{ start: Date, end: Date }>({
  start: new Date(new Date().getFullYear(), 0, 1),
  end: new Date(new Date().getFullYear(), 11, 31)
})

const handleDateClick = (date: string) => {
  const dayEntry = getCalendarRsp.value.workCalendar.find(item => item.date === date);
  const datechange = dayEntry && dayEntry.dayType === '休息日' ? '工作日' : '休息日';
  axios.post('ConfigWorkCalendar', {}, {
    params: {
      date: date,
      day_type: datechange
    }
  }).then(() => {
    loadCalendarData(selectedYear.value);
  });
};

// 监听日历对话框关闭，刷新数据
watch(calendarDialogVisible, (newVisible: boolean) => {
  if (!newVisible) {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    axios.get(`GetWorkCalendar/${year}`).then(res => {
      getCalendarRsp.value = res.data
    })
  }
})

onMounted(() => {
  if (setPageTitle) {
    setPageTitle('日历设定')
  }
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  axios.get(`GetWorkCalendar/${year}`).then(res => {
    getCalendarRsp.value = res.data
  })
});
</script>

<style scoped>
.container {
  background-color: #ffffff;
  margin: 20px;
  border-radius: 15px;
  text-align: center;
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow:
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.container:hover {
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
  cursor: pointer;
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

.setting-button {
  margin-top: 20px;
  color: #165ba0;
  background-color: rgb(255, 255, 255);
  height: 40px;
  width: 100px;
  font-size: 15px;
  border: 2px solid #165ba0;
  border-radius: 20px;
  transition: all 0.5s;
  box-shadow:
    0 5px 15px rgba(0, 0, 0, 0.1),
    0 2px 5px rgba(0, 0, 0, 0.05) !important;
}

.setting-button:hover {
  background-color: rgb(103, 105, 224);
  font-weight: bold;
  color: white;
  border: 2px solid rgb(103, 105, 224);
  transform: translateY(-2px);
  box-shadow:
    0 8px 25px rgba(0, 0, 0, 0.15),
    0 3px 10px rgba(0, 0, 0, 0.1) !important;
}

.setting-button-container {
  padding-bottom: 20px;
}

/* 日历样式优化 */
:deep(.el-calendar) {
  margin: 20px;
  border-radius: 15px;
  background-color: #ffffff;
}

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
}

:deep(.el-calendar-table td.is-selected) {
  background-color: #f0f9ff;
}

/* 对话框样式优化 */
:deep(.el-dialog) {
  border-radius: 15px;
  box-shadow:
    0 20px 60px rgba(0, 0, 0, 0.3),
    0 8px 25px rgba(0, 0, 0, 0.15) !important;
}

:deep(.el-dialog__header) {
  background-color: #f5f7fa;
  border-radius: 15px 15px 0 0;
  padding: 20px;
}

:deep(.el-dialog__title) {
  font-weight: 600;
  color: #303133;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .container {
    margin: 10px;
  }
  
  :deep(.el-calendar) {
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
  
  :deep(.el-dialog) {
    width: 95% !important;
    margin: 10px auto;
  }
}
</style>