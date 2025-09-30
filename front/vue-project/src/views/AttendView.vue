<template>
    <div class="container">
        <el-carousel :interval="5000" arrow="always">
            <el-carousel-item v-for="(item, index) in carouselImages" :key="index">
                <img :src="item.src" :alt="item.alt" class="carousel-image" />
            </el-carousel-item>
        </el-carousel>
        <div class="timeCounter">
            <p class="time-title">当前服务器时间：</p>
            <div class="time-balls">
                <span class="time-ball">{{ timeData.year }}</span>
                <span class="time-label">年</span>
                <span class="time-ball">{{ timeData.month }}</span>
                <span class="time-label">月</span>
                <span class="time-ball">{{ timeData.day }}</span>
                <span class="time-label">日</span>
                <span class="time-ball">{{ timeData.hour }}</span>
                <span class="time-label">时</span>
                <span class="time-ball">{{ timeData.minute }}</span>
                <span class="time-label">分</span>
                <span class="time-ball">{{ timeData.second }}</span>
                <span class="time-label">秒</span>
            </div>
        </div>
        <div class="attendbuttons">
            <el-button
                size="large"
                color="#4FB5D3"
                @click.prevent="punchIn()"
                :disabled="isPunchInDisabled"
                :class="{ 'disabled-button': isPunchInDisabled }"
            >
                上班打卡
            </el-button>
            <el-button
                size="large"
                color="#4FB5D3"
                @click.prevent="punchOut()"
                :disabled="isPunchOutDisabled"
                :class="{ 'disabled-button': isPunchOutDisabled }"
            >
                下班打卡
            </el-button>
            <el-button size="large" color="#4FB5D3" @click.prevent="resign()">补签</el-button>
        </div>
        <div class="attendlist">
            <el-table :data="tableData" :border="true" class="custom-table">
                <el-table-column prop="username" label="打卡人" />
                <el-table-column prop="checkInTime" label="上班时间" />
                <el-table-column prop="checkOutTime" label="下班时间" />
            </el-table>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, inject, watch, computed } from 'vue';
import image1 from '@/assets/images/image1.jpg';
import image2 from '@/assets/images/image2.jpg';
import image3 from '@/assets/images/image3.jpg';
import image4 from '@/assets/images/image4.jpg';
import router from '../router';
import { ElMessage } from 'element-plus';
import type { GetPunchMsgRsp, PunchReq, PunchRsp } from '@/models';
import { useCurUserStore } from '../stores/user';
import { storeToRefs } from 'pinia';
import axios from '../utils/request';

const store = useCurUserStore();
const { curUser } = storeToRefs(store);

// 定义时间数据类型
interface TimeData {
    year: string;
    month: string;
    day: string;
    hour: string;
    minute: string;
    second: string;
}

const isPunchInDisabled = ref(false);
const isPunchOutDisabled = ref(false);
const setPageTitle = inject<(title: string) => void>('setPageTitle');
const timeData = ref<TimeData>({
    year: '',
    month: '',
    day: '',
    hour: '',
    minute: '',
    second: ''
});
let timer: number | null = null;

const carouselImages = [
    { src: image1, alt: '图片1' },
    { src: image2, alt: '图片2' },
    { src: image3, alt: '图片3' },
    { src: image4, alt: '图片4' },
];

// 使用计算属性来创建表格数据
const tableData = computed(() => [
    {
        username: curUser.value?.username || '----',
        checkInTime: punchmsgrsp.value.check_in ? punchmsgrsp.value.check_in : '----',
        checkOutTime: punchmsgrsp.value.check_out ? punchmsgrsp.value.check_out : '----',
    }
]);

const updateTime = () => {
    const now = new Date();
    timeData.value = {
        year: now.getFullYear().toString(),
        month: (now.getMonth() + 1).toString().padStart(2, '0'),
        day: now.getDate().toString().padStart(2, '0'),
        hour: now.getHours().toString().padStart(2, '0'),
        minute: now.getMinutes().toString().padStart(2, '0'),
        second: now.getSeconds().toString().padStart(2, '0')
    };
};

