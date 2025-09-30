<template>
  <div class="container"  style="background-color: #ffffff;margin: 20px;border-radius: 15px;">
    <div class="search" size="large">
      <el-input v-model="input" style="width: 240px" placeholder="员工姓名" clearable />
      <el-input v-model="input" style="width: 240px;margin-left: 30px;" placeholder="工号" clearable />
      <el-select v-model="value" placeholder="Select" style="width: 240px;margin-left: 30px;">
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
      <el-button size="larger" type="primary" style="margin-left: 50px;">查找</el-button>
      <el-button size="larger" type="primary" style="margin-left: 20px;">重置</el-button>
    </div>
    <div class="list" style="width: 100%;">
      <el-table :data="scheduleData" size="large" stripe style="width: 100%;">
        <el-table-column prop="employeeId" label="工号" />
        <el-table-column prop="employeeName" label="姓名" />
        <el-table-column prop="department" label="部门" />
        <el-table-column prop="scheduleDate" label="排班日期" />
        <el-table-column prop="attendanceMethod" label="考勤方式" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" type="warning">修改</el-button>
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
        :total="400"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { inject, onMounted, ref } from 'vue';


const value = ref('')

const options = [
  {
    value: 'Option1',
    label: 'Option1',
  },
  {
    value: 'Option2',
    label: 'Option2',
  },
  {
    value: 'Option3',
    label: 'Option3',
  },
  {
    value: 'Option4',
    label: 'Option4',
  },
  {
    value: 'Option5',
    label: 'Option5',
  },
]
const currentPage = ref(5)
const pageSize = ref(10)
const input = ref('')

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`)
}

const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`)
}

const scheduleData = [
  {
    employeeId: 'E001',
    employeeName: '张三',
    department: '人事部',
    scheduleDate: '2023-06-01',
    attendanceMethod: '指纹考勤',
  },
  {
    employeeId: 'E002',
    employeeName: '李四',
    department: '技术部',
    scheduleDate: '2023-06-01',
    attendanceMethod: '人脸识别',
  },
  {
    employeeId: 'E003',
    employeeName: '王五',
    department: '销售部',
    scheduleDate: '2023-06-01',
    attendanceMethod: '密码考勤',
  },
  {
    employeeId: 'E004',
    employeeName: '赵六',
    department: '财务部',
    scheduleDate: '2023-06-01',
    attendanceMethod: '指纹考勤',
  },
  {
    employeeId: 'E005',
    employeeName: '钱七',
    department: '运营部',
    scheduleDate: '2023-06-01',
    attendanceMethod: '人脸识别',
  },
  {
    employeeId: 'E006',
    employeeName: '孙八',
    department: '市场部',
    scheduleDate: '2023-06-01',
    attendanceMethod: '密码考勤',
  }
]

const setPageTitle = inject<(title: string) => void>('setPageTitle')
onMounted(() => {
  if (setPageTitle) {
    setPageTitle('人员排班设定')
  }
});
</script>

<style scoped>
.container{
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow: 
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
.search {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
</style>
