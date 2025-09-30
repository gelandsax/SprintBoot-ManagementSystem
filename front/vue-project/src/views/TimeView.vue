<template>
  <div class="pageTitle">
    <div class="title">{{ curpageTitle }}</div>
    <div class="subTitle"><el-icon>
        <HomeFilled />
      </el-icon>
      <RouterLink :to="'/main'" style="text-decoration: none; color: black;"> 首页</RouterLink> <el-icon>
        <ArrowRight />
      </el-icon> <RouterLink :to="'/time'" style="text-decoration: none; color: black;">时间管理</RouterLink><span style="color: gray;"><el-icon>
          <ArrowRight />
        </el-icon> {{ curpageTitle }}</span>


    </div>
  </div>
  <el-empty v-if="!curpageTitle" description="页面未编辑" />
  <RouterView></RouterView>

</template>
<script setup lang="ts">
import { provide, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
const route = useRoute();
const curpageTitle = ref('')
// 提供全局方法给子组件使用
provide('setPageTitle', (title: string) => {
  curpageTitle.value = title
})
watch(
  () => route.fullPath,
  () => {
    // 重置标题，让子组件重新设置
    curpageTitle.value = '';

    // 如果需要强制刷新组件，可以使用以下方法
    // 这会触发当前路由组件的重新加载
  }
)
</script>
<style scoped>
.container {
  padding: 20px;
  background-color: gainsboro;
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
