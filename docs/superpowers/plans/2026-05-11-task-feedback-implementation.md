# 任务反馈功能实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在任务协同管理系统中新增即时反馈模块，支持下属提交带标签的任务反馈，支持@提及功能。

**Architecture:** 采用前后端分离架构，后端新增 Feedback 相关类，前端新增 FeedbackList 和 FeedbackInput 组件，集成到任务详情页。

**Tech Stack:** Spring Boot 3.0 + MyBatis-Plus + Vue 3 + Element Plus

---

## 文件结构

```
backend/src/main/java/com/tasksync/
├── entity/
│   └── Feedback.java          # 反馈实体类
├── mapper/
│   └── FeedbackMapper.java    # 反馈Mapper接口
├── service/
│   └── FeedbackService.java   # 反馈服务类
├── service/impl/
│   └── FeedbackServiceImpl.java
└── controller/
    └── FeedbackController.java

frontend/src/
├── components/
│   ├── FeedbackList.vue       # 反馈列表组件
│   └── FeedbackInput.vue      # 反馈输入组件
├── views/
│   └── TaskDetail.vue         # 修改：集成反馈区域
└── api/
    └── feedback.js            # 反馈API封装
```

---

## 实施任务

### Task 1: 创建后端 Feedback 实体类

**Files:**
- Create: `backend/src/main/java/com/tasksync/entity/Feedback.java`

- [ ] **Step 1: 创建 Feedback 实体类**

```java
package com.tasksync.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("task_feedbacks")
public class Feedback {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long taskId;
    
    private Long userId;
    
    private String content;
    
    private String tag;
    
    private String mentionedUsers;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(exist = false)
    private String userName;
    
    @TableField(exist = false)
    private String userAvatar;
}
```

- [ ] **Step 2: 提交代码**

```bash
git add backend/src/main/java/com/tasksync/entity/Feedback.java
git commit -m "feat: add Feedback entity class"
```

---

### Task 2: 创建 FeedbackMapper 接口

**Files:**
- Create: `backend/src/main/java/com/tasksync/mapper/FeedbackMapper.java`

- [ ] **Step 1: 创建 FeedbackMapper 接口**

```java
package com.tasksync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tasksync.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
```

- [ ] **Step 2: 提交代码**

```bash
git add backend/src/main/java/com/tasksync/mapper/FeedbackMapper.java
git commit -m "feat: add FeedbackMapper interface"
```

---

### Task 3: 创建 FeedbackService 服务类

**Files:**
- Create: `backend/src/main/java/com/tasksync/service/FeedbackService.java`
- Create: `backend/src/main/java/com/tasksync/service/impl/FeedbackServiceImpl.java`

- [ ] **Step 1: 创建 FeedbackService 接口**

```java
package com.tasksync.service;

import com.tasksync.entity.Feedback;
import java.util.List;

public interface FeedbackService {
    List<Feedback> getFeedbacksByTaskId(Long taskId);
    
    Feedback createFeedback(Feedback feedback);
    
    void deleteFeedback(Long feedbackId, Long userId);
}
```

- [ ] **Step 2: 创建 FeedbackServiceImpl 实现类**

```java
package com.tasksync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tasksync.entity.Feedback;
import com.tasksync.mapper.FeedbackMapper;
import com.tasksync.service.FeedbackService;
import com.tasksync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Autowired
    private UserService userService;

    @Override
    public List<Feedback> getFeedbacksByTaskId(Long taskId) {
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId).orderByDesc("created_at");
        List<Feedback> feedbacks = feedbackMapper.selectList(wrapper);
        
        for (Feedback feedback : feedbacks) {
            feedback.setUserName(userService.getUserById(feedback.getUserId()).getRealName());
            feedback.setUserAvatar(userService.getUserById(feedback.getUserId()).getAvatar());
        }
        
        return feedbacks;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        feedbackMapper.insert(feedback);
        return feedback;
    }

    @Override
    public void deleteFeedback(Long feedbackId, Long userId) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null && feedback.getUserId().equals(userId)) {
            feedbackMapper.deleteById(feedbackId);
        }
    }
}
```

- [ ] **Step 3: 提交代码**

```bash
git add backend/src/main/java/com/tasksync/service/FeedbackService.java
git add backend/src/main/java/com/tasksync/service/impl/FeedbackServiceImpl.java
git commit -m "feat: add FeedbackService with CRUD operations"
```

---

### Task 4: 创建 FeedbackController 控制器

**Files:**
- Create: `backend/src/main/java/com/tasksync/controller/FeedbackController.java`

- [ ] **Step 1: 创建 FeedbackController**

