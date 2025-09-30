<template>
    <el-watermark :width="130" :height="30" image="/images/logo.svg" :custom-style="{
        position: 'fixed',
        zIndex: -1,
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh'
    }">
        <div class="login-container">
            <el-card class="login-card">
                <div class="title">企业人事管理平台</div>
                <!-- 表单内容 -->
                <!-- 登录表单：isFrgotPassword为false时显示 -->
                <el-form v-if="!isForgotPassword" v-model="login_req" class="demo-form-inline">
                    <el-form-item>
                        <el-icon>
                            <User />
                        </el-icon>
                        <el-input v-model="usernameOrEmail" placeholder="请输入账号/邮箱" />
                    </el-form-item>
                    <el-form-item>
                        <el-icon>
                            <Lock />
                        </el-icon>
                        <el-input v-model="login_req.user.password" placeholder="请输入密码"
                            :type="showPassword ? 'text' : 'password'">
                            <!-- 使用 suffix 插槽将图标放在输入框右侧 -->
                            <template #suffix>
                                <el-icon @click="togglePasswordVisibility">
                                    <component :is="showPassword ? 'View' : 'Hide'" class="view" />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <span @click="isForgotPassword = true">忘记密码？</span>
                    <el-form-item>
                        <el-button type="primary" @click.prevent="doLogin()">登 录</el-button>
                    </el-form-item>
                </el-form>
                <!-- 忘记密码表单：isForgotPassword为true时显示 -->
                <div v-if="isForgotPassword" class="forgot-form">
                    <div class="forgot-desc">
                        请填写您的公司邮箱地址，我们会将新的随机密码发送给您的邮箱。
                    </div>
                    <el-form>
                        <el-form-item>
                            <el-icon>
                                <Message />
                            </el-icon>
                            <el-input v-model="forgotPassword_req.email" placeholder="请输入邮箱地址" />
                        </el-form-item>
                    </el-form>
                    <span @click="isForgotPassword = false">返回登录</span>
                    <el-form-item>
                        <el-button type="primary" @click.prevent="passwordReset()" class="!ml-0" :plain="true" @click="open2">重置密码</el-button>
                    </el-form-item>
                </div>
            </el-card>
        </div>
    </el-watermark>
</template>

<style scoped>
.login-container {
    position: relative;
    z-index: 10;
    width: 100vw;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
}

.login-card {
    position: relative;
    z-index: 20;
    background-color: white !important;
    width: 425px;
    height: 300px;
    border-radius: 15px;
    display: flex;
    justify-content: center;
    padding: 20px 0;
    opacity: 0;
    transform: translateY(-10px);
    animation: cardFadeIn 0.5s ease-in-out forwards;
    animation-delay: 0.1s;

    /* 初始阴影 - 黑色，中等范围 */
    box-shadow:
        0 15px 40px rgba(0, 0, 0, 0.3),
        0 5px 15px rgba(0, 0, 0, 0.15) !important;

    /* 平滑过渡效果 */
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.login-card:hover {
    /* 悬停时：阴影加深、范围变大、颜色变为深蓝 */
    box-shadow:
        0 25px 80px rgba(0, 57, 190, 0.4),
        0 15px 45px rgba(0, 57, 190, 0.35),
        0 8px 25px rgba(0, 57, 190, 0.3),
        0 0 0 1px rgba(0, 57, 190, 0.1) !important;

    /* 轻微上浮效果增强交互感 */
    transform: translateY(-8px);
}

/* 定义动画关键帧：从初始状态到最终状态 */
@keyframes cardFadeIn {
    0% {
        opacity: 0;
        transform: translateY(-10px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

.particles-bg {
    position: absolute;
    width: 100vw;
    height: 100vh;
    z-index: -1;
}

.el-card .title {
    display: flex;
    justify-content: center;
    font-size: 32px;
    text-shadow: 2px 2px 5px rgb(197, 197, 197);
    color: rgb(0, 0, 132);
    font-weight: bold;
    margin-top: 8px;
    margin-bottom: 30px;
}

.el-form-item {
    margin-top: 18px;
}

.el-form-item .el-icon {
    width: 30px;
    height: 30px;
    font-size: 24px;
    color: rgb(0, 0, 173);
}

.view {
    width: 23px;
    height: 23px;
    font-size: 24px;
    color: rgb(0, 0, 173);
    cursor: pointer;
    transition: all 0.3s ease;
}

.view:hover {
    color: #8700d6;
    transform: scale(1.1);
}

.el-form-item .el-input {
    width: 340px;
    height: 30px;
    color: black;
}

span {
    color: rgb(0, 100, 182);
    font-size: 11px;
    margin-left: 3px;
    cursor: pointer;
}

span:hover {
    color: rgb(0, 17, 69);
    text-decoration: none;
}

.demo-form-inline .el-input {
    --el-input-width: 220px;
}

.el-button {
    width: 370px;
    height: 40px;
    border-radius: 10px;
    color: white;
    font-weight: bold;
    font-size: 15px;
    background-color: rgb(0, 57, 190);
}

.forgot-form {
    width: 370px;
}

.forgot-desc {
    width: 100%;
    white-space: normal;
    word-wrap: break-word;
    margin: -8px 0 15px 0;
    font-size: 14.5px;
}

/* 深度选择器：覆盖 Element Plus 内部水印渲染层的层级 */
:deep(.el-watermark__content) {
    z-index: -1 !important;
    position: absolute !important;
    top: 0 !important;
    left: 0 !important;
    width: 100% !important;
    height: 100% !important;
}

/* 输入框文字颜色设置 */
:deep(.el-input__inner) {
    color: #333 !important;
    --el-input-placeholder-color: #333;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';
import type { ForgotPasswordReq, UserLoginReq, UserLoginRsp } from '../models';
import axios from '../utils/request';
import { useCurUserStore } from '../stores/user'
import router from '../router';
import { User, Lock, Message, View, Hide } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const isForgotPassword = ref(false); // 控制表单切换的变量
const showPassword = ref(false);// 控制密码显示/隐藏的变量
const store = useCurUserStore();
let { curUser } = store;

let usernameOrEmail = ref<string>('');

let login_req = ref<UserLoginReq>({
    user: {
        email: '',
        password: '',
        username: ''
    }
});

let login_rsp = ref<UserLoginRsp>({
    user: {
        username: '',
        email: '',
        employeeId: 0,
        token: '',
        userType: ''
    }
})

let forgotPassword_req = ref<ForgotPasswordReq>({
    email: ''
})

const open2 = () => {
  ElMessage({
    message: '重置密码成功,请您在邮件中及时查收！',
    type: 'success',
  })
}

// 切换密码可见性
const togglePasswordVisibility = () => {
    showPassword.value = !showPassword.value;
}

function passwordReset() {
    axios.post('/forgotPassword', forgotPassword_req.value).then(res => {
        console.log(res);
    })
}

function doLogin() {
    login_req.value.user.username = usernameOrEmail.value;
    login_req.value.user.email = usernameOrEmail.value;
    axios.post('/users/login', login_req.value).then(res => {
        login_rsp.value = res.data;
        console.log(login_rsp.value);
        store.setCurUser(login_rsp.value.user);
        router.push({ name: 'mainpage' });
    })
}
</script>