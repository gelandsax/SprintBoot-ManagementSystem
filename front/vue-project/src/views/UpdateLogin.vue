<template>
  <div class="pageTitle">
    <div class="title">修改登录密码</div>
    <div class="subTitle">
      <el-icon>
        <HomeFilled />
      </el-icon>
      <RouterLink :to="'/main'" style="text-decoration: none; color: black;"> 首页</RouterLink>
      <span style="color: gray;">
        <el-icon>
          <ArrowRight />
        </el-icon> 修改登录密码
      </span>
    </div>
  </div>
  <!-- --------------------- -->
  <div class="container">
    <el-card>
      <template #header>
        <div>
          <span>员工信息</span>
        </div>
      </template>

      <el-form ref="formRef" class="demo-form-inline" size="large">
        <el-form-item class="twoInput">
          <p>*</p>
          <span>原密码</span>
          <div class="input-wrapper">
            <el-input v-model="changepassword.oldPassword" type="text" @focus="handleFocus('oldPassword')"
              @blur="handleBlur('oldPassword')" @mouseenter="showClearIcon.oldPassword = true"
              @mouseleave="showClearIcon.oldPassword = false" :class="{ 'error-border': errors.oldPassword }">
              <template #suffix>
                <div class="suffix-icons">
                  <!-- 清空图标 -->
                  <el-icon v-if="showClearIcon.oldPassword && changepassword.oldPassword"
                    @click="clearField('oldPassword')" class="clear-icon">
                    <CircleClose />
                  </el-icon>

                  <!-- 验证状态图标 -->
                  <el-icon v-if="fieldTouched.oldPassword && changepassword.oldPassword"
                    :class="errors.oldPassword ? 'error-icon' : 'success-icon'" class="status-icon">
                    <CircleClose v-if="errors.oldPassword" />
                    <CircleCheck v-else />
                  </el-icon>
                </div>
              </template>
            </el-input>
            <div v-if="errors.oldPassword" class="error-message">{{ errors.oldPassword }}</div>
          </div>
        </el-form-item>

        <el-form-item class="twoInput">
          <p>*</p>
          <span>新密码</span>
          <div class="input-wrapper">
            <el-input v-model="changepassword.newPassword" :type="showPassword ? 'text' : 'password'"
              @focus="handleFocus('newPassword')" @blur="handleBlur('newPassword')"
              @mouseenter="showClearIcon.newPassword = true" @mouseleave="showClearIcon.newPassword = false"
              :class="{ 'error-border': errors.newPassword }">
              <template #suffix>
                <div class="suffix-icons">
                  <!-- 清空图标 -->
                  <el-icon v-if="showClearIcon.newPassword && changepassword.newPassword"
                    @click="clearField('newPassword')" class="clear-icon">
                    <CircleClose />
                  </el-icon>

                  <!-- 眼睛图标 -->
                  <el-icon @click="showPassword = !showPassword" class="eye-icon">
                    <View v-if="showPassword" />
                    <Hide v-else />
                  </el-icon>

                  <!-- 验证状态图标 -->
                  <el-icon v-if="fieldTouched.newPassword && changepassword.newPassword"
                    :class="errors.newPassword ? 'error-icon' : 'success-icon'" class="status-icon">
                    <CircleClose v-if="errors.newPassword" />
                    <CircleCheck v-else />
                  </el-icon>
                </div>
              </template>
            </el-input>
            <div v-if="errors.newPassword" class="error-message">{{ errors.newPassword }}</div>
          </div>
        </el-form-item>

        <el-form-item class="thirdInput">
          <p>*</p>
          <span>确认新密码</span>
          <div class="input-wrapper">
            <el-input v-model="confirmPassword" :type="showConfirmPassword ? 'text' : 'password'"
              @focus="handleFocus('confirmPassword')" @blur="handleBlur('confirmPassword')"
              @mouseenter="showClearIcon.confirmPassword = true" @mouseleave="showClearIcon.confirmPassword = false"
              :class="{ 'error-border': errors.confirmPassword }">
              <template #suffix>
                <div class="suffix-icons">
                  <!-- 清空图标 -->
                  <el-icon v-if="showClearIcon.confirmPassword && confirmPassword"
                    @click="clearField('confirmPassword')" class="clear-icon">
                    <CircleClose />
                  </el-icon>

                  <!-- 眼睛图标 -->
                  <el-icon @click="showConfirmPassword = !showConfirmPassword" class="eye-icon">
                    <View v-if="showConfirmPassword" />
                    <Hide v-else />
                  </el-icon>

                  <!-- 验证状态图标 -->
                  <el-icon v-if="fieldTouched.confirmPassword && confirmPassword"
                    :class="errors.confirmPassword ? 'error-icon' : 'success-icon'" class="status-icon">
                    <CircleClose v-if="errors.confirmPassword" />
                    <CircleCheck v-else />
                  </el-icon>
                </div>
              </template>
            </el-input>
            <div v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</div>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button type="primary" size="large" @click="handleSubmit">提交</el-button>
        <el-button type="primary" size="large" @click="handleReset">重置</el-button>
      </template>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, nextTick } from 'vue';
