<template>
  <div class="feedback-list">
    <div v-for="feedback in feedbacks" :key="feedback.id" class="feedback-item">
      <div class="feedback-header">
        <img v-if="feedback.userAvatar" :src="feedback.userAvatar" class="avatar" />
        <span class="username">{{ feedback.userName || '用户' }}</span>
        <span class="tag" v-if="feedback.tag">{{ feedback.tag }}</span>
        <span class="time">{{ formatTime(feedback.createdAt) }}</span>
      </div>
      <div class="feedback-content">{{ feedback.content }}</div>
    </div>
    <div v-if="feedbacks.length === 0" class="empty">暂无反馈</div>
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'

dayjs.extend(relativeTime)

defineProps({
  feedbacks: {
    type: Array,
    default: () => []
  }
})

const formatTime = (time) => {
  return dayjs(time).fromNow()
}
</script>

<style scoped>
.feedback-list {
  margin-top: 16px;
}

.feedback-item {
  padding: 12px;
  border-bottom: 1px solid var(--border-color);
}

.feedback-item:last-child {
  border-bottom: none;
}

.feedback-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
}

.username {
  font-weight: 500;
  font-size: 14px;
}

.tag {
  padding: 2px 8px;
  background: var(--primary-color);
  color: white;
  border-radius: 10px;
  font-size: 12px;
}

.time {
  margin-left: auto;
  font-size: 12px;
  color: var(--text-secondary);
}

.feedback-content {
  font-size: 14px;
  line-height: 1.6;
  color: var(--text-color);
}

.empty {
  text-align: center;
  padding: 20px;
  color: var(--text-secondary);
}
</style>
