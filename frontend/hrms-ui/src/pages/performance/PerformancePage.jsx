import React, { useEffect, useState } from 'react'
import { getAllReviews } from '../../api/performanceApi'
import { useAuth } from '../../context/AuthContext.jsx'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import PerformanceFormModal from './PerformanceFormModal.jsx'

export default function PerformancePage() {
  const { hasRole } = useAuth()
  const canCreate = hasRole('ADMIN', 'HR', 'MANAGER')

  const [reviews, setReviews] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = () => {
    setLoading(true)
    getAllReviews()
      .then((res) => setReviews(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load performance reviews'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  return (
    <div>
      <div className="page-header">
        <h1>Performance Reviews</h1>
        {canCreate && (
          <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Add Review</button>
        )}
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : reviews.length === 0 ? (
        <EmptyState message="No performance reviews found." />
      ) : (
        <table>
          <thead>
            <tr><th>Employee</th><th>Reviewer</th><th>Rating</th><th>Feedback</th><th>Date</th></tr>
          </thead>
          <tbody>
            {reviews.map((r) => (
              <tr key={r.id}>
                <td>{r.employee ? `${r.employee.firstName} ${r.employee.lastName}` : '-'}</td>
                <td>{r.reviewer ? `${r.reviewer.firstName} ${r.reviewer.lastName}` : '-'}</td>
                <td>{r.rating} / 5</td>
                <td>{r.feedback || '-'}</td>
                <td>{r.reviewDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <PerformanceFormModal onClose={() => setShowForm(false)} onCreated={() => { setShowForm(false); load(); }} />
      )}
    </div>
  )
}
