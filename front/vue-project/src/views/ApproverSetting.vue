<template>
    <div class="container" style="background-color: #ffffff;margin: 20px;border-radius: 15px;">
        <div class="search" style="text-align: left;">
            <el-input v-model="input" style="width: 240px" placeholder="Please input" clearable />
            <el-button size="larger" type="primary" style="margin-left: 50px;">查找</el-button>
        </div>
        <div class="list" style="width: 100%;">

            <el-table :data="tableData" size="large" stripe style="width: 100%;">
                <el-table-column prop="departmentId" label="部门编号" />
                <el-table-column prop="departmentName" label="部门名称" />
                <el-table-column prop="attendanceApprover" label="考勤审批人" />
                <el-table-column prop="leaveApprover" label="请假审批人" />
                <el-table-column label="操作" width="200">
                    <template #default="scope">
                        <el-button size="small" type="primary">编辑</el-button>
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
import { inject, onMounted, ref } from 'vue';

const currentPage = ref(5)
const pageSize = ref(10)
const handleSizeChange = (val: number) => {
    console.log(`${val} items per page`)
}
const handleCurrentChange = (val: number) => {
    console.log(`current page: ${val}`)
}
const tableData = [
    {
        departmentId: 'D001',
        departmentName: 'Human Resources',
        attendanceApprover: 'Alice Johnson',
        leaveApprover: 'Bob Smith',
    },
    {
        departmentId: 'D002',
        departmentName: 'Engineering',
        attendanceApprover: 'Charlie Brown',
        leaveApprover: 'David Wilson',
    },
    {
        departmentId: 'D003',
        departmentName: 'Marketing',
        attendanceApprover: 'Eva Green',
        leaveApprover: 'Frank Miller',
    },
    {
        departmentId: 'D004',
        departmentName: 'Finance',
        attendanceApprover: 'Grace Lee',
        leaveApprover: 'Henry Taylor',
    },
    {
        departmentId: 'D005',
        departmentName: 'Operations',
        attendanceApprover: 'Ivy Chen',
        leaveApprover: 'Jack Davis',
    },
    {
        departmentId: 'D006',
        departmentName: 'Sales',
        attendanceApprover: 'Karen White',
        leaveApprover: 'Leo Martin',
    }
]
const input = ref('')
const setPageTitle = inject<(title: string) => void>('setPageTitle')
onMounted(() => {
    if (setPageTitle) {
        setPageTitle('审批人设定')
    }
});
</script>
<style scoped>
.container {
    padding: 50px;
    border-radius: 15px;
    /* 初始阴影 - 黑色，中等范围 */
    box-shadow:
        0 15px 40px rgba(0, 0, 0, 0.3),
        0 5px 15px rgba(0, 0, 0, 0.15) !important;

    /* 平滑过渡效果 */
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
</style>
