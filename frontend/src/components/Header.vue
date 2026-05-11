<template>
  <header class="header">
    <div class="header-content">
      <div class="logo">TaskSync</div>
      <nav class="nav">
        <router-link to="/">首页</router-link>
        <router-link to="/tasks">任务</router-link>
        <router-link to="/tasks/create">创建任务</router-link>
      </nav>
      <div class="user-info">
        <span>{{ user?.realName || user?.username }}</span>
        <button @click="handleLogout" class="btn-logout">退出</button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const user = computed(() => authStore.user)

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-color);
}

.nav {
  display: flex;
  gap: 24px;
}

.nav a {
  color: var(--text-color);
  text-decoration: none;
  padding: 8px 0;
}

.nav a.router-link-active {
  color: var(--primary-color);
  border-bottom: 2px solid var(--primary-color);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-logout {
  padding: 6px 12px;
  background: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
