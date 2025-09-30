<template>
  <el-dialog v-model="dialogFormVisible" title="新增员工" width="800">
    <el-form :model="addEmployee" label-position="left" label-width="120px">
      <!-- Employee Information Section -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名">
            <el-input v-model="addEmployee.employee.name" autocomplete="off" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-radio-group v-model="addEmployee.employee.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生日">
            <el-date-picker v-model="addEmployee.employee.birthday" type="date" placeholder="Select birthday"
              format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="婚姻状态">
            <el-select v-model="addEmployee.employee.maritalStatus" placeholder="Select marital status"
              style="width: 100%">
              <el-option label="single" value="single" />
              <el-option label="married" value="married" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="部门">
            <el-select v-model="addEmployee.employee.deptId" placeholder="Select department" style="width: 100%">
              <el-option v-for="item in getAllDep.department" :key="item.id" :label="item.deptName" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职位">
            <el-select v-model="addEmployee.employee.jobId" placeholder="Select position" style="width: 100%">
              <el-option v-for="item in getAllJob.job" :key="item.id" :label="item.jobName" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="入职日期">
            <el-date-picker v-model="addEmployee.employee.hireDate" type="date" placeholder="Select hire date"
              format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次工作日期">
            <el-date-picker v-model="addEmployee.employee.firstWorkDate" type="date"
              placeholder="Select first work date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="薪资">
            <el-input v-model="addEmployee.employee.salary" autocomplete="off" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排班类型">
            <el-select v-model="addEmployee.employee.workShifts">
              <el-option label="正常A班" value="正常A班" />
              <el-option label="正常B班" value="正常B班" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="权限">
        <el-select v-model="addEmployee.employee.permisson" placeholder="Select permission">
          <el-option label="管理员" value="admin" />
          <el-option label="普通员工" value="普通员工" />
          <el-option label="请假审批人" value="请假审批人" />
          <el-option label="考勤审批人" value="考勤审批人" />
        </el-select>
      </el-form-item>

      <!-- User Account Section -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名">
            <el-input v-model="addEmployee.user.username" autocomplete="off" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户邮箱">
            <el-input v-model="addEmployee.user.email" autocomplete="off" type="email" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="密码">
        <el-input v-model="addEmployee.user.password" autocomplete="off" :type="passwordVisible ? 'text' : 'password'"
          show-password @focus="passwordVisible = true" @blur="passwordVisible = false" />
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
  
  <el-dialog v-model="editDialogVisible" title="修改员工" width="600">
    <el-form :model="editForm" label-position="left" label-width="120px">
      <el-form-item label="员工姓名" required>
        <el-input v-model="editForm.profile.emp_name" />
      </el-form-item>

      <el-form-item label="登录名" required>
        <el-input v-model="editForm.profile.user_name" />
      </el-form-item>

      <el-form-item label="性别" required>
        <el-select v-model="editForm.profile.gender">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>

      <el-form-item label="生日" required>
        <el-date-picker v-model="editForm.profile.birthday" type="date" placeholder="Select birthday"
          format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" />
      </el-form-item>

      <el-form-item label="入职日期" required>
        <el-date-picker v-model="editForm.profile.hireDate" type="date" placeholder="Select hire date"
          format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" />
      </el-form-item>

      <el-form-item label="月薪" required>
        <el-input v-model.number="editForm.profile.salary" />
      </el-form-item>

      <el-form-item label="权限" required>
        <el-select v-model="editForm.profile.permission">
          <el-option label="管理员" value="admin" />
          <el-option label="普通员工" value="user" />
          <el-option label="经理" value="manager" />
        </el-select>
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
    <div class="title">员工信息</div>
    <div class="subTitle">
      <el-icon>
        <HomeFilled />
      </el-icon>
      <RouterLink :to="'/main'" style="text-decoration: none; color: black;"> 首页</RouterLink> 
      <span style="color: gray;">
        <el-icon>
          <ArrowRight />
        </el-icon> 员工信息
      </span>
    </div>
  </div>
  
  <div class="container">
    <el-card>
      <template #header>
        <div class="block">
          <span class="content">姓名：</span>
          <el-input placeholder="=请输入="></el-input>
          <span class="content">性别：</span>
          <el-select placeholder="=请选择=">
            <el-option v-for="item in sex" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <span class="content">职位：</span>
          <el-select placeholder="=请选择=">
            <el-option v-for="item in getAllJob.job" :key="item.id" :label="item.jobName" :value="item.id" />
          </el-select>
          <span class="content">部门：</span>
          <el-select placeholder="=请选择=">
            <el-option v-for="item in getAllDep.department" :key="item.id" :label="item.deptName" :value="item.id" />
          </el-select>
          <span class="content">状态：</span>
          <el-select placeholder="=请选择=">
            <el-option v-for="item in style" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>

        <div class="dataSelect">
          <span class="demonstration">请选择日期：</span>
          <div class="dateselect">
            <div class="demo-date-picker">
              <el-date-picker v-model="value1" type="date" :size="size" />
            </div>
            <span class="font">-</span>
            <div class="demo-date-picker">
              <el-date-picker v-model="value2" type="date" :size="size" />
            </div>
          </div>
          <div>
            <el-button type="primary">搜索</el-button>
            <el-button type="primary">重置</el-button>
          </div>
        </div>
      </template>

      <el-button type="primary" style="margin-bottom: 15px;" @click="dialogFormVisible = true">新增</el-button>
      
      <el-table :border="true" :data="sortedTableData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号" width="120px" />
    <el-table-column prop="emp_name" label="姓名" width="120px" />
    <el-table-column prop="gender" label="性别" width="120px" />
    <el-table-column prop="status" label="状态" width="120px" />
    <el-table-column prop="job" label="职位" width="180px" />
    <el-table-column prop="department" label="部门" width="180px" />
    <el-table-column label="操作" width="350px">
      <template #default="scope">
        <el-button class="doButton1" @click.prevent="viewUser(scope.row.user_name)">
          <el-icon>
            <View />
          </el-icon>查看
        </el-button>
        <el-button class="doButton2" @click.prevent="editUser(scope.row.user_name)">
          <el-icon>
            <Refresh />
          </el-icon>修改
        </el-button>

        <el-button 
          :class="['doButton3', { 'doButton3-inactive': scope.row.status === '在职' }]" 
          @click.prevent="toggleEmployeeStatus(scope.row.id)"
        >
          <el-icon>
            <SemiSelect />
          </el-icon>{{ scope.row.status === '在职' ? '离职' : '入职' }}
        </el-button>
        
        <el-button class="doButton4">
          <el-icon>
            <Promotion />
          </el-icon>调转
        </el-button>
      </template>
    </el-table-column>
  </el-table>
      
      <div class="middle">
        <div class="demo-pagination-block">
          <div class="page"></div>
          <el-pagination size="small" v-model:current-page="currentPage" v-model:page-size="pageSize"
            :page-sizes="[100, 200, 300, 400]" layout="total, sizes, prev, pager, next, jumper" :total="400"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useCurUserStore } from '../stores/user'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import { ElMessage, type ComponentSize } from 'element-plus'
