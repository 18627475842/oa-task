<template>
  <div class="task-create">
    <div class="container">
      <h1>创建任务</h1>
      <form @submit.prevent="handleSubmit" class="task-form">
        <div class="form-group">
          <label>任务标题 *</label>
          <input v-model="form.title" type="text" placeholder="请输入任务标题" required />
        </div>
        <div class="form-group">
          <label>任务描述</label>
          <textarea v-model="form.description" rows="4" placeholder="请输入任务描述"></textarea>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>执行人</label>
            <select v-model="form.assigneeId">
              <option value="">请选择执行人</option>
              <option v-for="user in users" :key="user.id" :value="user.id">
                {{ user.realName || user.username }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>优先级</label>
            <select v-model="form.priority">
              <option value="LOW">低</option>
              <option value="MEDIUM">中</option>
              <option value="HIGH">高</option>
            </select>
          </div>
          <div class="form-group">
            <label>截止时间</label>
            <input v-model="form.deadline" type="datetime-local" />
          </div>
        </div>
        <div class="form-actions">
          <button type="button" class="btn" @click="router.back()">取消</button>
          <button type="submit" class="btn btn-primary" :disabled="loading">创建任务</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { taskApi } from '../api/task'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const users = ref([])
const form = reactive({
  title: '',
  description: '',
  assigneeId: '',
  priority: 'MEDIUM',
  deadline: ''
})

onMounted(async () => {
  const res = await request.get('/users')
  if (res.code === 200) users.value = res.data
})

const handleSubmit = async () => {
  loading.value = true
  try {
    const data = { ...form }
    if (data.deadline) {
      data.deadline = data.deadline.replace('T', ' ') + ':00'
    }
    const res = await taskApi.createTask(data)
    if (res.code === 200) {
      router.push('/tasks')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.task-form {
  background: white;
  padding: 24px;
  border-radius: 8px;
  max-width: 700px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

textarea {
  resize: vertical;
}
</style>
