import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext.jsx'
import FormInput from '../../components/FormInput.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await login(form.username, form.password)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="card auth-card">
        <h1>Welcome back</h1>
        <p className="subtitle">Sign in to the HR Management System</p>
        <ErrorMessage message={error} />
        <form onSubmit={handleSubmit}>
          <FormInput
            label="Username"
            name="username"
            value={form.username}
            onChange={handleChange}
            required
          />
          <FormInput
            label="Password"
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
          />
          <button className="btn btn-primary" style={{ width: '100%', marginTop: 8 }} disabled={submitting}>
            {submitting ? 'Signing in...' : 'Sign in'}
          </button>
        </form>
        <p style={{ marginTop: 16, fontSize: 14 }}>
          Don't have an account? <Link to="/register">Register</Link>
        </p>
        <p style={{ marginTop: 8, fontSize: 12, color: '#94a3b8' }}>
          Default admin: <strong>admin</strong> / <strong>Admin@123</strong>
        </p>
      </div>
    </div>
  )
}
