<template>
  <div class="pageTitle">
    <div class="title">职位信息</div>
    <div class="subTitle"><el-icon>
        <HomeFilled />
      </el-icon>
      <RouterLink :to="'/main'" style="text-decoration: none; color: black;"> 首页</RouterLink> <span
        style="color: gray;"><el-icon>
          <ArrowRight />
        </el-icon> 职位信息</span>
    </div>
  </div>

  <div class="container">
    <el-card>
      <template #header>
        <div class="block">
          <span class="content">名称：</span>
          <el-input style="height: 24px;" placeholder="请输入职位名称"></el-input>
          <span class="content">状态：</span>
          <el-select placeholder="=请选择=">
            <el-option v-for="item in sex" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <div class="selectButton">
            <el-button type="primary">搜索</el-button>
            <el-button type="primary">重置</el-button>
          </div>

        </div>


      </template>

      <el-button type="primary" style="margin-bottom: 15px;">新增</el-button>
      <el-table :border="true" :data="tableData" stripe style="width: 100%">
        <el-table-column prop="编号" label="编号" width="150px" />
        <el-table-column prop="职位名称" label="职位名称" width="210px" />
        <el-table-column prop="职位级别" label="职位级别" width="210px" />
        <el-table-column prop="状态" label="状态" width="200px" />
        <el-table-column prop="操作" label="操作" width="480px">
          <el-button class="doButton1"><el-icon>
              <View />
            </el-icon>查看</el-button>
          <el-button class="doButton2"><el-icon>
              <Refresh />
            </el-icon>修改</el-button>
          <el-button class="doButton3"><el-icon>
              <SemiSelect />
            </el-icon>撤销</el-button>
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
import { useCurUserStore } from '../stores/user'  /* 当前用户 */
import { storeToRefs } from 'pinia'
import router from '../router';
import { ref } from 'vue'
const store = useCurUserStore()
const { curUser } = storeToRefs(store)
const tableData = [
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
  {
    编号: '001',
    职位名称: '管理员',
    职位级别: '女',
    状态: '人事助理',
    操作: '1',
  },
]
const currentPage = ref(5)
const pageSize = ref(10)
const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`)
}
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`)
}
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
</script>


<style scoped>
.container {
  padding: 5px 20px;
  background-color: rgb(133, 226, 200);
  margin: 10px 20px;
  border-radius: 15px;
  /* 初始阴影 - 黑色，中等范围 */
  box-shadow: 
    0 15px 40px rgba(0, 0, 0, 0.3),
    0 5px 15px rgba(0, 0, 0, 0.15) !important;
  
  /* 平滑过渡效果 */
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
  background-color: #9abcf4;
}

.pageTitle .subTitle {
  font-size: medium;
  padding: 10px 20px;
  background-color: antiquewhite;
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

.dateselect {
  display: flex;
}


/* --------------------- */

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
  margin-top: 40px;
  margin-bottom: 10px;
  gap: 20px;
}

.font {
  font-weight: bold;
}

.content {
  margin-top: 2px;
  font-size: 14px;
  margin-left: 20px;
}

.doButton1 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(0, 149, 255);
  text-decoration: none;
}

.doButton2 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(224, 143, 29);
  text-decoration: none;
}

.doButton3 {
  width: 65px;
  height: 25px;
  font-size: 13px;
  color: white;
  background-color: rgb(244, 102, 123);
  text-decoration: none;
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
}

/* 标题栏：深灰蓝 → 浅灰蓝（从左到右，视觉上有“收缩聚焦”感） */
.pageTitle .title {
  font-size: larger;
  font-weight: 800;
  padding: 10px 20px;
  border-top-left-radius: 12px;
  background-image: linear-gradient(to right, #3A639B, #7BA7E0); /* 深灰蓝→浅灰蓝 */
  background-size: 100% 100%;
  color: white; /* 加白色文字，与深色渐变对比更清晰（原样式可能漏了文字色） */
}

/* 副标题栏：浅蓝灰 → 淡青蓝（辅助色，比标题浅2个层级，不抢焦点） */
.pageTitle .subTitle {
  font-size: medium;
  font-weight: bold;
  padding: 10px 20px;
  border-bottom-left-radius: 12px;
  background-image: linear-gradient(to right, #E0E8F0, #B3C6E0); /* 浅蓝灰→淡青蓝 */
  background-size: 100% 100%;
  color: #333; /* 深灰文字，与浅色渐变搭配不刺眼 */
}
</style>
