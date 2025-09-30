<template>
  <el-dialog v-model="dialogFormVisible" title="新增部门" width="600">
    <el-form :model="addDepartment" label-position="left" label-width="120px">
      <el-form-item label="部门名称" required>
        <el-input v-model="addDepartment.deptName" autocomplete="off" />
      </el-form-item>

      <el-form-item label="部门编制" required>
        <el-input v-model.number="addDepartment.deptSize" autocomplete="off" type="number" />
      </el-form-item>

      <el-form-item label="上级部门" required>
        <el-input v-model="addDepartment.fatherDeptName" autocomplete="off" />
      </el-form-item>

      <el-form-item label="负责人ID" required>
        <el-input v-model.number="addDepartment.leaderId" autocomplete="off" type="number" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="editDialogVisible" title="修改部门" width="600">
    <el-form :model="editForm" label-position="left" label-width="120px">
      <el-form-item label="部门名称" required>
        <el-input v-model="editForm.deptName" />
      </el-form-item>

      <el-form-item label="部门编制" required>
        <el-input v-model.number="editForm.deptSize" type="number" />
      </el-form-item>

      <el-form-item label="上级部门" required>
        <el-input v-model="editForm.fatherDeptName" />
      </el-form-item>

      <el-form-item label="负责人ID" required>
        <el-input v-model.number="editForm.leaderId" type="number" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditForm">
          提交
        </el-button>
      </div>
    </template>
  </el-dialog>
  
  <div class="pageTitle">
    <div class="title">部门信息</div>
    <div class="subTitle">
      <el-icon>
        <HomeFilled />
      </el-icon>
      <RouterLink :to="'/main'" style="text-decoration: none; color: black;"> 首页</RouterLink> 
      <span style="color: gray;">
        <el-icon>
          <ArrowRight />
        </el-icon> 部门信息
      </span>
    </div>
  </div>

  <div class="container">
    <el-card>
      <template #header>
        <div class="block">
          <span class="content">部门名称：</span>
          <el-input v-model="searchParams.deptName" placeholder="请输入" style="height: 24px;" />
          <span class="content">状态：</span>
          <el-select v-model="searchParams.status" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <div class="selectButton">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button type="primary" @click="handleReset">重置</el-button>
          </div>
        </div>
      </template>

      <el-button type="primary" style="margin-bottom: 15px;" @click="dialogFormVisible = true">新增</el-button>
      <el-table :border="true" :data="filteredTableData" stripe style="width: 100%">
        <el-table-column prop="deptId" label="编号" width="130px" />
        <el-table-column prop="deptName" label="部门名称" width="180px" />
        <el-table-column prop="deptLeaderName" label="负责人姓名" width="180px" />
        <el-table-column prop="deptQuota" label="部门编制" width="180px" />
        <el-table-column prop="status" label="状态" width="180px" />
        <el-table-column label="操作" width="400px">
          <template #default="scope">
            <el-button class="doButton1" @click.prevent="viewDepartment(scope.row.deptId)">
              <el-icon>
                <View />
              </el-icon>查看
            </el-button>
            <el-button class="doButton2" @click.prevent="editDepartment(scope.row)">
              <el-icon>
                <Refresh />
              </el-icon>修改
            </el-button>
            <el-button class="doButton3" @click.prevent="updateStatus(scope.row)">
              <el-icon>
                <SemiSelect />
              </el-icon>{{ scope.row.status === '启用' ? '撤销' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="middle">
        <div class="demo-pagination-block">
          <div class="page"></div>
          <el-pagination size="small" v-model:current-page="currentPage" v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 40]" layout="total, sizes, prev, pager, next, jumper" 
            :total="filteredTableData.length" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ref, computed, onMounted } from 'vue'

const router = useRouter()

// 对话框控制
const dialogFormVisible = ref(false)
const editDialogVisible = ref(false)

// 假数据
const mockDepartmentData = [
  {
    deptId: 1,
    deptName: '技术部',
    deptLeaderName: '张三',
    deptQuota: 50,
    status: '启用',
    fatherDeptName: '无',
    leaderId: 1001
  },
  {
    deptId: 2,
    deptName: '市场部',
    deptLeaderName: '李四',
    deptQuota: 30,
    status: '启用',
    fatherDeptName: '无',
    leaderId: 1002
  },
  {
    deptId: 3,
    deptName: '人事部',
    deptLeaderName: '王五',
    deptQuota: 20,
    status: '启用',
    fatherDeptName: '无',
    leaderId: 1003
  },
  {
    deptId: 4,
    deptName: '财务部',
    deptLeaderName: '赵六',
    deptQuota: 15,
    status: '启用',
    fatherDeptName: '无',
    leaderId: 1004
  },
  {
    deptId: 5,
    deptName: '研发部',
    deptLeaderName: '钱七',
    deptQuota: 40,
    status: '停用',
    fatherDeptName: '技术部',
    leaderId: 1005
  }
]

// 表格数据
const tableData = ref([...mockDepartmentData])
const total = ref(mockDepartmentData.length)

// 搜索参数
const searchParams = ref({
  deptName: '',
  status: ''
})

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)

// 状态选项
const statusOptions = [
  { value: '启用', label: '启用' },
  { value: '停用', label: '停用' }
]

