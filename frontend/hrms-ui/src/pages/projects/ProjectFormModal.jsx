import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'

const EMPTY_FORM = {
  projectCode: '', name: '', description: '', startDate: '', endDate: '', status: 'PLANNED', budget: '',
}

export default function ProjectFormModal({ initialData, onClose, onSubmit }) {
  const [form, setForm] = useState(EMPTY_FORM)
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    if (initialData) {
      setForm({
        projectCode: initialData.projectCode || '',
        name: initialData.name || '',
        description: initialData.description || '',
        startDate: initialData.startDate || '',
        endDate: initialData.endDate || '',
        status: initialData.status || 'PLANNED',
        budget: initialData.budget ?? '',
      })
    }
  }, [initialData])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await onSubmit({ ...form, budget: form.budget === '' ? null : Number(form.budget) })
    } catch (err) {
      const details = err.response?.data?.details
      setError(details ? details.join(', ') : err.response?.data?.message || 'Something went wrong')
      setSubmitting(false)
    }
  }

  return (
    <Modal title={initialData ? 'Edit Project' : 'Add Project'} onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <FormInput label="Project Code" name="projectCode" value={form.projectCode} onChange={handleChange} required />
          <FormInput label="Name" name="name" value={form.name} onChange={handleChange} required />
        </div>
        <FormInput label="Description" name="description" value={form.description} onChange={handleChange} />
        <div className="form-row">
          <FormInput label="Start Date" type="date" name="startDate" value={form.startDate} onChange={handleChange} />
          <FormInput label="End Date" type="date" name="endDate" value={form.endDate} onChange={handleChange} />
        </div>
        <div className="form-row">
          <FormSelect label="Status" name="status" value={form.status} onChange={handleChange}>
            <option value="PLANNED">PLANNED</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="COMPLETED">COMPLETED</option>
            <option value="ON_HOLD">ON_HOLD</option>
          </FormSelect>
          <FormInput label="Budget" type="number" step="0.01" name="budget" value={form.budget} onChange={handleChange} />
        </div>
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
