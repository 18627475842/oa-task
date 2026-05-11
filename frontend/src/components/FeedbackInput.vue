<template>
  <div class="feedback-input">
    <textarea
      v-model="content"
      placeholder="添加反馈..."
      rows="2"
      @keydown.ctrl.enter="handleSubmit"
    ></textarea>
    <div class="input-footer">
      <input
        v-if="showTagInput"
        v-model="tag"
        placeholder="标签"
        class="tag-input"
      />
      <button class="btn-tag" @click="showTagInput = !showTagInput">+ 标签</button>
      <button class="btn-submit btn-primary" @click="handleSubmit" :disabled="!content.trim()">
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['submit'])

const content = ref('')
const tag = ref('')
const showTagInput = ref(false)

const handleSubmit = () => {
  if (!content.value.trim()) return
  emit('submit', {
    userId: props.userId,
    content: content.value.trim(),
    tag: tag.value.trim() || undefined
  })
  content.value = ''
  tag.value = ''
  showTagInput.value = false
}
</script>

<style scoped>
.feedback-input {
  margin-top: 16px;
}

textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  resize: none;
  font-family: inherit;
}

.input-footer {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
}

.tag-input {
  width: 100px;
  padding: 6px 10px;
}

.btn-tag {
  padding: 6px 12px;
  background: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.btn-submit {
  margin-left: auto;
}
</style>
