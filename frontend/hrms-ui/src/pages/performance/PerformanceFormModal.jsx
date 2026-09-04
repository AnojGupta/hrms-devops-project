import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import { getEmployees } from '../../api/employeeApi'
import { createReview } from '../../api/performanceApi'

export default function PerformanceFormModal({ onClose, onCreated }) {
  const [employees, setEmployees] = useState([])
  const [form, setForm] = useState({ employeeId: '', reviewerId: '', rating: '3', feedback: '', reviewDate: '' })
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
      await createReview({
        employeeId: Number(form.employeeId),
        reviewerId: Number(form.reviewerId),
        rating: Number(form.rating),
        feedback: form.feedback,
        reviewDate: form.reviewDate || null,
      })
      onCreated()
    } catch (err) {
      const details = err.response?.data?.details
      setError(details ? details.join(', ') : err.response?.data?.message || 'Failed to submit review')
      setSubmitting(false)
    }
  }

  return (
    <Modal title="Add Performance Review" onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <FormSelect label="Employee" name="employeeId" value={form.employeeId} onChange={handleChange} required>
          <option value="">-- Select employee --</option>
          {employees.map((e) => (
            <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
          ))}
        </FormSelect>
        <FormSelect label="Reviewer" name="reviewerId" value={form.reviewerId} onChange={handleChange} required>
          <option value="">-- Select reviewer --</option>
          {employees.map((e) => (
            <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
          ))}
        </FormSelect>
        <FormSelect label="Rating (1-5)" name="rating" value={form.rating} onChange={handleChange}>
          {[1, 2, 3, 4, 5].map((n) => <option key={n} value={n}>{n}</option>)}
        </FormSelect>
        <FormInput label="Feedback" name="feedback" value={form.feedback} onChange={handleChange} />
        <FormInput label="Review Date" type="date" name="reviewDate" value={form.reviewDate} onChange={handleChange} />
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
