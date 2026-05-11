import { defineStore } from 'pinia'
import { authApi } from '../api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  getters: {
    isLoggedIn: state => !!state.token
  },
  actions: {
    async login(username, password) {
      const res = await authApi.login({ username, password })
      if (res.code === 200) {
        this.token = res.data.token
        this.user = res.data
        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))
      }
      return res
    },
    async register(data) {
      const res = await authApi.register(data)
      if (res.code === 200) {
        this.token = res.data.token
        this.user = res.data
        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))
      }
      return res
    },
    async fetchCurrentUser() {
      const res = await authApi.getCurrentUser()
      if (res.code === 200) {
        this.user = res.data
        localStorage.setItem('user', JSON.stringify(this.user))
      }
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
