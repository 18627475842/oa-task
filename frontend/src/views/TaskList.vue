<template>
  <div class="task-list-page">
    <div class="container">
      <div class="page-header">
        <h1>任务列表</h1>
        <div class="filters">
          <select v-model="filterType" class="filter-select">
            <option value="all">全部任务</option>
            <option value="created">我创建的</option>
            <option value="assigned">分配给我的</option>
          </select>
          <select v-model="filterStatus" class="filter-select">
            <option value="">全部状态</option>
            <option value="PENDING">待处理</option>
            <option value="IN_PROGRESS">进行中</option>
            <option value="COMPLETED">已完成</option>
            <option value="ACCEPTED">已验收</option>
          </select>
        </div>
      </div>
      <div class="task-grid">
        <TaskCard
          v-for="task in filteredTasks"
          :key="task.id"
          :task="task"
          @click="router.push(`/tasks/${task.id}`)"
        />
        <div v-if="filteredTasks.length === 0" class="empty">暂无任务</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { taskApi } from '../api/task'
import TaskCard from '../components/TaskCard.vue'

const router = useRouter()
const filterType = ref('all')
const filterStatus = ref('')
const tasks = ref([])

const filteredTasks = computed(() => {
  let result = tasks.value
  if (filterType.value === 'created') {
    result = result.filter(t => t.leaderId === authStore.user?.id)
  } else if (filterType.value === 'assigned') {
    result = result.filter(t => t.assigneeId === authStore.user?.id)
  }
  if (filterStatus.value) {
    result = result.filter(t => t.status === filterStatus.value)
  }
  return result
})

import { useAuthStore } from '../stores/auth'
const authStore = useAuthStore()

onMounted(async () => {
  const res = await taskApi.getTasks()
  if (res.code === 200) tasks.value = res.data
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.filters {
  display: flex;
  gap: 12px;
}

.filter-select {
  width: auto;
  min-width: 120px;
}

.task-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.empty {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px;
  color: var(--text-secondary);
  font-size: 16px;
}
</style>
