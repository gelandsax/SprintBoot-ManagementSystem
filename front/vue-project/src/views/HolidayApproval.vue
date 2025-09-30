<template>
  <div class="container">
    <div class="button-row">
      <el-button type="success" size="large" @click="batchApprove">批量同意</el-button>
      <el-button type="danger" size="large" @click="batchReject">批量驳回</el-button>
    </div>
    <div class="re-sign">
      <el-table 
        ref="multipleTableRef" 
        :data="pendingVacationRequests" 
        size="large" 
        stripe 
        style="width: 100%;"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="姓名" width="160" />
        <el-table-column prop="vocationType" label="休假类型" width="160" />
        <el-table-column prop="days" label="天数" width="160" />
        <el-table-column prop="startDate" label="开始时间" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="endDate" label="结束时间" width="200">
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
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" type="success" @click="approveItem(scope.row.id)">同意</el-button>
            <el-button size="small" type="danger" @click="rejectItem(scope.row.id)">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="demo-pagination-block">
      <el-pagination 
        v-model:current-page="currentPage" 
        v-model:page-size="pageSize" 
        :page-sizes="[5, 10, 20, 30]"
        layout="total, sizes, prev, pager, next, jumper" 
        :total="pendingVacationRequests.length" 
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" 
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GetAllVacationListRsp } from '@/models';
import axios from '../utils/request';
import { computed, inject, onMounted, ref } from 'vue';
import { ElMessage, type ElTable } from 'element-plus';

const setPageTitle = inject<(title: string) => void>('setPageTitle');

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
const getAllVacationList = ref<GetAllVacationListRsp>({
  vocationReqList: [{
    id: 0,
    username: '',
    vocationType: '',
    startDate: '',
    endDate: '',
    days: 0,
    reason: '',
    status: ''
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
    case '未审批':
      return 'warning';
    case '已拒绝':
      return 'danger';
    default:
      return 'info';
  }
}

// 待审批的休假请求
const pendingVacationRequests = computed(() => {
  return getAllVacationList.value.vocationReqList.filter(item => item.status === '未审批');
});

// 加载数据
const loadData = () => {
  axios.get('/vacation/AllRequestList').then(res => {
    console.log('休假审批数据:', res.data);
    getAllVacationList.value = res.data;
  }).catch(error => {
    console.error('获取休假审批数据失败:', error);
    ElMessage.error('获取休假审批数据失败');
  });
}

onMounted(() => {
  if (setPageTitle) {
    setPageTitle('审批休假');
  }
  loadData();
});

// 表格选择和批量操作
const multipleTableRef = ref<InstanceType<typeof ElTable>>();
const multipleSelection = ref<number[]>([]);

const handleSelectionChange = (selection: GetAllVacationListRsp['vocationReqList']) => {
  multipleSelection.value = selection.map(item => item.id);
}

// 单个审批操作
const approveItem = (id: number) => {
  axios.post('/vacationApprove/agree', {
    vacationRequestIds: [id]
  })
    .then(() => {
      ElMessage.success('审批同意成功');
      refreshData();
    })
    .catch((error) => {
      console.error('审批同意失败:', error);
      ElMessage.error('审批同意失败');
    })
}

const rejectItem = (id: number) => {
  axios.post('/vacationApprove/reject', {
    vacationRequestIds: [id]
  })
    .then(() => {
      ElMessage.success('审批驳回成功');
      refreshData();
    })
    .catch((error) => {
      console.error('审批驳回失败:', error);
      ElMessage.error('审批驳回失败');
    })
}

// 批量操作
const batchApprove = () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请至少选择一项进行批量操作');
    return;
  }

  axios.post('/vacationApprove/agreeAll', {
    vacationRequestIds: multipleSelection.value
  })
    .then(() => {
      ElMessage.success('批量同意成功');
      multipleTableRef.value?.clearSelection();
      refreshData();
    })
    .catch((error) => {
      console.error('批量同意失败:', error);
      ElMessage.error('批量同意失败');
    })
}

const batchReject = () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请至少选择一项进行批量操作');
    return;
  }

  axios.post('/vacationApprove/rejectAll', {
    vacationRequestIds: multipleSelection.value
  })
    .then(() => {
      ElMessage.success('批量驳回成功');
      multipleTableRef.value?.clearSelection();
      refreshData();
    })
    .catch((error) => {
      console.error('批量驳回失败:', error);
      ElMessage.error('批量驳回失败');
    })
}

// 刷新数据
const refreshData = () => {
  loadData();
}
</script>

<style scoped>
.container {
  background-color: #ffffff;
  margin: 20px;
  border-radius: 15px;
  padding: 20px;
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

.button-row {
  margin-bottom: 20px;
}

.re-sign {
  margin-bottom: 20px;
}

.demo-pagination-block {
  display: flex;
  justify-content: center;
  margin-top: 20px;
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

/* 按钮样式优化 */
.button-row .el-button {
  box-shadow:
    0 5px 15px rgba(0, 0, 0, 0.1),
    0 2px 5px rgba(0, 0, 0, 0.05) !important;
  transition: all 0.3s ease;
}

.button-row .el-button:hover {
  transform: translateY(-2px);
  box-shadow:
    0 8px 25px rgba(0, 0, 0, 0.15),
    0 3px 10px rgba(0, 0, 0, 0.1) !important;
}

/* 操作按钮样式 */
:deep(.el-table .el-button) {
  margin: 0 2px;
  box-shadow:
    0 2px 8px rgba(0, 0, 0, 0.1),
    0 1px 3px rgba(0, 0, 0, 0.05) !important;
  transition: all 0.3s ease;
}

:deep(.el-table .el-button:hover) {
  transform: translateY(-1px);
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.15),
    0 2px 5px rgba(0, 0, 0, 0.1) !important;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .container {
    padding: 10px;
    margin: 10px;
  }
  
  .button-row {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .button-row .el-button {
    width: 100%;
  }
}
</style>