import { useCurUserStore } from '../stores/user';
import { storeToRefs } from 'pinia';
import router from '../router';
import type { ChangePasswordReq } from '@/models';
import axios from '../utils/request';
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Action } from 'element-plus'

const store = useCurUserStore();
const { curUser } = storeToRefs(store);

// 使用第二个代码的数据模型
const confirmPassword = ref('')
let changepassword = ref<ChangePasswordReq>({
  newPassword: '',
  oldPassword: ''
})

// 显示/隐藏密码
const showPassword = ref(false);
const showConfirmPassword = ref(false);

// 状态管理
const fieldTouched = reactive({
  oldPassword: false,
  newPassword: false,
  confirmPassword: false
});

const showClearIcon = reactive({
  oldPassword: false,
  newPassword: false,
  confirmPassword: false
});

const errors = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 处理焦点事件
const handleFocus = (field: keyof typeof fieldTouched) => {
  if (!fieldTouched[field]) {
    fieldTouched[field] = true;
  }
};

// 处理失去焦点事件
const handleBlur = (field: string) => {
  validateField(field);
};

// 验证字段
const validateField = (field: string) => {
  if (field === 'oldPassword') {
    if (!changepassword.value.oldPassword && fieldTouched.oldPassword) {
      errors.oldPassword = '请输入原密码';
    } else {
      errors.oldPassword = '';
    }
  } else if (field === 'newPassword') {
    if (!changepassword.value.newPassword && fieldTouched.newPassword) {
      errors.newPassword = '请输入新密码';
    } else if (changepassword.value.newPassword && changepassword.value.newPassword.length < 6) {
      errors.newPassword = '密码长度不能少于6位';
    } else {
      errors.newPassword = '';
    }
  } else if (field === 'confirmPassword') {
    if (!confirmPassword.value && fieldTouched.confirmPassword) {
      errors.confirmPassword = '请确认新密码';
    } else if (confirmPassword.value !== changepassword.value.newPassword) {
      errors.confirmPassword = '两次密码输入不同';
    } else {
      errors.confirmPassword = '';
    }
  }
};

// 清空字段
const clearField = (field: string) => {
  if (field === 'oldPassword') {
    changepassword.value.oldPassword = '';
  } else if (field === 'newPassword') {
    changepassword.value.newPassword = '';
  } else if (field === 'confirmPassword') {
    confirmPassword.value = '';
  }

  nextTick(() => {
    validateField(field);
  });
};

// 监听密码变化，实时验证
watch(() => changepassword.value.newPassword, () => {
  if (confirmPassword.value && fieldTouched.confirmPassword) {
    validateField('confirmPassword');
  }
  validateField('newPassword');
});

watch(() => confirmPassword.value, () => {
  if (fieldTouched.confirmPassword) {
    validateField('confirmPassword');
  }
});

watch(() => changepassword.value.oldPassword, () => {
  if (fieldTouched.oldPassword) {
    validateField('oldPassword');
  }
});

