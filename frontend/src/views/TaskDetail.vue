<template>
  <div class="task-detail">
    <div class="container">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="task" class="detail-content">
        <div class="detail-header">
          <h1>{{ task.title }}</h1>
          <StatusTag :status="task.status" />
        </div>
        <div class="detail-meta">
          <span>负责人: {{ task.leaderName }}</span>
          <span>执行人: {{ task.assigneeName || '未分配' }}</span>
          <span>优先级: {{ getPriorityText(task.priority) }}</span>
          <span>截止: {{ formatDate(task.deadline) }}</span>
        </div>
        <div class="detail-body">
          <div class="description-section">
            <h3>任务描述</h3>
            <p>{{ task.description || '暂无描述' }}</p>
          </div>
          <div class="progress-section">
            <h3>进度</h3>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: task.progress + '%' }"></div>
            </div>
            <span>{{ task.progress }}%</span>
          </div>
          <div class="status-actions" v-if="task.assigneeId === authStore.user?.id">
            <button
              v-if="task.status === 'PENDING'"
              class="btn btn-primary"
              @click="updateStatus('IN_PROGRESS')"
            >开始任务</button>
            <button
              v-if="task.status === 'IN_PROGRESS'"
              class="btn btn-success"
              @click="updateStatus('COMPLETED')"
            >完成任务</button>
          </div>
          <div v-if="task.leaderId === authStore.user?.id && task.status === 'COMPLETED'" class="status-actions">
            <button class="btn btn-success" @click="updateStatus('ACCEPTED')">验收通过</button>
          </div>
        </div>
        <div class="comments-section">
          <h3>评论</h3>
          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <span class="comment-user">{{ comment.userId }}</span>
              <span class="comment-content">{{ comment.content }}</span>
              <span class="comment-time">{{ formatTime(comment.createdAt) }}</span>
            </div>
            <div v-if="comments.length === 0" class="empty">暂无评论</div>
          </div>
          <div class="comment-input">
            <textarea v-model="newComment" placeholder="添加评论..." rows="2"></textarea>
            <button class="btn btn-primary" @click="addComment" :disabled="!newComment.trim()">发送</button>
          </div>
        </div>
        <div class="feedbacks-section">
          <h3>反馈</h3>
          <FeedbackList :feedbacks="feedbacks" />
          <FeedbackInput :userId="authStore.user?.id" @submit="addFeedback" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { taskApi } from '../api/task'
import { feedbackApi } from '../api/feedback'
import StatusTag from '../components/StatusTag.vue'
import FeedbackList from '../components/FeedbackList.vue'
import FeedbackInput from '../components/FeedbackInput.vue'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'

dayjs.extend(relativeTime)

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const task = ref(null)
const comments = ref([])
const feedbacks = ref([])
const newComment = ref('')
const loading = ref(true)

const formatDate = (date) => date ? dayjs(date).format('YYYY-MM-DD HH:mm') : '-'
const formatTime = (time) => dayjs(time).fromNow()
const getPriorityText = (p) => ({ 'HIGH': '高', 'MEDIUM': '中', 'LOW': '低' }[p] || p)

const loadTask = async () => {
  const res = await taskApi.getTaskById(route.params.id)
  if (res.code === 200) task.value = res.data
}

const loadComments = async () => {
  const res = await taskApi.getComments(route.params.id)
  if (res.code === 200) comments.value = res.data
}

const loadFeedbacks = async () => {
  const res = await feedbackApi.getFeedbacks(route.params.id)
  if (res.code === 200) feedbacks.value = res.data
}

const updateStatus = async (status) => {
  await taskApi.updateTask(task.value.id, { status })
  await loadTask()
}

const addComment = async () => {
  await taskApi.createComment(task.value.id, newComment.value)
  newComment.value = ''
  await loadComments()
}

const addFeedback = async (data) => {
  await feedbackApi.createFeedback(task.value.id, data)
  await loadFeedbacks()
}

onMounted(async () => {
  await Promise.all([loadTask(), loadComments(), loadFeedbacks()])
  loading.value = false
})
</script>

<style scoped>
.detail-content {
  background: white;
  padding: 24px;
  border-radius: 8px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.detail-meta {
  display: flex;
  gap: 24px;
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-color);
}

.detail-body {
  margin-bottom: 24px;
}

.description-section, .progress-section, .status-actions, .comments-section, .feedbacks-section {
  margin-bottom: 24px;
}

.progress-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
}

.progress-fill {
  height: 100%;
  background: var(--primary-color);
}

.comment-item {
  padding: 12px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  gap: 12px;
}

.comment-user {
  font-weight: 500;
  color: var(--primary-color);
}

.comment-time {
  margin-left: auto;
  color: var(--text-secondary);
  font-size: 12px;
}

.comment-input {
  display: flex;
  gap: 12px;
  margin-top: 12px;
}

.comment-input textarea {
  flex: 1;
}
</style>