```java
package com.tasksync.controller;

import com.tasksync.common.Result;
import com.tasksync.entity.Feedback;
import com.tasksync.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public Result<List<Feedback>> getFeedbacks(@PathVariable Long taskId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByTaskId(taskId);
        return Result.success(feedbacks);
    }

    @PostMapping
    public Result<Feedback> createFeedback(@PathVariable Long taskId, 
                                           @RequestBody Feedback feedback,
                                           @RequestHeader("X-User-Id") Long userId) {
        feedback.setTaskId(taskId);
        feedback.setUserId(userId);
        Feedback created = feedbackService.createFeedback(feedback);
        return Result.success(created);
    }

    @DeleteMapping("/{feedbackId}")
    public Result<Void> deleteFeedback(@PathVariable Long taskId,
                                       @PathVariable Long feedbackId,
                                       @RequestHeader("X-User-Id") Long userId) {
        feedbackService.deleteFeedback(feedbackId, userId);
        return Result.success(null);
    }
}
```

- [ ] **Step 2: 提交代码**

```bash
git add backend/src/main/java/com/tasksync/controller/FeedbackController.java
git commit -m "feat: add FeedbackController with REST API endpoints"
```

---

### Task 5: 添加数据库迁移脚本

**Files:**
- Create: `backend/src/main/resources/migration/V1__create_task_feedbacks.sql`

- [ ] **Step 1: 创建数据库迁移脚本**

```sql
CREATE TABLE task_feedbacks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  task_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  tag ENUM('BLOCKING', 'PROGRESSING', 'NEED_RESOURCE', 'MILESTONE') DEFAULT 'PROGRESSING',
  mentioned_users VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (task_id) REFERENCES tasks(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_task_feedbacks_task_id ON task_feedbacks(task_id);
CREATE INDEX idx_task_feedbacks_created_at ON task_feedbacks(created_at DESC);
```

- [ ] **Step 2: 提交代码**

```bash
git add backend/src/main/resources/migration/V1__create_task_feedbacks.sql
git commit -m "feat: add database migration for task_feedbacks table"
```

---

### Task 6: 创建前端反馈 API 封装

**Files:**
- Create: `frontend/src/api/feedback.js`

- [ ] **Step 1: 创建 feedback API 文件**

```javascript
import request from '../utils/request'

export function getFeedbacks(taskId) {
  return request({
    url: `/api/tasks/${taskId}/feedbacks`,
    method: 'get'
  })
}

export function createFeedback(taskId, data) {
  return request({
    url: `/api/tasks/${taskId}/feedbacks`,
    method: 'post',
    data
  })
}

export function deleteFeedback(taskId, feedbackId) {
  return request({
    url: `/api/tasks/${taskId}/feedbacks/${feedbackId}`,
    method: 'delete'
  })
}
```

- [ ] **Step 2: 提交代码**

```bash
git add frontend/src/api/feedback.js
git commit -m "feat: add feedback API module"
```

---

### Task 7: 创建 FeedbackInput 组件

**Files:**
- Create: `frontend/src/components/FeedbackInput.vue`

- [ ] **Step 1: 创建 FeedbackInput 组件**

