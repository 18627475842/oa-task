<template>
  <div class="login-container">
    <div class="login-box">
      <h1>TaskSync</h1>
      <p class="subtitle">任务协作平台</p>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" required />
        </div>
        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ isRegister ? '注册' : '登录' }}
        </button>
      </form>
      <p class="switch-mode">
        {{ isRegister ? '已有账号？' : '没有账号？' }}
        <a href="#" @click.prevent="isRegister = !isRegister">
          {{ isRegister ? '登录' : '注册' }}
        </a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const isRegister = ref(false)
const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
  realName: ''
})

const handleSubmit = async () => {
  loading.value = true
  try {
    if (isRegister.value) {
      const res = await authStore.register({
        username: form.username,
        password: form.password,
        realName: form.realName || form.username
      })
      if (res.code === 200) {
        router.push('/')
      }
    } else {
      const res = await authStore.login(form.username, form.password)
      if (res.code === 200) {
        router.push('/')
      }
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
  width: 380px;
}

.login-box h1 {
  text-align: center;
  color: var(--primary-color);
  margin-bottom: 8px;
}

.subtitle {
  text-align: center;
  color: var(--text-secondary);
  margin-bottom: 32px;
}

.btn-block {
  width: 100%;
  padding: 12px;
  font-size: 16px;
}

.switch-mode {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
}

.switch-mode a {
  color: var(--primary-color);
}
</style>
