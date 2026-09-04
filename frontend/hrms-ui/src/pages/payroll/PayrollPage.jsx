import React, { useEffect, useState } from 'react'
import { getAllPayroll } from '../../api/payrollApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import StatusBadge from '../../components/StatusBadge.jsx'
import PayrollFormModal from './PayrollFormModal.jsx'

export default function PayrollPage() {
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = () => {
    setLoading(true)
    getAllPayroll()
      .then((res) => setRecords(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load payroll records'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  return (
    <div>
      <div className="page-header">
        <h1>Payroll</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Add Payroll Record</button>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : records.length === 0 ? (
        <EmptyState message="No payroll records found." />
      ) : (
        <table>
          <thead>
            <tr><th>Employee</th><th>Basic</th><th>Bonus</th><th>Deductions</th><th>Net Salary</th><th>Payment Date</th><th>Status</th></tr>
          </thead>
          <tbody>
            {records.map((p) => (
              <tr key={p.id}>
                <td>{p.employee ? `${p.employee.firstName} ${p.employee.lastName}` : '-'}</td>
                <td>{p.basicSalary}</td>
                <td>{p.bonus}</td>
                <td>{p.deductions}</td>
                <td><strong>{p.netSalary}</strong></td>
                <td>{p.paymentDate || '-'}</td>
                <td><StatusBadge status={p.paymentStatus} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <PayrollFormModal onClose={() => setShowForm(false)} onCreated={() => { setShowForm(false); load(); }} />
      )}
    </div>
  )
}
