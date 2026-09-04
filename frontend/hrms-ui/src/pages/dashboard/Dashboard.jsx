import React, { useEffect, useState } from 'react'
import { getDashboardSummary } from '../../api/dashboardApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'

export default function Dashboard() {
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    getDashboardSummary()
      .then((res) => setSummary(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load dashboard summary'))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div>
      <div className="page-header">
        <h1>Dashboard</h1>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : (
        summary && (
          <div className="stat-grid">
            <StatCard label="Total Employees" value={summary.totalEmployees} />
            <StatCard label="Total Departments" value={summary.totalDepartments} />
            <StatCard label="Total Projects" value={summary.totalProjects} />
            <StatCard label="On Leave Today" value={summary.employeesOnLeaveToday} />
            <StatCard label="Present Today" value={summary.presentToday} />
          </div>
        )
      )}
    </div>
  )
}

function StatCard({ label, value }) {
  return (
    <div className="stat-card">
      <div className="label">{label}</div>
      <div className="value">{value}</div>
    </div>
  )
}
