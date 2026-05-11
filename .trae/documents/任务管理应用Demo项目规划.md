# 任务管理应用 Demo 项目规划

## 📋 项目概述

### 项目名称
**TaskSync** - 领导与下级任务协同管理系统

### 核心价值
实现领导与下级之间的任务下发、执行、反馈、验收的完整生命周期管理，通过现代化的界面设计展示企业级任务协作流程。

### 目标用户
- **领导角色**：创建并分配任务、查看任务进度、验收任务成果
- **下属角色**：接收任务、执行任务、提交进度反馈

### 成功标准
1. ✅ 完整的任务生命周期闭环（创建→分配→执行→反馈→验收）
2. ✅ 领导和下属双角色功能完整
3. ✅ 简洁商务风格的现代化UI设计
4. ✅ Vue.js前端 + Spring Boot后端完整可运行
5. ✅ MySQL数据库数据持久化
6. ✅ 用户登录认证功能

---

## 🏗️ 技术架构

### 整体架构
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Vue.js 3.0    │────▶│  Spring Boot    │────▶│     MySQL       │
│   (前端界面)      │◀────│   REST API      │◀────│   (数据存储)      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
       │                        │
       │                        │
   简洁商务风              JWT认证 + 业务逻辑
   响应式设计              分层架构
```

### 技术栈详情

#### 前端技术栈
- **框架**：Vue.js 3.0 (Composition API)
- **UI库**：Element Plus (简洁商务风格)
- **状态管理**：Pinia
- **路由**：Vue Router 4
- **HTTP客户端**：Axios
- **构建工具**：Vite

#### 后端技术栈
- **框架**：Spring Boot 3.0
- **认证**：JWT (JSON Web Token)
- **ORM**：MyBatis-Plus
- **数据库**：MySQL 8.0
- **API风格**：RESTful API

---

## 🎨 前端设计

### 页面结构

```
/login                 - 登录页面
/dashboard             - 仪表盘（首页）
/tasks                 - 任务列表
/tasks/create          - 创建任务（领导）
/tasks/:id             - 任务详情
/my-tasks              - 我的任务（下属视角）
```

### 页面功能

#### 1. 登录页面 (/login)
- 用户名 + 密码输入
- 角色选择（领导/下属）
- 登录按钮
- **设计风格**：简洁商务风，居中卡片式布局

#### 2. 仪表盘 (/dashboard)
**领导视角**：
- 今日任务统计卡片（总数、待处理、进行中、已完成）
- 任务分布饼图
- 最近任务列表
- 快速创建任务按钮

**下属视角**：
- 待办任务统计
- 已完成任务统计
- 任务进度时间线
- 最新任务通知

#### 3. 任务列表 (/tasks)
- 筛选栏：状态筛选、日期范围、关键词搜索
- 任务卡片列表：
  - 任务标题
  - 负责人头像 + 姓名
  - 截止日期
  - 状态标签（待接收/进行中/已完成）
  - 优先级标识
- 分页组件
- **新建任务按钮**（领导专属）

#### 4. 创建任务 (/tasks/create)
- 任务标题（输入框）
- 任务描述（富文本编辑器）
- 执行人选择（下属下拉列表）
- 截止日期（日期选择器）
- 优先级（高/中/低单选）
- 附件上传区域（可选）
- 提交按钮

#### 5. 任务详情 (/tasks/:id)
- 任务基本信息卡片
- 执行进度条
- 任务时间线（创建→分配→执行→反馈→验收）
- 评论区（上下级互动）
- 状态操作按钮（领取/开始/完成/验收）

### UI设计规范

#### 颜色系统
```
主色调：#409EFF (商务蓝)
辅助色：#67C23A (成功绿)
警告色：#E6A23C (警告橙)
错误色：#F56C6C (错误红)
背景色：#F5F7FA (浅灰背景)
文字色：#303133 (主文字)
次要文字：#909399 (次要文字)
边框色：#DCDFE6 (边框)
```

#### 字体规范
- 主字体：'PingFang SC', 'Microsoft YaHei', sans-serif
- 标题字重：500
- 正文字重：400
- 标题大小：h1=24px, h2=20px, h3=16px
- 正文字号：14px

#### 布局规范
- 页面最大宽度：1200px
- 卡片间距：20px
- 内边距：20px
- 圆角：4px
- 阴影：0 2px 12px rgba(0, 0, 0, 0.1)

#### 动效规范
- 页面切换：fade 0.3s
- 卡片悬停：transform scale(1.02) 0.2s
- 按钮点击：opacity 0.8 0.1s
- 列表加载：stagger 50ms

---

## ⚙️ 后端设计

### 项目结构
```
backend/
├── src/main/java/com/tasksync/
│   ├── TaskSyncApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── CorsConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── TaskController.java
│   │   └── UserController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── TaskService.java
│   │   └── UserService.java
│   ├── mapper/
│   │   ├── TaskMapper.java
│   │   └── UserMapper.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Task.java
│   │   └── Comment.java
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── TaskCreateRequest.java
│   │   └── TaskUpdateRequest.java
│   ├── security/
│   │   ├── JwtTokenUtil.java
│   │   └── JwtAuthFilter.java
│   └── common/
│       ├── Result.java
│       └── ResultCode.java
└── src/main/resources/
    ├── application.yml
    └── schema.sql
