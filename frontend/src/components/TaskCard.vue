<template>
  <div class="task-card card" @click="$emit('click')">
    <div class="task-header">
      <h3>{{ task.title }}</h3>
      <StatusTag :status="task.status" />
    </div>
    <p class="task-desc">{{ task.description || '暂无描述' }}</p>
    <div class="task-meta">
      <span v-if="task.assigneeName">负责人: {{ task.assigneeName }}</span>
      <span v-if="task.deadline">截止: {{ formatDate(task.deadline) }}</span>
      <span>优先级: {{ getPriorityText(task.priority) }}</span>
    </div>
    <div class="task-progress">
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: task.progress + '%' }"></div>
      </div>
      <span class="progress-text">{{ task.progress }}%</span>
    </div>
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
import StatusTag from './StatusTag.vue'

defineProps({
  task: {
    type: Object,
    required: true
  }
})

defineEmits(['click'])

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const getPriorityText = (priority) => {
  const map = { 'HIGH': '高', 'MEDIUM': '中', 'LOW': '低' }
  return map[priority] || priority
}
</script>

<style scoped>
.task-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.task-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.task-header h3 {
  font-size: 16px;
  margin: 0;
  flex: 1;
  margin-right: 12px;
}

.task-desc {
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.task-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: var(--text-secondary);
  margin-bottom: 12px;
}

.task-progress {
  display: flex;
  align-items: center;
  gap: 12px;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: var(--primary-color);
  transition: width 0.3s;
}

.progress-text {
  font-size: 12px;
  color: var(--text-secondary);
  min-width: 40px;
}
</style>
