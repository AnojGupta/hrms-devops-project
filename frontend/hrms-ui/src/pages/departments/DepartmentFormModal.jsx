import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import { getEmployees } from '../../api/employeeApi'

export default function DepartmentFormModal({ initialData, onClose, onSubmit }) {
  const [form, setForm] = useState({ name: '', description: '', location: '', managerId: '' })
  const [employees, setEmployees] = useState([])
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})
    if (initialData) {
      setForm({
        name: initialData.name || '',
        description: initialData.description || '',
        location: initialData.location || '',
        managerId: initialData.manager?.id || '',
      })
    }
  }, [initialData])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await onSubmit({ ...form, managerId: form.managerId === '' ? null : Number(form.managerId) })
    } catch (err) {
      const details = err.response?.data?.details
      setError(details ? details.join(', ') : err.response?.data?.message || 'Something went wrong')
      setSubmitting(false)
    }
  }

  return (
    <Modal title={initialData ? 'Edit Department' : 'Add Department'} onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <FormInput label="Name" name="name" value={form.name} onChange={handleChange} required />
        <FormInput label="Description" name="description" value={form.description} onChange={handleChange} />
        <FormInput label="Location" name="location" value={form.location} onChange={handleChange} />
        <FormSelect label="Manager" name="managerId" value={form.managerId} onChange={handleChange}>
          <option value="">-- None --</option>
          {employees.map((e) => (
            <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
          ))}
        </FormSelect>
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