```

### API接口设计

#### 认证接口
```
POST /api/auth/login
  Request: { username, password }
  Response: { token, userInfo }

POST /api/auth/logout
  Response: { success }

GET /api/auth/current-user
  Headers: Authorization: Bearer <token>
  Response: { userInfo }
```

#### 任务接口
```
GET    /api/tasks                 # 获取任务列表
POST   /api/tasks                 # 创建任务（领导）
GET    /api/tasks/:id             # 获取任务详情
PUT    /api/tasks/:id             # 更新任务
DELETE /api/tasks/:id             # 删除任务
PUT    /api/tasks/:id/status      # 更新任务状态
GET    /api/tasks/my              # 获取我的任务（下属）
GET    /api/tasks/assigned        # 获取我分配的任务（领导）
POST   /api/tasks/:id/comments    # 添加评论
GET    /api/tasks/:id/comments    # 获取评论列表
```

#### 用户接口
```
GET /api/users                   # 获取用户列表（领导）
GET /api/users/subordinates       # 获取下属列表
```

### API响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 错误码规范
```
200: 成功
400: 参数错误
401: 未认证
403: 无权限
404: 资源不存在
500: 服务器错误
```

---

## 🗄️ 数据库设计

### 用户表 (users)
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  real_name VARCHAR(50),
  role ENUM('LEADER', 'MEMBER') NOT NULL,
  leader_id BIGINT,
  avatar VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (leader_id) REFERENCES users(id)
);
```

### 任务表 (tasks)
```sql
CREATE TABLE tasks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  leader_id BIGINT NOT NULL,
  assignee_id BIGINT NOT NULL,
  status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'ACCEPTED') DEFAULT 'PENDING',
  priority ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM',
  deadline DATETIME,
  progress INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  accepted_at TIMESTAMP,
  FOREIGN KEY (leader_id) REFERENCES users(id),
  FOREIGN KEY (assignee_id) REFERENCES users(id)
);
```

### 评论表 (comments)
```sql
CREATE TABLE comments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  task_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (task_id) REFERENCES tasks(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### 任务状态历史表 (task_status_history)
```sql
CREATE TABLE task_status_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  task_id BIGINT NOT NULL,
  from_status VARCHAR(50),
  to_status VARCHAR(50) NOT NULL,
  operator_id BIGINT NOT NULL,
  remark VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (task_id) REFERENCES tasks(id),
  FOREIGN KEY (operator_id) REFERENCES users(id)
);
```

---

## 🔐 认证与授权

### JWT认证流程
1. 用户登录 → 验证用户名密码 → 生成JWT Token
2. 前端存储Token（localStorage）
3. 请求携带Token（Authorization: Bearer <token>）
4. 后端验证Token → 解析用户信息 → 判断权限

### 角色权限
```
领导 (LEADER):
  ✓ 创建任务
  ✓ 分配任务给下属
  ✓ 查看所有任务
  ✓ 查看任务进度
  ✓ 验收任务
  ✗ 无法执行任务

下属 (MEMBER):
  ✓ 接收任务
  ✓ 执行任务（更新进度）
  ✓ 提交任务完成
  ✗ 无法创建任务
  ✗ 无法分配任务
```

---

## 📝 核心功能模块

### 1. 任务创建（领导）
- 填写任务基本信息
- 选择执行人（下属列表）
- 设置截止日期和优先级
- 提交后任务状态为 PENDING

### 2. 任务领取（下属）
- 下属查看待接收任务
- 点击"领取任务"按钮
- 任务状态变为 IN_PROGRESS
- 记录领取时间

### 3. 任务执行（下属）
- 更新任务进度（0-100%）
- 提交进度反馈
- 完成后点击"申请验收"
- 任务状态变为 COMPLETED

### 4. 任务验收（领导）
- 查看任务完成情况
- 审核任务成果
- 通过则"验收通过"（ACCEPTED）
- 不通过则"驳回"（回到IN_PROGRESS）

### 5. 任务评论
- 领导可以添加评论
- 下属可以回复
- 评论显示在任务详情页
- 按时间倒序排列

---

## 🧪 测试数据

### 初始用户
```
领导：
  - 用户名: leader
  - 密码: leader123
  - 姓名: 张经理