import type { GetAllDepartmentsRsp, AddEmployeeReq, GetAllJobsRsp, GetAllProfilesRsp, Profile, GetProfileRsp, EmployeeUpdateReq } from '@/models';
import axios from '../utils/request';
import { computed, onMounted, ref, watch } from 'vue'

const router = useRouter()
const store = useCurUserStore()
const { curUser } = storeToRefs(store)

// 对话框控制
const dialogFormVisible = ref(false)
const editDialogVisible = ref(false)

// 表格数据
const tableData = ref<GetAllProfilesRsp>({
  profile: [{
    birthday: '',
    department: '',
    departmentHistory: [],
    email: '',
    emp_name: '',
    firstWorkDate: '',
    gender: '',
    hireDate: '',
    id: 0,
    job: '',
    jobHistory: [],
    maritalStatus: '',
    permission: '',
    salary: '',
    user_name: '',
  }]
})

// 排序后的表格数据
const sortedTableData = computed(() => {
  return [...tableData.value.profile].sort((a, b) => a.id - b.id);
});

// 查看用户
const viewUser = (username: string) => {
  router.push({ name: 'userinfo', params: { name: username } })
}

// 新增员工数据
const addEmployee = ref<AddEmployeeReq>({
  employee: {
    birthday: '',
    deptId: 0,
    firstWorkDate: '',
    gender: '',
    hireDate: '',
    jobId: 0,
    maritalStatus: '',
    name: '',
    permisson: '',
    salary: '',
    workShifts: ''
  },
  user: {
    email: '',
    password: '',
    username: ''
  }
})

const passwordVisible = ref(false)

// 搜索和分页相关
const value1 = ref('')
const value2 = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const size = ref<ComponentSize>('default')

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`)
}

const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`)
}

// 选项数据
const sex = [
  {
    value: '男',
    label: '男',
  },
  {
    value: '女',
    label: '女',
  }
]

const style = [
  {
    value: '在职',
    label: '在职',
  },
  {
    value: '离职',
    label: '离职',
  }
]

