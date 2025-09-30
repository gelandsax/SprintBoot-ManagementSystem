<template>
    <div class="container">
        <div class="list" style="width: 100%;">
            <el-table :data="tableData.bqlist" size="large" stripe style="width: 100%;">
                <el-table-column label="日期" width="120">
                    <template #default="scope">
                        {{ formatDate(scope.row.workdate) }}
                    </template>
                </el-table-column>
                <el-table-column prop="username" label="员工姓名" width="120" />
                <el-table-column prop="status" label="迟到原因" width="120" />
                <el-table-column label="上班时间" width="238">
                    <template #default="scope">
                        <el-time-picker v-model="scope.row.check_in_time" format="HH:mm:ss" value-format="HH:mm:ss"
                            placeholder="选择时间" :disabled="!scope.row.check_in">
                        </el-time-picker>
                    </template>
                </el-table-column>
                <el-table-column label="下班时间" width="238">
                    <template #default="scope">
                        <el-time-picker v-model="scope.row.check_out_time" format="HH:mm:ss" value-format="HH:mm:ss"
                            placeholder="选择时间" :disabled="!scope.row.check_out">
                        </el-time-picker>
                    </template>
                </el-table-column>
                <el-table-column label="异常原因">
                    <template #default="scope">
                        <el-input v-model="scope.row.reason" placeholder="请输入补签原因" />
                    </template>
                </el-table-column>
                <el-table-column label="补签" width="200">
                    <template #default="scope">
                        <el-button size="small" type="primary" @click.prevent="resign(scope.row)"
                            :disabled="!isValidResign(scope.row)">
                            <el-icon>
                                <FirstAidKit />
                            </el-icon> 补签
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="header_back">
            <el-button @click="() => { $router.back() }">返回打卡</el-button>
        </div>
        <div class="demo-pagination-block">
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[5, 10, 20, 30]"
                layout="total, sizes, prev, pager, next, jumper" :total="tableData.bqlist.length"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, inject, computed } from 'vue';
import { ElMessage } from 'element-plus';
import type { BQapplyReq, Bqlist, GetAllBQAttendRsp } from '@/models';
import axios from '../utils/request';

// 分页相关
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

// 扩展 Bqlist 接口以包含补签所需字段
interface BqlistWithResign extends Bqlist {
    reason?: string;
    check_in_time?: string;
    check_out_time?: string;
    attendanceId: number;
}

interface GetAllBQAttendRspWithResign {
    bqlist: BqlistWithResign[];
}

// 日期格式化函数
const formatDate = (dateString: string) => {
    if (!dateString) return '';
    return dateString.split('T')[0];
}

// 表格数据
const tableData = ref<GetAllBQAttendRspWithResign>({
    bqlist: []
});

// 补签请求数据
const resignReq = ref<BQapplyReq>({
    bqapplay: {
        username: '',
        status: '',
        workdate: '',
        checkIn: '',
        checkOut: '',
        reason: '',
        attendanceId: 0
    }
});

// 验证补签数据是否有效
const isValidResign = (row: BqlistWithResign) => {
    return row.reason && row.reason.trim() !== '' &&
        (row.check_in_time || row.check_out_time);
}

// 补签函数
function resign(row: BqlistWithResign) {
    if (!isValidResign(row)) {
        ElMessage({
            message: '请填写补签原因并选择至少一个时间',
            type: 'warning',
        });
        return;
    }

    resignReq.value.bqapplay.username = row.username;
    resignReq.value.bqapplay.status = '未审批';
    resignReq.value.bqapplay.workdate = row.workdate.slice(0, 10);
    resignReq.value.bqapplay.checkIn = row.check_in_time || '';
    resignReq.value.bqapplay.checkOut = row.check_out_time || '';
    resignReq.value.bqapplay.reason = row.reason || '';
    resignReq.value.bqapplay.attendanceId = row.attendanceId || 0;

    console.log('补签请求数据:', resignReq.value);

    axios.post('/attendance/BQApply', resignReq.value).then(res => {
        window.location;
        ElMessage({
            message: '补签申请提交成功',
            type: 'success',
        });
        // 重新加载数据
        loadAttendanceData();

    })
}


// 时间格式化函数
function formatTimeValue(timeValue: any): string {
    if (typeof timeValue === 'string') {
        return timeValue;
    } else if (timeValue instanceof Date) {
        const hours = String(timeValue.getHours()).padStart(2, '0');
        const minutes = String(timeValue.getMinutes()).padStart(2, '0');
        const seconds = String(timeValue.getSeconds()).padStart(2, '0');
        return `${hours}:${minutes}:${seconds}`;
    } else if (timeValue && typeof timeValue === 'object') {
        // 处理后端返回的时间对象
        const hours = String(timeValue.hours || 0).padStart(2, '0');
        const minutes = String(timeValue.minutes || 0).padStart(2, '0');
        const seconds = String(timeValue.seconds || 0).padStart(2, '0');
        return `${hours}:${minutes}:${seconds}`;
    }
    return '';
}

// 加载考勤数据
const loadAttendanceData = () => {
    axios.get('/attendance/BQAttend').then(res => {
        console.log('获取补签数据:', res.data);
        // 转换数据，添加补签所需字段
        const transformedData = {
            ...res.data,
            bqlist: res.data.bqlist.map((item: Bqlist) => {
                // 将后端的时间对象转换为字符串格式用于显示
                const checkInTime = item.check_in ? formatTimeValue(item.check_in) : '';
                const checkOutTime = item.check_out ? formatTimeValue(item.check_out) : '';

                return {
                    ...item,
                    reason: '',
                    check_in_time: checkInTime,
                    check_out_time: checkOutTime,
                    attendanceId: item.attendanceId || 0
                };
            })
        };
        tableData.value = transformedData;
    }).catch(error => {
        console.error('获取补签数据失败:', error);
        ElMessage({
            message: '获取补签数据失败',
            type: 'error',
        });
    });
}

const setPageTitle = inject<(title: string) => void>('setPageTitle');

onMounted(() => {
    if (setPageTitle) {
        setPageTitle('考勤补签');
    }
    loadAttendanceData();
});
</script>

<style scoped>
.container {
    padding: 20px;
    margin: 20px;
    background: #ffffff;
    overflow: hidden;
    border-radius: 15px;
    box-shadow:
        0 15px 40px rgba(0, 0, 0, 0.3),
        0 5px 15px rgba(0, 0, 0, 0.15) !important;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.list {
    margin-bottom: 15px;
}

.header_back {
    text-align: center;
    margin-bottom: 20px;
}

.header_back .el-button {
    width: 100px;
    height: 36px;
    font-weight: bold;
    font-size: 14px;
    color: white;
    background-color: rgb(0, 170, 255);
    box-shadow:
        0 15px 40px rgba(0, 0, 0, 0.1),
        0 5px 15px rgba(0, 0, 0, 0.1) !important;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.header_back .el-button:hover {
    background-color: white;
    color: rgb(0, 170, 255);
    border: 1px solid rgb(0, 170, 255);
}

.demo-pagination-block {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

/* 响应式调整 */
@media (max-width: 768px) {
    .container {
        padding: 10px;
        margin: 10px;
    }

    .header_back .el-button {
        width: 80px;
        height: 32px;
        font-size: 12px;
    }
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

:deep(.el-table .el-time-picker) {
    width: 100%;
}
</style>