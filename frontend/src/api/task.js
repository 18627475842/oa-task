import request from './request'

export const taskApi = {
  getTasks() {
    return request.get('/tasks')
  },
  getCreatedTasks() {
    return request.get('/tasks/created')
  },
  getAssignedTasks() {
    return request.get('/tasks/assigned')
  },
  getTaskById(id) {
    return request.get(`/tasks/${id}`)
  },
  createTask(data) {
    return request.post('/tasks', data)
  },
  updateTask(id, data) {
    return request.put(`/tasks/${id}`, data)
  },
  deleteTask(id) {
    return request.delete(`/tasks/${id}`)
  },
  getComments(taskId) {
    return request.get(`/tasks/${taskId}/comments`)
  },
  createComment(taskId, content) {
    return request.post(`/tasks/${taskId}/comments`, { content })
  },
  deleteComment(taskId, commentId) {
    return request.delete(`/tasks/${taskId}/comments/${commentId}`)
  }
}