下属：
  - 用户名: member1
  - 密码: member123
  - 姓名: 李小明
  - 上级: 张经理

  - 用户名: member2
  - 密码: member123
  - 姓名: 王小红
  - 上级: 张经理
```

### 初始任务
```
1. "完成季度报告" - 李小明 - 已完成（ACCEPTED）
2. "市场调研" - 王小红 - 进行中（IN_PROGRESS）
3. "产品测试" - 李小明 - 待接收（PENDING）
```

---

## 📦 项目文件结构

### 前端结构 (frontend/)
```
frontend/
├── package.json
├── vite.config.js
├── index.html
├── src/
│   ├── main.js
│   ├── App.vue
│   ├── router/
│   │   └── index.js
│   ├── stores/
│   │   ├── auth.js
│   │   └── task.js
│   ├── views/
│   │   ├── Login.vue
│   │   ├── Dashboard.vue
│   │   ├── TaskList.vue
│   │   ├── TaskCreate.vue
│   │   └── TaskDetail.vue
│   ├── components/
│   │   ├── TaskCard.vue
│   │   ├── StatusTag.vue
│   │   └── Header.vue
│   ├── api/
│   │   ├── auth.js
│   │   └── task.js
│   ├── utils/
│   │   └── request.js
│   └── styles/
│       └── common.css
```

### 后端结构 (backend/)
```
backend/
├── pom.xml
├── src/main/java/com/tasksync/
│   ├── TaskSyncApplication.java
│   ├── config/
│   ├── controller/
│   ├── service/
│   ├── mapper/
│   ├── entity/
│   ├── dto/
│   ├── security/
│   └── common/
└── src/main/resources/
    ├── application.yml
    └── schema.sql
```

---

## 🚀 开发计划

### 阶段1：后端基础架构（1-2天）
- [ ] 创建Spring Boot项目
- [ ] 配置MySQL数据库连接
- [ ] 实现用户实体和Mapper
- [ ] 实现JWT认证
- [ ] 实现用户登录注册接口

### 阶段2：任务管理核心（1-2天）
- [ ] 实现任务实体和Mapper
- [ ] 实现任务CRUD接口
- [ ] 实现任务状态流转
- [ ] 实现评论功能

### 阶段3：前端界面开发（2-3天）
- [ ] 创建Vue 3项目
- [ ] 配置路由和状态管理
- [ ] 实现登录页面
- [ ] 实现仪表盘
- [ ] 实现任务列表
- [ ] 实现任务创建
- [ ] 实现任务详情

### 阶段4：功能联调与优化（1-2天）
- [ ] 前后端接口对接
- [ ] UI细节优化
- [ ] 测试完整流程
- [ ] 添加动画效果

### 总工期：5-9个工作日

---

## ✅ 验收标准

### 功能验收
- [ ] 领导可以创建任务
- [ ] 领导可以分配任务给下属
- [ ] 下属可以领取任务
- [ ] 下属可以更新任务进度
- [ ] 下属可以申请完成
- [ ] 领导可以验收任务
- [ ] 领导和下属可以评论互动
- [ ] 任务状态正确流转

### 技术验收
- [ ] 前后端分离架构
- [ ] RESTful API规范
- [ ] JWT认证安全
- [ ] MySQL数据持久化
- [ ] 代码结构清晰
- [ ] 注释完整

### UI验收
- [ ] 简洁商务风格统一
- [ ] 响应式布局
- [ ] 流畅动画效果
- [ ] 友好的用户体验
- [ ] 清晰的视觉层级

### 测试验收
- [ ] 登录功能正常
- [ ] 角色权限正确
- [ ] 任务流程闭环
- [ ] 数据正确保存
- [ ] 无明显Bug

---

## 🎯 项目亮点

1. **完整的任务生命周期**：从创建到验收的闭环流程
2. **角色权限分明**：领导与下属功能隔离
3. **现代化UI设计**：简洁商务风格，符合企业审美
4. **实时状态更新**：任务状态和时间线可视化
5. **评论互动系统**：上下级可以在任务中沟通
6. **JWT安全认证**：生产级别的认证机制

---

## ⚠️ 假设与约束

1. **Demo级别**：不做微服务、缓存、消息队列等企业级特性
2. **单leader多member**：简化组织架构，支持多个下属
3. **数据演示**：使用预设测试数据，无需复杂的用户注册
4. **简化权限**：不实现细粒度的权限控制
5. **单数据库**：不使用分库分表等大数据量方案
