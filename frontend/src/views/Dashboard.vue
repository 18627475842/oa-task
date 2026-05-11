<template>
  <div class="dashboard">
    <div class="container">
      <h1>欢迎回来，{{ user?.realName || user?.username }}</h1>
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">{{ stats.created }}</div>
          <div class="stat-label">我创建的任务</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.assigned }}</div>
          <div class="stat-label">分配给我的任务</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.inProgress }}</div>
          <div class="stat-label">进行中的任务</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.completed }}</div>
          <div class="stat-label">已完成的任务</div>
        </div>
      </div>
      <div class="section">
        <h2>最近任务</h2>
        <div class="task-list">
          <TaskCard
            v-for="task in recentTasks"
            :key="task.id"
            :task="task"
            @click="router.push(`/tasks/${task.id}`)"
          />
          <div v-if="recentTasks.length === 0" class="empty">暂无任务</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { taskApi } from '../api/task'
import TaskCard from '../components/TaskCard.vue'

const router = useRouter()
const authStore = useAuthStore()
const user = computed(() => authStore.user)

const createdTasks = ref([])
const assignedTasks = ref([])

const stats = computed(() => ({
  created: createdTasks.value.length,
  assigned: assignedTasks.value.length,
  inProgress: assignedTasks.value.filter(t => t.status === 'IN_PROGRESS').length,
  completed: [...createdTasks.value, ...assignedTasks.value].filter(t => t.status === 'COMPLETED' || t.status === 'ACCEPTED').length
}))

const recentTasks = computed(() => {
  const all = [...createdTasks.value, ...assignedTasks.value]
  return all.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)).slice(0, 5)
})

onMounted(async () => {
  const [createdRes, assignedRes] = await Promise.all([
    taskApi.getCreatedTasks(),
    taskApi.getAssignedTasks()
  ])
  if (createdRes.code === 200) createdTasks.value = createdRes.data
  if (assignedRes.code === 200) assignedTasks.value = assignedRes.data
})
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin: 24px 0;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: var(--primary-color);
}

.stat-label {
  margin-top: 8px;
  color: var(--text-secondary);
  font-size: 14px;
}

.section {
  margin-top: 32px;
}

.section h2 {
  margin-bottom: 16px;
}

.task-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.empty {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
}
</style>