```vue
<template>
  <div class="feedback-input">
    <div class="tag-selector">
      <el-radio-group v-model="selectedTag" size="small">
        <el-radio-button label="PROGRESSING">
          <span class="tag-dot progress"></span>进展顺利
        </el-radio-button>
        <el-radio-button label="BLOCKING">
          <span class="tag-dot blocking"></span>遇到问题
        </el-radio-button>
        <el-radio-button label="NEED_RESOURCE">
          <span class="tag-dot resource"></span>需要资源
        </el-radio-button>
        <el-radio-button label="MILESTONE">
          <span class="tag-dot milestone"></span>已完成阶段
        </el-radio-button>
      </el-radio-group>
    </div>
    
    <el-input
      type="textarea"
      v-model="content"
      placeholder="输入反馈内容，支持 @提及..."
      :rows="3"
      @input="handleMentionInput"
    />
    
    <div class="mention-dropdown" v-if="showMentionDropdown">
      <div 
        class="mention-item" 
        v-for="user in filteredUsers" 
        :key="user.id"
        @click="selectMention(user)"
      >
        <el-avatar :size="24" :src="user.avatar">{{ user.name[0] }}</el-avatar>
        <span>{{ user.name }}</span>
      </div>
    </div>
    
    <el-button type="primary" size="default" @click="submitFeedback" :loading="loading">
      提交反馈
    </el-button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { createFeedback } from '../api/feedback'

const props = defineProps({
  taskId: { type: Number, required: true }
})

const emit = defineEmits(['refresh'])

const selectedTag = ref('PROGRESSING')
const content = ref('')
const loading = ref(false)
const showMentionDropdown = ref(false)
const mentionQuery = ref('')
const mentionStartIndex = ref(-1)

const allUsers = ref([])

const filteredUsers = computed(() => {
  if (!mentionQuery.value) return allUsers.value
  return allUsers.value.filter(u => 
    u.name.toLowerCase().includes(mentionQuery.value.toLowerCase())
  )
})

const handleMentionInput = () => {
  const text = content.value
  const cursorPos = text.length
  
  for (let i = cursorPos - 1; i >= 0; i--) {
    if (text[i] === '@') {
      mentionStartIndex.value = i
      showMentionDropdown.value = true
      mentionQuery.value = text.slice(i + 1, cursorPos)
      break
    }
    if (text[i] === ' ' || text[i] === '\n') break
  }
  
  if (mentionStartIndex.value === -1) {
    showMentionDropdown.value = false
  }
}

const selectMention = (user) => {
  const before = content.value.slice(0, mentionStartIndex.value)
  const after = content.value.slice(mentionStartIndex.value + mentionQuery.value.length + 1)
  content.value = before + '@' + user.name + ' ' + after
  showMentionDropdown.value = false
  mentionStartIndex.value = -1
}

const submitFeedback = async () => {
  if (!content.value.trim()) {
    ElMessage.warning('请输入反馈内容')
    return
  }
  
  loading.value = true
  try {
    const mentionedUsers = extractMentions(content.value)
    await createFeedback(props.taskId, {
      content: content.value,
      tag: selectedTag.value,
      mentionedUsers
    })
    content.value = ''
    ElMessage.success('反馈提交成功')
    emit('refresh')
  } catch (error) {
    ElMessage.error('提交失败')
  } finally {
    loading.value = false
  }
}

const extractMentions = (text) => {
  const mentions = []
  const regex = /@([^\s@]+)/g
  let match
  while ((match = regex.exec(text)) !== null) {
    const user = allUsers.value.find(u => u.name === match[1])
    if (user) mentions.push(user.id)
  }
  return mentions.join(',')
}
</script>

<style scoped>
.feedback-input {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 4px;
}

.tag-selector {
  margin-bottom: 12px;
}

.tag-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 4px;
}

.tag-dot.progress { background: #67C23A; }
.tag-dot.blocking { background: #F56C6C; }
.tag-dot.resource { background: #E6A23C; }
.tag-dot.milestone { background: #409EFF; }

.mention-dropdown {
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
  margin-top: 4px;
}

.mention-item {
  padding: 8px 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.mention-item:hover {
  background: #f5f7fa;
}

.el-button {
  margin-top: 12px;
  float: right;
}
</style>
```

- [ ] **Step 2: 提交代码**

```bash
git add frontend/src/components/FeedbackInput.vue
git commit -m "feat: add FeedbackInput component with tag selector and mention support"
```

---

### Task 8: 创建 FeedbackList 组件

**Files:**
- Create: `frontend/src/components/FeedbackList.vue`

- [ ] **Step 1: 创建 FeedbackList 组件**

```vue
<template>
  <div class="feedback-list">
    <div v-if="feedbacks.length === 0" class="empty-state">
      暂无反馈记录
    </div>
    
    <div v-else class="feedback-item" v-for="feedback in feedbacks" :key="feedback.id">
      <div class="feedback-header">
        <el-avatar :size="36" :src="feedback.userAvatar">
          {{ feedback.userName?.[0] }}
        </el-avatar>
        <div class="feedback-meta">
          <span class="user-name">{{ feedback.userName }}</span>
          <span class="feedback-time">{{ formatTime(feedback.createdAt) }}</span>
        </div>
        <div :class="['feedback-tag', tagClass(feedback.tag)]">
          {{ tagLabel(feedback.tag) }}
        </div>
        <el-button 
          v-if="feedback.userId === currentUserId" 
          type="danger" 
          size="small" 
          text
          @click="handleDelete(feedback.id)"
        >
          删除
        </el-button>
      </div>
      
      <div class="feedback-content" v-html="highlightMentions(feedback.content)"></div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteFeedback } from '../api/feedback'

const props = defineProps({
  feedbacks: { type: Array, default: () => [] },
  currentUserId: { type: Number, required: true }
})

const emit = defineEmits(['refresh'])

const tagLabel = (tag) => {
  const labels = {
    'BLOCKING': '遇到问题',
    'PROGRESSING': '进展顺利',
    'NEED_RESOURCE': '需要资源',
    'MILESTONE': '已完成阶段'
  }
  return labels[tag] || tag
}

const tagClass = (tag) => {
  const classes = {
    'BLOCKING': 'tag-blocking',
    'PROGRESSING': 'tag-progressing',
    'NEED_RESOURCE': 'tag-resource',
    'MILESTONE': 'tag-milestone'
  }
  return classes[tag] || ''
}

const formatTime = (time) => {
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
}

const highlightMentions = (content) => {
  return content.replace(/@([^\s@]+)/g, '<span class="mention">@$1</span>')
}

const handleDelete = async (feedbackId) => {
  try {
    await ElMessageBox.confirm('确定删除这条反馈吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteFeedback(props.feedbacks[0]?.taskId, feedbackId)
    ElMessage.success('删除成功')
    emit('refresh')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}
</script>

<style scoped>
.feedback-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.empty-state {
  text-align: center;
  color: #909399;
  padding: 24px;
}

.feedback-item {
  background: white;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 16px;
}

.feedback-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.feedback-meta {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.user-name {
  font-weight: 500;
  color: #303133;
}

.feedback-time {
  font-size: 12px;
  color: #909399;
}

.feedback-tag {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.tag-blocking { background: #fef0f0; color: #F56C6C; }
.tag-progressing { background: #f0f9eb; color: #67C23A; }
.tag-resource { background: #fdf6ec; color: #E6A23C; }
.tag-milestone { background: #ecf5ff; color: #409EFF; }

.feedback-content {
  color: #606266;
  line-height: 1.6;
}

.feedback-content :deep(.mention) {
  color: #409EFF;
  font-weight: 500;
}
</style>
```