// 部门数据
const getAllDep = ref<GetAllDepartmentsRsp>({
  department: [{
    id: 0,
    deptName: '',
    leaderId: 0,
    createdAt: ''
  }]
});

// 职位数据
const getAllJob = ref<GetAllJobsRsp>({
  job: [{
    id: 0,
    jobName: '',
    createdAt: ''
  }]
});

// 获取数据
const fetchData = () => {
  axios.get(`profiles`).then(res => {
    tableData.value.profile = res.data.map((item: { profile: Profile }) => item.profile);
  })
  axios.get('all/jobs').then(res => {
    getAllJob.value.job = res.data;
  })
  axios.get('all/departsment').then(res => {
    getAllDep.value.department = res.data;
  })
}

const searchParams = ref<number>(0)
watch(searchParams, () => {
  fetchData()
})

onMounted(() => {
  fetchData()
})

// 提交新增表单
const submitForm = () => {
  axios.post('profiles/addemployee', addEmployee.value).then(() => {
    dialogFormVisible.value = false
    ElMessage({
      message: '添加成功',
      type: 'success',
    })
    searchParams.value++;
  })
}

// 编辑相关
const editForm = ref<GetProfileRsp>({
  profile: {
    user_name: '',
    emp_name: '',
    email: '',
    gender: '',
    birthday: '',
    firstWorkDate: '',
    hireDate: '',
    department: '',
    job: '',
    departmentHistory: [],
    jobHistory: [],
    maritalStatus: '',
    id: 0,
    permission: '',
    salary: '',
  }
})

const editUser = (username: string) => {
  axios.get(`profiles/${username}`).then(res => {
    editForm.value = res.data;
    editDialogVisible.value = true;
  }).catch(err => {
    ElMessage.error('Failed to load user data');
  });
}

const submitEditForm = () => {
  const updateData: EmployeeUpdateReq = {
    employeeId: editForm.value.profile.id,
    employeeName: editForm.value.profile.emp_name,
    loginName: editForm.value.profile.user_name,
    gender: editForm.value.profile.gender,
    birthday: editForm.value.profile.birthday,
    hiredDate: editForm.value.profile.hireDate,
    salary: editForm.value.profile.salary,
    permission: editForm.value.profile.permission
  };
  
  axios.put(`profiles/updateemployee/${editForm.value.profile.id}`, updateData).then(() => {
    editDialogVisible.value = false
    searchParams.value++;
    ElMessage({
      message: '修改成功',
      type: 'success',
    })
  }).catch(err => {
    ElMessage.error('修改失败');
  })
}

// 状态切换
const toggleEmployeeStatus = (id: number) => {
  axios.put(`profiles/status/${id}`).then(() => {
    searchParams.value++;
    ElMessage({
      message: '操作成功',
      type: 'success',
    })
  }).catch(err => {
    ElMessage.error('操作失败');
  })
}
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
  height: 24px;
}

.el-select {
  width: 180px;
  height: 30px;
}

.block {
  display: flex;
  margin-top: 10px;
}

.dateselect {
  display: flex;
}

.demo-date-picker {
  display: flex;
  width: 200px;
  padding: 0;
  flex-wrap: wrap;
}

.demonstration {
  font-size: 14px;
  margin-right: -20px;
  margin-top: 5px;
}

@media screen and (max-width: 768px) {
  .demo-date-picker .block {
    flex: 0 0 100%;
    padding: 1rem 0;
    min-width: auto;
    border-right: none;
    border-bottom: solid 1px var(--el-border-color);
  }

  .demo-date-picker .block:last-child {
    border-bottom: none;
  }
}

.dataSelect {
  display: flex;
  margin: 30px 0 10px 20px;
  gap: 20px;
}

.font {
  font-weight: bold;
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
  background-color: rgb(244, 102, 123); /* 入职状态 - 绿色 */
  text-decoration: none;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

/* 在职状态时的离职按钮样式 */
.doButton3-inactive {
  background-color: #909399 !important; /* 灰色背景 */
  color: white !important; /* 白色文字 */
}

.doButton4 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(57, 185, 28);
  text-decoration: none;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.doButton1:hover,
.doButton2:hover,
.doButton3:hover,
.doButton4:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* 在职状态时的离职按钮悬停效果 */
.doButton3-inactive:hover {
  background-color: #7a7e83 !important; /* 悬停时稍深的灰色 */
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
  
  .dataSelect {
    flex-direction: column;
    margin: 20px 0 10px 0;
  }
  
  .el-input,
  .el-select {
    width: 100%;
  }
}
</style>