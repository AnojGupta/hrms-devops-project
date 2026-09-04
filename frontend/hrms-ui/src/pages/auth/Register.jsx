import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext.jsx'
import FormInput from '../../components/FormInput.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'

export default function Register() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', email: '', password: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await register(form)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="card auth-card">
        <h1>Create an account</h1>
        <p className="subtitle">Register as a new employee-level user</p>
        <ErrorMessage message={error} />
        <form onSubmit={handleSubmit}>
          <FormInput label="Username" name="username" value={form.username} onChange={handleChange} required />
          <FormInput label="Email" type="email" name="email" value={form.email} onChange={handleChange} required />
          <FormInput
            label="Password"
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
            minLength={6}
          />
          <button className="btn btn-primary" style={{ width: '100%', marginTop: 8 }} disabled={submitting}>
            {submitting ? 'Creating account...' : 'Register'}
          </button>
        </form>
        <p style={{ marginTop: 16, fontSize: 14 }}>
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  )
}
