<template>
  <div class="container" style="background-color: #ffffff;margin: 20px;border-radius: 15px;">
    <div class="search">
      <el-button size="larger" type="primary" style="margin-left: 20px;" @click="dialogVisible = true">新增</el-button>
      <el-dialog
    v-model="dialogVisible"
    title="新增假期类型"
    width="800"
  >
    <div class="form" style="display: flex; flex-direction: column;">
          <el-form-item label="假期类型：">
            <el-input v-model="form.name" placeholder="请输入假期类型名" />
          </el-form-item>
          <el-form-item label="是否受限：">
            <el-select v-model="form.limited" placeholder="请选择是否受限">
              <el-option label="受限" value="受限"></el-option>
              <el-option label="不受限" value="不受限"></el-option>
            </el-select>
          </el-form-item>
        </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
    </div>
    <div class="list" style="width: 100%;">
      <el-table :data="holidayData" size="large" stripe style="width: 100%;">
        <el-table-column prop="holidayType" label="假期类型" width="400" />
        <el-table-column prop="limited" label="是否受限" width="400" />
        <el-table-column label="操作" >
          <template #default="scope">
            <el-button size="small" type="warning">修改</el-button>
            <el-button size="small" type="danger">删除</el-button>
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
const currentPage = ref(5)
const pageSize = ref(10)
const dialogVisible = ref(false)
const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`)
}

const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`)
}

const form = ref({
  name: '',
  limited: ''
})
const holidayData = [
  {
    holidayType: '病假',
    limited: '不受限',
  },
  {
    holidayType: '年假',
    limited: '受剩余天数限定',
  }
]

const setPageTitle = inject<(title: string) => void>('setPageTitle')
onMounted(() => {
  if (setPageTitle) {
    setPageTitle('假期类型设定')
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
