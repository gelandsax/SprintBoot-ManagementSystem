<template>
  <div class="container">
    <div class="button-row">
      <el-button type="success" size="large" @click="batchApprove">批量同意</el-button>
      <el-button type="danger" size="large" @click="batchReject">批量驳回</el-button>
    </div>
    <div class="re-sign">
      <el-table 
        ref="multipleTableRef" 
        :data="getAllBQApproveListRsp.bqapproveListVo" 
        size="large" 
        stripe
        style="width: 100%" 
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="姓名" width="120" />
        <el-table-column prop="workdate" label="日期" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.workdate) }}
          </template>
        </el-table-column>
        <el-table-column prop="check_in" label="上班打卡" width="120">
          <template #default="scope">
            {{ formatTime(scope.row.check_in) }}
          </template>
        </el-table-column>
        <el-table-column prop="check_out" label="下班打卡" width="120">
          <template #default="scope">
            {{ formatTime(scope.row.check_out) }}
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="补签原因" />
        <el-table-column prop="status" label="状态" />
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
        :total="getAllBQApproveListRsp.bqapproveListVo.length" 
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" 
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GetBQApproveListRsp } from '@/models';
import axios from '../utils/request';
import { inject, onMounted, ref } from 'vue';
import { ElMessage, type ElTable } from 'element-plus'

const setPageTitle = inject<(title: string) => void>('setPageTitle')

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

const handleSizeChange = (val: number) => {
  pageSize.value = val
  console.log(`${val} items per page`)
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  console.log(`current page: ${val}`)
}

// 数据格式化函数
const formatDate = (dateString: string) => {
  if (!dateString) return '';
  return dateString.split('T')[0];
}

const formatTime = (timeObj: any) => {
  if (!timeObj) return '--';
  if (typeof timeObj === 'string') return timeObj;
  
  // 处理时间对象
  const hours = String(timeObj.hour || 0).padStart(2, '0');
  const minutes = String(timeObj.minute || 0).padStart(2, '0');
  const seconds = String(timeObj.second || 0).padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
}

// 主要数据
const getAllBQApproveListRsp = ref<GetBQApproveListRsp>({
  bqapproveListVo: [{
    check_in: {
      hour: 0,
      minute: 0,
      nano: 0,
      second: 0
    },
    check_out: {
      hour: 0,
      minute: 0,
      nano: 0,
      second: 0
    },
    id: 0,
    reason: '',
    status: '',
    username: '',
    workdate: ''
  }]
})

// 加载数据
const loadData = () => {
  axios.get('/attendance/BQApprove').then(res => {
    getAllBQApproveListRsp.value = res.data
  }).catch(error => {
    console.error('加载审批数据失败:', error)
    ElMessage.error('加载审批数据失败')
  })
}

onMounted(() => {
  if (setPageTitle) {
    setPageTitle('审批考勤补签')
  }
  loadData()
});

// 表格选择和批量操作
const multipleTableRef = ref<InstanceType<typeof ElTable>>()
const selectedItems = ref<any[]>([]) // 改为存储完整的选择项

// 处理选择变化 - 修复后的版本
const handleSelectionChange = (selection: any[]) => {
  selectedItems.value = selection;
  console.log('选中的项目:', selectedItems.value);
}

// 单个审批操作
const approveItem = (id: number) => {
  axios.post('attendance/BQApprove/agree', {
    attendanceRetroactiveIds: [id]
  })
    .then(() => {
      ElMessage.success('审批同意成功')
      refreshData()
    })
    .catch((error) => {
      console.error('审批同意失败:', error)
      ElMessage.error('审批同意失败')
    })
}

const rejectItem = (id: number) => {
  axios.post('/attendance/BQApprove/reject', {
    attendanceRetroactiveIds: [id]
  })
    .then(() => {
      ElMessage.success('审批驳回成功')
      refreshData()
    })
    .catch((error) => {
      console.error('审批驳回失败:', error)
      ElMessage.error('审批驳回失败')
    })
}

// 批量操作 - 修复后的版本
const batchApprove = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请至少选择一项进行批量操作')
    return
  }

  // 提取所有选中项的ID
  const selectedIds = selectedItems.value.map(item => item.id);
  console.log('批量同意的ID:', selectedIds);

  axios.post('/attendance/BQApprove/agreeAll', {
    attendanceRetroactiveIds: selectedIds
  })
    .then(() => {
      ElMessage.success(`批量同意成功，共处理 ${selectedIds.length} 条申请`)
      multipleTableRef.value?.clearSelection()
      refreshData()
    })
    .catch((error) => {
      console.error('批量同意失败:', error)
      ElMessage.error('批量同意失败')
    })
}

const batchReject = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请至少选择一项进行批量操作')
    return
  }

  // 提取所有选中项的ID
  const selectedIds = selectedItems.value.map(item => item.id);
  console.log('批量驳回的ID:', selectedIds);

  axios.post('/attendance/BQApprove/rejectAll', {
    attendanceRetroactiveIds: selectedIds
  })
    .then(() => {
      ElMessage.success(`批量驳回成功，共处理 ${selectedIds.length} 条申请`)
      multipleTableRef.value?.clearSelection()
      refreshData()
    })
    .catch((error) => {
      console.error('批量驳回失败:', error)
      ElMessage.error('批量驳回失败')
    })
}

// 刷新数据
const refreshData = () => {
  loadData()
  // 清空选择
  selectedItems.value = [];
}
</script>

<style scoped>
.container {
  padding: 20px;
  margin: 20px;
  background: #ffffff;
  border-radius: 15px;
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow: 
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.button-row {
  margin-bottom: 20px;
}

.re-sign {
  margin-bottom: 20px;
}

.demo-pagination-block {
  margin-top: 20px;
  display: flex;
  justify-content: center;
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
}

:deep(.el-table .el-button) {
  margin: 0 2px;
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