import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import { getEmployees } from '../../api/employeeApi'
import { createPayroll } from '../../api/payrollApi'

export default function PayrollFormModal({ onClose, onCreated }) {
  const [employees, setEmployees] = useState([])
  const [form, setForm] = useState({ employeeId: '', basicSalary: '', bonus: '', deductions: '', paymentDate: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})
  }, [])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const netPreview = () => {
    const basic = Number(form.basicSalary) || 0
    const bonus = Number(form.bonus) || 0
    const deductions = Number(form.deductions) || 0
    return (basic + bonus - deductions).toFixed(2)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await createPayroll({
        employeeId: Number(form.employeeId),
        basicSalary: Number(form.basicSalary),
        bonus: form.bonus === '' ? 0 : Number(form.bonus),
        deductions: form.deductions === '' ? 0 : Number(form.deductions),
        paymentDate: form.paymentDate || null,
      })
      onCreated()
    } catch (err) {
      const details = err.response?.data?.details
      setError(details ? details.join(', ') : err.response?.data?.message || 'Failed to create payroll record')
      setSubmitting(false)
    }
  }

  return (
    <Modal title="Add Payroll Record" onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <FormSelect label="Employee" name="employeeId" value={form.employeeId} onChange={handleChange} required>
          <option value="">-- Select employee --</option>
          {employees.map((e) => (
            <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
          ))}
        </FormSelect>
        <div className="form-row">
          <FormInput label="Basic Salary" type="number" step="0.01" name="basicSalary" value={form.basicSalary} onChange={handleChange} required />
          <FormInput label="Bonus" type="number" step="0.01" name="bonus" value={form.bonus} onChange={handleChange} />
        </div>
        <div className="form-row">
          <FormInput label="Deductions" type="number" step="0.01" name="deductions" value={form.deductions} onChange={handleChange} />
          <FormInput label="Payment Date" type="date" name="paymentDate" value={form.paymentDate} onChange={handleChange} />
        </div>
        <p style={{ fontSize: 13, color: '#64748b' }}>Net salary preview: <strong>{netPreview()}</strong></p>
        <div className="modal-actions">
          <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Saving...' : 'Save'}
          </button>
        </div>
      </form>
    </Modal>
  )
}
