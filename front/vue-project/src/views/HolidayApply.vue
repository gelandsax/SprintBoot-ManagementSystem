<template>
  <el-card class="container"  style="background-color: #ffffff;margin: 20px;border-radius: 15px;">
    <template #header>
      <div class="card-header">
        <span>填写休假信息</span>
      </div>
    </template>
    <el-form :model="postVacation" label-width="auto" style="max-width: 600px; justify-content: center;margin: 0 260px;">
      <el-form-item label="申请人">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="休假日期">
        <el-col :span="11">
          <el-date-picker v-model="postVacation.vacationReq.startDate" type="date" placeholder="年/月/日"
            style="width: 100%" />
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">至</span>
        </el-col>
        <el-col :span="11">
          <el-date-picker v-model="postVacation.vacationReq.endDate" type="date" placeholder="年/月/日"
            style="width: 100%" />
        </el-col>
      </el-form-item>
      <el-form-item label="假期类型">
        <el-select v-model="postVacation.vacationReq.vocationType" placeholder="请选择">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="可用天数">
        <span style="font-size: 18px; font-weight: bold; color: #409eff;">
          {{ remainingDays !== null ? remainingDays : '~' }}
        </span>
        <span> 天</span>
      </el-form-item>
      <el-form-item label="请假事由">
        <el-input v-model="postVacation.vacationReq.reason" type="textarea" :rows="8" resize="none" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="button">
        <el-button size="larger" type="primary" @click="onSubmit()">提交</el-button>
        <el-button size="larger" type="primary" @click="resetForm">重置</el-button>
      </div>
    </template>
  </el-card>
</template>
<script setup lang="ts">
import type { GetVacationRemainingReq, PostVacationReq } from '@/models';
import axios from '../utils/request';
import { inject, onMounted, reactive, ref, watch } from 'vue';
import { ElMessage } from 'element-plus';

const setPageTitle = inject<(title: string) => void>('setPageTitle')
onMounted(() => {
  if (setPageTitle) {
    setPageTitle('申请休假')
  }
});

const postVacation = ref<PostVacationReq>({
  vacationReq: {
    days: 0,
    endDate: '',
    reason: '',
    startDate: '',
    username: '',
    vocationType: ''
  }
})
const resetForm = () => {
  postVacation.value = {
    vacationReq: {
      days: 0,
      endDate: '',
      reason: '',
      startDate: '',
      username: '',
      vocationType: ''
    }
  }
  form.name = ''
  remainingDays.value = null
}
const remainingDays = ref<number | null>(null)
// do not use same name with ref
const form = reactive({
  name: '',
  region: '',
  startDate: '',
  endDate: '',
  delivery: false,
  type: [],
  resource: '',
  desc: '',
})

const formatDate = (date: Date | string): string => {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const onSubmit = () => {
  // Format dates to YYYY-MM-DD
  if (postVacation.value.vacationReq.startDate) {
    postVacation.value.vacationReq.startDate = formatDate(postVacation.value.vacationReq.startDate)
  }
  if (postVacation.value.vacationReq.endDate) {
    postVacation.value.vacationReq.endDate = formatDate(postVacation.value.vacationReq.endDate)
  }

  if (postVacation.value.vacationReq.startDate && postVacation.value.vacationReq.endDate) {
    const start = new Date(postVacation.value.vacationReq.startDate)
    const end = new Date(postVacation.value.vacationReq.endDate)
    const timeDiff = end.getTime() - start.getTime()
    const daysDiff = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1
    postVacation.value.vacationReq.days = daysDiff

    if (remainingDays.value !== null && daysDiff > remainingDays.value) {
      ElMessage({
        message: `申请天数(${daysDiff}天)超过可用天数(${remainingDays.value}天)`,
        type: 'warning',
      })
      return
    }
  }

  axios.post('/vacation/request', postVacation.value).then(res => {
    ElMessage({
      message: '提交成功',
      type: 'success',
    })
    resetForm()
  })
}
const getVacationRemainReq = ref<GetVacationRemainingReq>({
  vacationinfo: [{
    typename: '',
    limit: false,
    remaindays: 0,
    evbid: 0
  }]
})
const fetchRemainingDays = (vacationType: string) => {
  // 如果需要传递用户名或其他参数
  axios.get('/vacation').then(res => {
    getVacationRemainReq.value = res.data;
    const matchingVacation = getVacationRemainReq.value.vacationinfo.find(
      item => item.typename == vacationType
    );

    if (matchingVacation) {
      remainingDays.value = matchingVacation.remaindays;
    } else {
      remainingDays.value = null;
    }
  }).catch(err => {
    console.error('获取剩余天数失败:', err);
    remainingDays.value = null;
  });
};
watch(() => postVacation.value.vacationReq.vocationType, (newType) => {
  if (newType) {
    fetchRemainingDays(newType);
  } else {
    remainingDays.value = null;
  }
});

const options = [
  {
    value: '年假',
    label: '年假',
  },
  {
    value: '病假',
    label: '病假',
  },
  {
    value: '事假',
    label: '事假',
  },
  {
    value: '婚假',
    label: '婚假',
  },
  {
    value: '产假',
    label: '产假',
  },
  {
    value: '丧假',
    label: '丧假',
  },
]
</script>
<style scoped>
.container {
  background: #ffffff;
  margin: 20px;
  font-size: 20px;
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow: 
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.container .el-form-item {
  margin: 30px 0;
}

.card-header{
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}

.button{
  justify-content: center;
  max-width: 100px;
  margin: 0 350px 0 520px;
}
</style>