const refreshTrigger = ref(0);
watch(refreshTrigger, () => {
    axios.get('/attendance/record').then(res => {
        punchmsgrsp.value = res.data;
        isPunchInDisabled.value = !!punchmsgrsp.value.check_in;
        isPunchOutDisabled.value = !punchmsgrsp.value.check_in;
    });
});

// 组件挂载时开始计时
onMounted(() => {
    if (setPageTitle) {
        setPageTitle('考勤管理');
    }
    updateTime();
    timer = window.setInterval(updateTime, 1000);
    axios.get('/attendance/record').then(res => {
        punchmsgrsp.value = res.data;
        isPunchInDisabled.value = !!punchmsgrsp.value.check_in;
        isPunchOutDisabled.value = !punchmsgrsp.value.check_in;
    });
    refreshTrigger.value = 0;
});

const punchmsgrsp = ref<GetPunchMsgRsp>({
    check_in: '',
    check_out: '',
    status: '',
    username: ''
});

// 组件卸载时清理定时器
onUnmounted(() => {
    if (timer) {
        clearInterval(timer);
    }
});

const resign = () => {
    router.push('/time/attend_resign');
};

const punchmsg = ref<PunchReq>({
    type: '',
    username: ''
});

const punchrspmsg = ref<PunchRsp>({
    username: '',
    status: '',
    serverTime: '',
});

function punchIn() {
    punchmsg.value.type = 'check_in';
    if (curUser.value == null) {
        ElMessage({
            message: '请先登录',
            type: 'warning',
        });
    } else {
        punchmsg.value.username = curUser.value?.username;
        axios.post('/attendance/clock', punchmsg.value).then(res => {
            punchrspmsg.value = res.data;
            refreshTrigger.value++;
            if (punchrspmsg.value.status == '正常') {
                ElMessage({
                    message: '打卡成功',
                    type: 'success',
                });
            } else if (punchrspmsg.value.status == '迟到') {
                ElMessage({
                    message: '打卡成功，请勿忘记打卡',
                    type: 'warning',
                });
            } else if (punchrspmsg.value.status == '异常') {
                ElMessage({
                    message: '打卡失败，请勿忘记打卡',
                    type: 'error',
                });
            } else {
                ElMessage({
                    message: '打卡失败',
                    type: 'error',
                });
            }
        });
    }
}

const lastPunchOutTime = ref<number>(parseInt(localStorage.getItem('lastPunchOutTime') || '0'));
const updateLastPunchOutTime = (time: number) => {
    lastPunchOutTime.value = time;
    localStorage.setItem('lastPunchOutTime', time.toString());
};

function punchOut() {
    punchmsg.value.type = 'check_out';
    if (curUser.value == null) {
        ElMessage({
            message: '请先登录',
            type: 'warning',
        });
    } else {
        const currentTime = Date.now();
        const timeDiff = currentTime - lastPunchOutTime.value;

        if (lastPunchOutTime.value !== 0 && timeDiff < 180000) {
            ElMessage({
                message: '打卡过于频繁，请稍后再试',
                type: 'warning',
            });
            return;
        }
        punchmsg.value.username = curUser.value?.username;
        axios.post('/attendance/clock', punchmsg.value).then(res => {
            punchrspmsg.value = res.data;
            updateLastPunchOutTime(currentTime);
            refreshTrigger.value++;
            if (punchrspmsg.value.status == '正常') {
                ElMessage({
                    message: '打卡成功',
                    type: 'success',
                });
            } else if (punchrspmsg.value.status == '迟到') {
                ElMessage({
                    message: '打卡成功，请勿忘记打卡',
                    type: 'warning',
                });
            } else if (punchrspmsg.value.status == '异常') {
                ElMessage({
                    message: '打卡失败，请勿忘记打卡',
                    type: 'error',
                });
            } else {
                ElMessage({
                    message: '打卡失败',
                    type: 'error',
                });
            }
        });
    }
}
</script>

<style scoped>
/* 核心：禁止页面所有滚动条 */
html,
body {
    overflow: hidden;
    /* 完全隐藏浏览器滚动条 */
    height: 100%;
    margin: 0;
    padding: 0;
}

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    /* 确保padding不增加元素总宽度 */
}

.carousel-image {
    width: 100%;
    height: 300px;
    object-fit: cover;
}

