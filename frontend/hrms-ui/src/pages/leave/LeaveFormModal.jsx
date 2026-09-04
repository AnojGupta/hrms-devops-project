import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import { getEmployees } from '../../api/employeeApi'
import { applyLeave } from '../../api/leaveApi'

export default function LeaveFormModal({ onClose, onCreated }) {
  const [employees, setEmployees] = useState([])
  const [form, setForm] = useState({ employeeId: '', leaveType: 'CASUAL_LEAVE', startDate: '', endDate: '', reason: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})
  }, [])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await applyLeave({ ...form, employeeId: Number(form.employeeId) })
      onCreated()
    } catch (err) {
      const details = err.response?.data?.details
      setError(details ? details.join(', ') : err.response?.data?.message || 'Failed to submit leave request')
      setSubmitting(false)
    }
  }

  return (
    <Modal title="Apply for Leave" onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <FormSelect label="Employee" name="employeeId" value={form.employeeId} onChange={handleChange} required>
          <option value="">-- Select employee --</option>
          {employees.map((e) => (
            <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
          ))}
        </FormSelect>
        <FormSelect label="Leave Type" name="leaveType" value={form.leaveType} onChange={handleChange}>
          <option value="SICK_LEAVE">SICK LEAVE</option>
          <option value="CASUAL_LEAVE">CASUAL LEAVE</option>
          <option value="PAID_LEAVE">PAID LEAVE</option>
          <option value="UNPAID_LEAVE">UNPAID LEAVE</option>
        </FormSelect>
        <div className="form-row">
          <FormInput label="Start Date" type="date" name="startDate" value={form.startDate} onChange={handleChange} required />
          <FormInput label="End Date" type="date" name="endDate" value={form.endDate} onChange={handleChange} required />
        </div>
        <FormInput label="Reason" name="reason" value={form.reason} onChange={handleChange} />
        <div className="modal-actions">
          <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Submitting...' : 'Submit'}
          </button>
        </div>
      </form>
    </Modal>
  )
}
