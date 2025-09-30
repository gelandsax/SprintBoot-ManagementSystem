<template>
  <el-card class="container">
    <template #header>
      <div class="card-header">
        <span style="font-weight: 800;">休假记录</span>
      </div>
    </template>
    
    <div class="search" style="font-size: 15px;">
      <span style="margin-left: 30px; font-weight: 700;">姓名：</span>
      <el-input v-model="searchParams.username" style="width: 240px" placeholder="请输入姓名" clearable />
      <el-button size="small" type="primary" style="margin-left: 50px;" @click="handleSearch">查找</el-button>
      <el-button size="small" style="margin-left: 10px;" @click="handleReset">重置</el-button>
    </div>
    
    <el-table 
      :data="filteredTableData" 
      :border="true" 
      size="large" 
      style="width: 97%; margin: 20px;"
      stripe
    >
      <el-table-column prop="username" label="姓名" width="120" />
      <el-table-column prop="vocationType" label="假期类型" width="120" />
      <el-table-column prop="days" label="时长(天)" width="100" />
      <el-table-column prop="startDate" label="开始时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.startDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="endDate" label="结束时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.endDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag 
            :type="getStatusType(scope.row.status)"
            effect="light"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="请假原因" />
    </el-table>
    
    <template #footer>
      <div class="demo-pagination-block">
        <el-pagination 
          v-model:current-page="currentPage" 
          v-model:page-size="pageSize" 
          :page-sizes="[5, 10, 20, 30]"
          layout="total, sizes, prev, pager, next, jumper" 
          :total="filteredTableData.length" 
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" 
        />
      </div>
    </template>
  </el-card>
</template>

<script setup lang="ts">
import type { GetMyVacationListRsp } from '@/models';
import axios from '../utils/request';
import { inject, onMounted, ref, computed } from 'vue';
import { useCurUserStore } from '../stores/user';
import { storeToRefs } from 'pinia';

const store = useCurUserStore();
const { curUser } = storeToRefs(store);

// 搜索参数
const searchParams = ref({
  username: ''
});

// 分页参数
const currentPage = ref(1);
const pageSize = ref(10);

const handleSizeChange = (val: number) => {
  pageSize.value = val;
  console.log(`${val} items per page`);
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  console.log(`current page: ${val}`);
}

// 主要数据
const getMyHolidaysList = ref<GetMyVacationListRsp>({
  vocationReqList: [{
    id: 0,
    username: '',
    vocationType: '',
    startDate: '',
    endDate: '',
    days: 0,
    status: '',
    reason: '',
  }]
});

// 日期格式化函数
const formatDate = (dateString: string) => {
  if (!dateString) return '';
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN');
  } catch (e) {
    return dateString.split('T')[0]; // 如果解析失败，尝试简单分割
  }
}

// 状态标签类型
const getStatusType = (status: string) => {
  switch (status) {
    case '已批准':
      return 'success';
    case '待审批':
      return 'warning';
    case '已拒绝':
      return 'danger';
    default:
      return 'info';
  }
}

// 搜索功能
const handleSearch = () => {
  currentPage.value = 1; // 搜索时回到第一页
}

const handleReset = () => {
  searchParams.value.username = '';
  currentPage.value = 1;
}

// 过滤后的表格数据
const filteredTableData = computed(() => {
  let data = getMyHolidaysList.value.vocationReqList;
  
  // 根据用户名过滤
  if (searchParams.value.username) {
    data = data.filter(item => 
      item.username.toLowerCase().includes(searchParams.value.username.toLowerCase())
    );
  }
  
  return data;
});

// 加载数据
const loadData = () => {
  const apiUrl = curUser.value?.userType === 'admin' 
    ? 'vacation/AllRequestList' 
    : 'vacation/myRequestList'; // 如果是普通用户，可能需要不同的接口
  
  axios.get(apiUrl).then(res => {
    console.log('休假记录数据:', res.data);
    getMyHolidaysList.value = res.data;
  }).catch(error => {
    console.error('获取休假记录失败:', error);
  });
}

const setPageTitle = inject<(title: string) => void>('setPageTitle');

onMounted(() => {
  if (setPageTitle) {
    setPageTitle('休假记录');
  }
  loadData();
});
</script>

<style scoped>
.container {
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

.container:hover {
  box-shadow: 
    0 20px 50px rgba(0, 0, 0, 0.35),
    0 8px 20px rgba(0, 0, 0, 0.2) !important;
}

.search {
  margin: 20px 0;
  padding: 0 20px;
}

.demo-pagination-block {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

/* 表格样式优化 */
:deep(.el-table) {
  box-shadow:
    0 5px 15px rgba(0, 0, 0, 0.1),
    0 2px 5px rgba(0, 0, 0, 0.05) !important;
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
  text-align: center;
}

:deep(.el-table td) {
  text-align: center;
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 搜索区域样式 */
.search .el-input {
  margin-left: 10px;
}

.search .el-button {
  box-shadow:
    0 3px 10px rgba(0, 0, 0, 0.1),
    0 1px 3px rgba(0, 0, 0, 0.05) !important;
  transition: all 0.3s ease;
}

.search .el-button:hover {
  transform: translateY(-1px);
  box-shadow:
    0 5px 15px rgba(0, 0, 0, 0.15),
    0 2px 5px rgba(0, 0, 0, 0.1) !important;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .container {
    margin: 10px;
  }
  
  .search {
    display: flex;
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
  
  .search .el-input {
    width: 100% !important;
    margin-left: 0;
  }
  
  .search .el-button {
    margin-left: 0 !important;
  }
}
</style>