- [ ] **Step 2: 提交代码**

```bash
git add frontend/src/components/FeedbackList.vue
git commit -m "feat: add FeedbackList component with tag display"
```

---

### Task 9: 集成反馈组件到任务详情页

**Files:**
- Modify: `frontend/src/views/TaskDetail.vue`

- [ ] **Step 1: 在 TaskDetail.vue 中集成反馈区域**

在模板的合适位置添加反馈区域（约在任务详情内容和评论区之间）：

```vue
<!-- 反馈区域 -->
<div class="feedback-section">
  <h3>任务反馈</h3>
  <FeedbackInput :taskId="taskId" @refresh="loadFeedbacks" />
  <FeedbackList 
    :feedbacks="feedbacks" 
    :currentUserId="currentUserId" 
    @refresh="loadFeedbacks" 
  />
</div>
```

在 script 部分添加：

```javascript
import { getFeedbacks } from '../api/feedback'

const feedbacks = ref([])
const currentUserId = ref(parseInt(localStorage.getItem('userId')))

const loadFeedbacks = async () => {
  const res = await getFeedbacks(taskId)
  feedbacks.value = res.data
}

onMounted(() => {
  loadFeedbacks()
})
```

添加样式：

```css
.feedback-section {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;
}

.feedback-section h3 {
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 500;
}
```

- [ ] **Step 2: 提交代码**

```bash
git add frontend/src/views/TaskDetail.vue
git commit -m "feat: integrate feedback components into TaskDetail page"
```

---

### Task 10: 功能测试验证

**Files:**
- None (测试验证)

- [ ] **Step 1: 验证反馈提交功能**

测试场景：
1. 登录下属账号
2. 进入任务详情页
3. 选择反馈标签
4. 输入反馈内容（测试@提及）
5. 点击提交
6. 验证反馈显示在列表中

预期结果：反馈成功提交，标签正确显示，@提及高亮

- [ ] **Step 2: 验证@提及功能**

测试场景：
1. 在反馈输入框输入@
2. 验证下拉用户列表显示
3. 选择用户完成@提及
4. 提交反馈

预期结果：@后用户高亮显示

- [ ] **Step 3: 验证删除功能**

测试场景：
1. 点击自己提交的反馈的删除按钮
2. 确认删除

预期结果：反馈从列表中移除

- [ ] **Step 4: 提交测试结果**

```bash
git log --oneline -10
```

---

## 自我检查

**1. 规范覆盖检查：**
- [x] 反馈类型标签（4种）
- [x] @提及功能
- [x] 独立反馈列表展示
- [x] 反馈提交/删除API
- [x] 标签颜色规范

**2. 占位符检查：**
- 无 TBD/TODO
- 无"类似 Task N"的引用
- 所有代码块完整

**3. 类型一致性检查：**
- Feedback 实体字段与 API 定义一致
- tag 使用 ENUM 类型，与前端标签映射一致
- mentionedUsers 使用逗号分隔字符串

**4. 完整性检查：**
- 后端：entity → mapper → service → controller 完整链路
- 前端：API → FeedbackInput → FeedbackList → TaskDetail 集成
- 数据库：迁移脚本包含表结构和索引
