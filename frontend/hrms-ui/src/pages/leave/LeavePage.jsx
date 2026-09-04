import React, { useEffect, useState } from 'react'
import { getLeaves, approveLeave, rejectLeave } from '../../api/leaveApi'
import { getEmployees } from '../../api/employeeApi'
import { useAuth } from '../../context/AuthContext.jsx'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import StatusBadge from '../../components/StatusBadge.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import LeaveFormModal from './LeaveFormModal.jsx'

export default function LeavePage() {
  const { hasRole } = useAuth()
  const canDecide = hasRole('ADMIN', 'HR', 'MANAGER')

  const [leaves, setLeaves] = useState([])
  const [employees, setEmployees] = useState([])
  const [approverId, setApproverId] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = () => {
    setLoading(true)
    getLeaves()
      .then((res) => setLeaves(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load leave requests'))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    load()
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})
  }, [])

  const handleDecision = async (id, action) => {
    if (!approverId) { setError('Select an approver first'); return }
    setError('')
    try {
      if (action === 'approve') await approveLeave(id, Number(approverId))
      else await rejectLeave(id, Number(approverId))
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update leave request')
    }
  }

  return (
    <div>
      <div className="page-header">
        <h1>Leave Requests</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Apply for Leave</button>
      </div>

      {canDecide && (
        <div className="card" style={{ marginBottom: 20, maxWidth: 320 }}>
          <FormSelect label="Approve/Reject as" value={approverId} onChange={(e) => setApproverId(e.target.value)}>
            <option value="">-- Select employee --</option>
            {employees.map((e) => (
              <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
            ))}
          </FormSelect>
        </div>
      )}

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : leaves.length === 0 ? (
        <EmptyState message="No leave requests found." />
      ) : (
        <table>
          <thead>
            <tr>
              <th>Employee</th><th>Type</th><th>From</th><th>To</th><th>Reason</th><th>Status</th>
              {canDecide && <th></th>}
            </tr>
          </thead>
          <tbody>
            {leaves.map((l) => (
              <tr key={l.id}>
                <td>{l.employee ? `${l.employee.firstName} ${l.employee.lastName}` : '-'}</td>
                <td>{l.leaveType?.replace(/_/g, ' ')}</td>
                <td>{l.startDate}</td>
                <td>{l.endDate}</td>
                <td>{l.reason || '-'}</td>
                <td><StatusBadge status={l.status} /></td>
                {canDecide && (
                  <td>
                    {l.status === 'PENDING' ? (
                      <>
                        <button className="btn btn-primary btn-sm" onClick={() => handleDecision(l.id, 'approve')}>Approve</button>{' '}
                        <button className="btn btn-danger btn-sm" onClick={() => handleDecision(l.id, 'reject')}>Reject</button>
                      </>
                    ) : '-'}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <LeaveFormModal onClose={() => setShowForm(false)} onCreated={() => { setShowForm(false); load(); }} />
      )}
    </div>
  )
}
