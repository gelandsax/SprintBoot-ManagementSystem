<template>
  <el-calendar
    style="background-color: #ffffff;margin: 20px;border-radius: 8px;"
    :range="getCalendarRsp"
  >
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
</template>

<style>
.is-selected {
  color: #1989fa;
}

.date-cell {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.attendance-status {
  font-size: 12px;
  color: #666;
}
.rest-day-label {
  font-size: 12px;
  color: #ff4d4f;
  font-weight: bold;
}
</style>
<script setup lang="ts">
import type { GetWorkCalendarRsp } from '@/models';
import axios from '../utils/request';
import { inject, onMounted, ref } from 'vue';

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


onMounted(() => {
  if (setPageTitle) {
    setPageTitle('月度出勤记录')
  }
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  axios.get(`GetWorkCalendar/${year}`).then(res => {
    getCalendarRsp.value = res.data
  })
});
</script>
