import React, { createContext, useContext, useEffect, useState } from 'react'
import * as authApi from '../api/authApi'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const storedUser = localStorage.getItem('hrms_user')
    const token = localStorage.getItem('hrms_token')
    if (storedUser && token) {
      setUser(JSON.parse(storedUser))
    }
    setLoading(false)
  }, [])

  const login = async (username, password) => {
    const { data } = await authApi.login({ username, password })
    persistSession(data)
    return data
  }

  const register = async (payload) => {
    const { data } = await authApi.register(payload)
    persistSession(data)
    return data
  }

  const persistSession = (data) => {
    localStorage.setItem('hrms_token', data.token)
    const sessionUser = { username: data.username, email: data.email, roles: data.roles }
    localStorage.setItem('hrms_user', JSON.stringify(sessionUser))
    setUser(sessionUser)
  }

  const logout = () => {
    localStorage.removeItem('hrms_token')
    localStorage.removeItem('hrms_user')
    setUser(null)
  }

  const hasRole = (...roles) => {
    if (!user || !user.roles) return false
    return roles.some((r) => user.roles.includes(r))
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, hasRole }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