// 新增部门数据
const addDepartment = ref({
  deptName: '',
  deptSize: 0,
  fatherDeptName: '',
  leaderId: 0
})

// 编辑部门数据
const editForm = ref({
  deptName: '',
  deptSize: 0,
  fatherDeptName: '',
  leaderId: 0
})

let currentEditDeptId = 0

// 过滤后的表格数据
const filteredTableData = computed(() => {
  let filtered = tableData.value;
  
  // 按部门名称过滤
  if (searchParams.value.deptName) {
    filtered = filtered.filter(item => 
      item.deptName.includes(searchParams.value.deptName)
    );
  }
  
  // 按状态过滤
  if (searchParams.value.status) {
    filtered = filtered.filter(item => 
      item.status === searchParams.value.status
    );
  }
  
  return filtered;
});

// 分页后的数据
const paginatedTableData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredTableData.value.slice(start, end);
});

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  ElMessage.success('搜索完成');
}

// 重置
const handleReset = () => {
  searchParams.value = {
    deptName: '',
    status: ''
  }
  currentPage.value = 1
  ElMessage.success('重置完成');
}

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
}

// 查看部门
const viewDepartment = (deptId: number) => {
  ElMessage.info(`查看部门 ID: ${deptId}`);
  // 如果有详细页面，可以在这里跳转
  // router.push({ name: 'departmentDetail', params: { id: deptId } })
}

// 编辑部门
const editDepartment = (department: any) => {
  editForm.value = {
    deptName: department.deptName,
    deptSize: department.deptQuota,
    fatherDeptName: department.fatherDeptName,
    leaderId: department.leaderId
  }
  currentEditDeptId = department.deptId
  editDialogVisible.value = true
}

// 更新部门状态（撤销/启用）
const updateStatus = (department: any) => {
  const newStatus = department.status === '启用' ? '停用' : '启用';
  const action = department.status === '启用' ? '撤销' : '启用';
  
  // 更新假数据中的状态
  const index = tableData.value.findIndex(item => item.deptId === department.deptId);
  if (index !== -1) {
    tableData.value[index].status = newStatus;
    ElMessage.success(`${action}成功`);
  }
}

// 提交新增表单
const submitForm = () => {
  // 生成新的部门ID
  const newDeptId = Math.max(...tableData.value.map(item => item.deptId)) + 1;
  
  // 添加到假数据
  tableData.value.push({
    deptId: newDeptId,
    deptName: addDepartment.value.deptName,
    deptLeaderName: `用户${addDepartment.value.leaderId}`,
    deptQuota: addDepartment.value.deptSize,
    status: '启用',
    fatherDeptName: addDepartment.value.fatherDeptName,
    leaderId: addDepartment.value.leaderId
  });
  
  dialogFormVisible.value = false;
  ElMessage.success('添加成功');
  
  // 重置表单
  addDepartment.value = {
    deptName: '',
    deptSize: 0,
    fatherDeptName: '',
    leaderId: 0
  };
}

// 提交编辑表单
const submitEditForm = () => {
  // 更新假数据
  const index = tableData.value.findIndex(item => item.deptId === currentEditDeptId);
  if (index !== -1) {
    tableData.value[index] = {
      ...tableData.value[index],
      deptName: editForm.value.deptName,
      deptQuota: editForm.value.deptSize,
      fatherDeptName: editForm.value.fatherDeptName,
      leaderId: editForm.value.leaderId,
      deptLeaderName: `用户${editForm.value.leaderId}`
    };
    
    editDialogVisible.value = false;
    ElMessage.success('修改成功');
  }
}

onMounted(() => {
  // 初始化时不需要API调用
  console.log('部门管理页面已加载');
})
</script>

<style scoped>
.container {
  padding: 5px 20px;
  background-color: rgb(133, 226, 200);
  margin: 10px 20px;
  border-radius: 15px;
  box-shadow: 
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.container .el-card {
  border-radius: 15px;
  margin: 20px 0;
}

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

.el-input {
  width: 150px;
  height: 32px;
}

.el-select {
  width: 180px;
  height: 30px;
}

.block {
  display: flex;
  margin-top: 10px;
  margin-bottom: 20px;
}

.content {
  margin-top: 2px;
  margin-left: 20px;
  font-size: 14px;
}

.doButton1 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(0, 149, 255);
  text-decoration: none;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.doButton2 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(224, 143, 29);
  text-decoration: none;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.doButton3 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(244, 102, 123);
  text-decoration: none;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.doButton1:hover,
.doButton2:hover,
.doButton3:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.selectButton {
  margin-left: 20px;
}

.demo-pagination-block+.demo-pagination-block {
  margin-bottom: 0px;
}

.demo-pagination-block .page {
  margin-bottom: 16px;
}

.middle {
  display: flex;
  align-items: center;
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
  text-align: center;
}

:deep(.el-table td) {
  text-align: center;
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
    padding: 10px;
    margin: 10px;
  }
  
  .block {
    flex-direction: column;
    gap: 10px;
  }
  
  .selectButton {
    margin-left: 0;
  }
  
  .el-input,
  .el-select {
    width: 100%;
  }
}
</style>