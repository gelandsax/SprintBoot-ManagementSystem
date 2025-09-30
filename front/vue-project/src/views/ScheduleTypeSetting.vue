<template>
  <div class="container" style="background-color: #ffffff;margin: 20px;border-radius: 15px;">
    <div class="search">
      <el-button size="larger" type="primary" style="margin-left: 20px;" @click="dialogVisible = true">新增</el-button>
      <el-dialog v-model="dialogVisible" title="新增排班类型" width="800">
        <div class="form" style="display: flex;">
          <el-form-item label="排班类型：">
            <el-input v-model="form.name" placeholder="请输入排班类型名" />
          </el-form-item>
        <el-form-item label="上班时间：">
          <el-time-picker
            v-model="form.startTime"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择上班时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="下班时间：">
          <el-time-picker
            v-model="form.endTime"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择下班时间">
          </el-time-picker>
        </el-form-item>
        </div>

        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary">重置</el-button>
            <el-button type="primary" @click="dialogVisible = false">
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
    <div class="list" style="width: 100%;">
      <el-table :data="scheduleData" size="large" stripe style="width: 100%;">
        <el-table-column prop="scheduleType" label="排班类型" />
        <el-table-column prop="startTime" label="上班时间" />
        <el-table-column prop="endTime" label="下班时间" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" type="warning">修改</el-button>
            <el-button size="small" type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="demo-pagination-block">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[5, 10, 20, 30]"
        layout="total, sizes, prev, pager, next, jumper" :total="400" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessageBox } from 'element-plus';
import { inject, onMounted, reactive, ref } from 'vue';


const value = ref('')
const form = reactive({
  name: '',
  region: '',
  startTime: '',
  endTime: '',
  delivery: false,
  type: [],
  resource: '',
  desc: '',
})
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
const dialogVisible = ref(false)


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
    scheduleType: '早班',
    startTime: '08:00',
    endTime: '17:00'
  },
  {
    scheduleType: '晚班',
    startTime: '16:00',
    endTime: '01:00'
  }
]

const setPageTitle = inject<(title: string) => void>('setPageTitle')
onMounted(() => {
  if (setPageTitle) {
    setPageTitle('排班类型设定')
  }
});
</script>

<style scoped>
.search {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
.container{
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow: 
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
</style>