// 提交表单
const handleSubmit = async () => {
  // 标记所有字段为已触摸
  Object.keys(fieldTouched).forEach(key => {
    fieldTouched[key as keyof typeof fieldTouched] = true;
    validateField(key);
  });

  // 检查是否有错误
  const hasErrors = Object.values(errors).some(error => error !== '');

  if (!hasErrors) {
    // 表单验证通过，执行提交逻辑
    resetpassword();
  } else {
    console.log('表单验证失败');
  }
};

// 重置表单
const handleReset = () => {
  changepassword.value.newPassword = '';
  changepassword.value.oldPassword = '';
  confirmPassword.value = '';

  // 重置触摸状态
  Object.keys(fieldTouched).forEach(key => {
    fieldTouched[key as keyof typeof fieldTouched] = false;
  });

  // 重置错误状态
  Object.keys(errors).forEach(key => {
    errors[key as keyof typeof errors] = '';
  });

  // 重置密码显示状态
  showPassword.value = false;
  showConfirmPassword.value = false;
};

// 第二个代码的密码重置函数
function resetpassword() {
  axios.put('changePassWord', changepassword.value).then(
    () => {
      ElMessageBox.alert('密码修改成功', '提示', {
        confirmButtonText: '确定',
        callback: (action: Action) => {
          ElMessage({
            type: 'success',
            message: '密码修改成功',
          })
          // 在消息框关闭后执行路由跳转
          store.userLogOut()
          router.push({ name: 'login' }).then(() => {
            window.location.reload();
          });
        },
      })
    }
  ).catch(error => {
    ElMessage({
      type: 'error',
      message: '密码修改失败，请检查原密码是否正确',
    })
  })
}
</script>

<style scoped>
.container {
  padding: 5px 20px;
  background-color: rgb(133, 226, 200);
  margin: 10px 20px;
  border-radius: 15px;
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

.el-form-item {
  margin: -10px 0 -10px 0;
  display: flex;
  align-items: flex-start;
}

.input-wrapper {
  position: relative;
  display: flex;
  flex-direction: column;
}

.el-input {
  --el-input-width: 500px;
  margin-left: 10px;
}

.el-card .span {
  font-size: 14px;
  margin-right: 10px;
}

p {
  color: red;
  margin-right: 3px;
  margin-top: 8px;
}

.twoInput {
  margin: 2px 0 2px 28px;
}

.thirdInput {
  margin: 2px 0 6px 0;
}

/* 错误消息样式 - 紧挨输入框下方靠左，无空隙 */
.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 10px;
  line-height: 0.5;
}

/* 错误边框 */
.error-border :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #f56c6c inset !important;
}

/* 图标容器样式 - 关键修复 */
.suffix-icons {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  width: 100%;
  gap: 8px;
  margin-left: auto;
}

/* 清空图标样式 - 永远在最左侧 */
.clear-icon {
  color: #c0c4cc;
  cursor: pointer;
  transition: color 0.2s;
  order: 1;
  flex-shrink: 0;
}

.clear-icon:hover {
  color: #f56c6c;
}

/* 眼睛图标样式 - 中间位置 */
.eye-icon {
  color: #c0c4cc;
  cursor: pointer;
  transition: color 0.2s;
  order: 2;
  flex-shrink: 0;
}

.eye-icon:hover {
  color: #409eff;
}

/* 状态图标样式 - 永远在最右侧 */
.status-icon {
  order: 3;
  flex-shrink: 0;
}

.error-icon {
  color: #f56c6c;
}

.success-icon {
  color: #67c23a;
}

/* 隐藏 Element Plus 自带的错误提示 */
:deep(.el-form-item__error) {
  display: none;
}

/* 调整 suffix 区域布局 */
:deep(.el-input__suffix) {
  display: flex !important;
  align-items: center !important;
  justify-content: flex-end !important;
  position: relative !important;
  right: 0 !important;
}

/* 确保图标正确对齐 */
:deep(.el-input__suffix-inner) {
  display: flex !important;
  align-items: center !important;
  justify-content: flex-end !important;
  width: 100% !important;
}

/* 修复图标排列问题 */
:deep(.el-icon) {
  display: inline-flex !important;
  align-items: center !important;
  justify-content: center !important;
}
</style>