.el-carousel {
    border-radius: 15px;
    margin: 10px;
}

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
}

/* 容器自适应视口，不溢出 */
.container {
    height: 100vh;
    /* 视口高度 */
    padding: 20px;
    margin: 20px;
    background: #ffffff;
    overflow: hidden;
    /* 容器内内容不溢出 */
    border-radius: 15px;
    /* 初始阴影 - 黑色，中等范围 */
    box-shadow:
        0 15px 40px rgba(0, 0, 0, 0.3),
        0 5px 15px rgba(0, 0, 0, 0.15) !important;

    /* 平滑过渡效果 */
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.container .attendbuttons {
    margin-top: 30px;
    height: 50px;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 10px;
}

.attendbuttons .el-button {
    min-width: 200px;
    height: 50px;
    padding: 0 30px;
    color: white;
    font-size: 20px;
    font-weight: bold;
    /* 初始阴影 - 黑色，中等范围 */
    box-shadow:
        0 15px 40px rgba(0.1, 0.1, 0.1, 0.2),
        0 5px 15px rgba(0, 0, 0, 0.15) !important;

    /* 平滑过渡效果 */
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.attendbuttons .el-button:hover {
    color: #4FB5D3;
    background-color: white;
}

.timeCounter {
    margin: 20px 10px;
    background: linear-gradient(135deg, #667eea 0%, #d7c4c4 100%);
    padding: 20px;
    text-align: center;
    border: 1px solid rgb(114, 92, 92);
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.time-title {
    font-size: 24px;
    color: #ffffff;
    font-weight: 700;
    margin: 0 0 15px 0;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.time-balls {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 10px;
    align-items: center;
}

.time-ball {
    display: inline-block;
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 50%;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    color: #333;
    font-size: 20px;
    font-weight: bold;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.time-label {
    color: #ffffff;
    font-size: 18px;
    font-weight: 600;
    margin: 0 5px;
}

/* 表格区域完全自适应，不固定宽度 */
.attendlist {
    width: 100%;
    max-width: 100%;
    /* 不超过容器宽度 */
    height: 120px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 50px auto 0;
}

/* 表格自适应宽度，不溢出 */
:deep(.custom-table) {
    width: 100% !important;
    /* 表格宽度跟随容器 */
    max-width: 1050px;
    /* 最大宽度限制 */
    border: 1px solid #4FB5D3 !important;
    font-size: 20px;
}

:deep(.custom-table .el-table__header tr) {
    height: 60px !important;
}

:deep(.custom-table .el-table__header th) {
    background-color: #4FB5D3 !important;
    color: white !important;
    font-weight: bold;
    text-align: center !important;
    border-right: 1px solid #4FB5D3 !important;
}

:deep(.custom-table .el-table__body tr) {
    height: 60px !important;
}

:deep(.custom-table .el-table__body td) {
    border-bottom: 1px solid #4FB5D3 !important;
    color: black !important;
    text-align: center !important;
    border-right: 1px solid #4FB5D3 !important;
}

:deep(.custom-table .el-table--border) {
    border: 2px solid #4FB5D3 !important;
}

:deep(.custom-table .el-table--border::after),
:deep(.custom-table .el-table--border::before) {
    background-color: #4FB5D3 !important;
}

/* 响应式调整，确保小屏幕不溢出 */
@media (max-width: 768px) {
    .container {
        padding: 10px;
    }

    .carousel-image {
        height: 200px;
    }

    .time-title {
        font-size: 20px;
    }

    .time-ball {
        width: 40px;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
    }

    .attendbuttons {
        height: auto;
        flex-direction: column;
        padding: 10px 0;
    }

    .attendbuttons .el-button {
        width: 100%;
        font-size: 16px;
    }
}

.time-label {
    font-size: 16px;
  }

  .time-title {
    font-size: 20px;
  }
.el-table{
    /* 初始阴影 - 黑色，中等范围 */
    box-shadow:
        0 15px 40px rgba(0, 0, 0, 0.1),
        0 5px 15px rgba(0, 0, 0, 0.1) !important;

    /* 平滑过渡效果 */
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.attendlist {
  display: flex;
  flex-wrap: wrap;
  margin: 0;
  margin-top: 20px;
  text-align: center;
}
</style>
