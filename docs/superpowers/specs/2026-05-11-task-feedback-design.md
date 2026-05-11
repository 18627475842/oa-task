# 任务反馈功能设计

## 概述

在任务协同管理系统中新增独立的**即时反馈模块**，支持下属快速提交任务状态反馈，领导可实时了解任务进展。反馈功能相比现有评论增加了标签分类、@提及等能力，提升沟通效率。

## 功能设计

### 反馈类型标签

| 标签 | 说明 | 颜色 |
|------|------|------|
| 遇到问题 | 需要领导协助解决 | #F56C6C |
| 进展顺利 | 正常推进 | #67C23A |
| 需要资源 | 需要额外支持 | #E6A23C |
| 已完成阶段 | 达成里程碑节点 | #409EFF |

### @提及功能

- 支持在反馈内容中@对方（如 @张经理）
- 输入@触发下拉用户列表
- 被@者收到通知提醒

### 反馈展示

- 独立的反馈历史展示区域
- 按时间倒序排列
- 区分"我的反馈"和"对方反馈"

## 数据结构

### 数据库表

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
```

### 字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| task_id | BIGINT | 关联任务ID |
| user_id | BIGINT | 反馈提交人ID |
| content | TEXT | 反馈内容（支持@格式） |
| tag | ENUM | 反馈类型标签 |
| mentioned_users | VARCHAR(500) | 被@的用户ID列表，逗号分隔 |
| created_at | TIMESTAMP | 创建时间 |

## API接口

### 获取反馈列表

```
GET /api/tasks/:id/feedbacks
Response: {
  code: 200,
  data: [
    {
      id, taskId, userId, userName, userAvatar,
      content, tag, mentionedUsers, createdAt
    }
  ]
}
```

### 提交反馈

```
POST /api/tasks/:id/feedbacks
Request: { content, tag, mentionedUsers }
Response: { code: 200, data: { feedback } }
```

### 删除反馈

```
DELETE /api/tasks/:id/feedbacks/:fid
Response: { code: 200, message: "删除成功" }
```

## 前端设计

### 任务详情页 - 反馈区域

#### 反馈输入区

- 标签选择器：4种类型快速切换，默认"进展顺利"
- 富文本输入框：支持文字输入和@提及
- @提及交互：输入@触发下拉用户列表
- 提交按钮

#### 反馈列表区

卡片式展示每条反馈：

- 头像 + 姓名 + 时间
- 标签颜色标识
- 反馈内容（含@高亮显示）
- 删除按钮（仅本人可见）

### 标签颜色规范

```css
.feedback-tag-blocking { color: #F56C6C; }
.feedback-tag-progressing { color: #67C23A; }
.feedback-tag-need-resource { color: #E6A23C; }
.feedback-tag-milestone { color: #409EFF; }
```

## 实现计划

### 后端实现

1. 新增 `Feedback` 实体类
2. 新增 `FeedbackMapper` 接口
3. 新增 `FeedbackService` 服务类
4. 新增 `FeedbackController` 控制器
5. 实现@提及解析和通知发送逻辑

### 前端实现

1. 新增 `FeedbackList` 组件（反馈列表）
2. 新增 `FeedbackInput` 组件（反馈输入）
3. 在 `TaskDetail.vue` 中集成反馈区域
4. 实现@提及下拉交互
5. 样式适配和动效添加

## 影响范围

- **新增文件**：后端 entity/Feedback.java, mapper/FeedbackMapper.java, service/FeedbackService.java, controller/FeedbackController.java；前端 components/FeedbackList.vue, components/FeedbackInput.vue
- **修改文件**：TaskDetail.vue（添加反馈区域）、TaskService.java（添加反馈关联查询）
