<template>
  <span :class="['status-tag', statusClass]">{{ statusText }}</span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true
  }
})

const statusClass = computed(() => {
  const map = {
    'PENDING': 'status-pending',
    'IN_PROGRESS': 'status-progress',
    'COMPLETED': 'status-completed',
    'ACCEPTED': 'status-accepted'
  }
  return map[props.status] || 'status-default'
})

const statusText = computed(() => {
  const map = {
    'PENDING': '待处理',
    'IN_PROGRESS': '进行中',
    'COMPLETED': '已完成',
    'ACCEPTED': '已验收'
  }
  return map[props.status] || props.status
})
</script>

<style scoped>
.status-tag {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-pending {
  background: #fff7e6;
  color: #fa8c16;
}

.status-progress {
  background: #e6f7ff;
  color: #1890ff;
}

.status-completed {
  background: #f6ffed;
  color: #52c41a;
}

.status-accepted {
  background: #d9f7be;
  color: #389e0d;
}
</style>
