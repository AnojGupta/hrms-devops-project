import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import Layout from '../components/Layout.jsx'

export default function ProtectedRoute({ children, roles }) {
  const { user, loading, hasRole } = useAuth()

  if (loading) return null
  if (!user) return <Navigate to="/login" replace />
  if (roles && !hasRole(...roles)) return <Navigate to="/" replace />

  return <Layout>{children}</Layout>
}
