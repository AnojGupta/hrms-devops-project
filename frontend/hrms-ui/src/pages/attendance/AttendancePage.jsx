import React, { useEffect, useState } from 'react'
import { getAllAttendance, checkIn, checkOut } from '../../api/attendanceApi'
import { getEmployees } from '../../api/employeeApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import StatusBadge from '../../components/StatusBadge.jsx'
import FormSelect from '../../components/FormSelect.jsx'

export default function AttendancePage() {
  const [records, setRecords] = useState([])
  const [employees, setEmployees] = useState([])
  const [employeeId, setEmployeeId] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [actionLoading, setActionLoading] = useState(false)

  const load = () => {
    setLoading(true)
    getAllAttendance()
      .then((res) => setRecords(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load attendance'))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    load()
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})
  }, [])

  const handleCheckIn = async () => {
    if (!employeeId) { setError('Select an employee first'); return }
    setError('')
    setActionLoading(true)
    try {
      await checkIn(Number(employeeId))
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Check-in failed')
    } finally {
      setActionLoading(false)
    }
  }

  const handleCheckOut = async () => {
    if (!employeeId) { setError('Select an employee first'); return }
    setError('')
    setActionLoading(true)
    try {
      await checkOut(Number(employeeId))
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Check-out failed')
    } finally {
      setActionLoading(false)
    }
  }

  return (
    <div>
      <div className="page-header">
        <h1>Attendance</h1>
      </div>

      <div className="card" style={{ marginBottom: 20 }}>
        <div className="toolbar" style={{ alignItems: 'flex-end' }}>
          <div style={{ minWidth: 260 }}>
            <FormSelect label="Employee" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)}>
              <option value="">-- Select employee --</option>
              {employees.map((e) => (
                <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
              ))}
            </FormSelect>
          </div>
          <button className="btn btn-primary" onClick={handleCheckIn} disabled={actionLoading}>Check In</button>
          <button className="btn btn-secondary" onClick={handleCheckOut} disabled={actionLoading}>Check Out</button>
        </div>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : records.length === 0 ? (
        <EmptyState message="No attendance records yet." />
      ) : (
        <table>
          <thead>
            <tr><th>Employee</th><th>Date</th><th>Check In</th><th>Check Out</th><th>Status</th></tr>
          </thead>
          <tbody>
            {records.map((r) => (
              <tr key={r.id}>
                <td>{r.employee ? `${r.employee.firstName} ${r.employee.lastName}` : '-'}</td>
                <td>{r.date}</td>
                <td>{r.checkInTime || '-'}</td>
                <td>{r.checkOutTime || '-'}</td>
                <td><StatusBadge status={r.status} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
