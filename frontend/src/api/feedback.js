import request from './request'

export const feedbackApi = {
  getFeedbacks(taskId) {
    return request.get(`/tasks/${taskId}/feedbacks`)
  },
  createFeedback(taskId, data) {
    return request.post(`/tasks/${taskId}/feedbacks`, data, {
      headers: { 'X-User-Id': data.userId }
    })
  },
  deleteFeedback(taskId, feedbackId) {
    return request.delete(`/tasks/${taskId}/feedbacks/${feedbackId}`)
  }
}
