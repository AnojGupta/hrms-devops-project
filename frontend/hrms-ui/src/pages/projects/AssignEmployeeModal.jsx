import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import { getEmployees } from '../../api/employeeApi'
import { assignEmployeeToProject } from '../../api/projectApi'

export default function AssignEmployeeModal({ project, onClose, onAssigned }) {
  const [employees, setEmployees] = useState([])
  const [employeeId, setEmployeeId] = useState('')
  const [roleInProject, setRoleInProject] = useState('')
  const [assignedDate, setAssignedDate] = useState('')
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!employeeId) {
      setError('Please select an employee')
      return
    }
    setError('')
    setSubmitting(true)
    try {
      await assignEmployeeToProject(project.id, employeeId, { roleInProject, assignedDate: assignedDate || null })
      onAssigned()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to assign employee')
      setSubmitting(false)
    }
  }

  return (
    <Modal title={`Assign employee to ${project.name}`} onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <FormSelect label="Employee" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} required>
          <option value="">-- Select employee --</option>
          {employees.map((e) => (
            <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
          ))}
        </FormSelect>
        <FormInput label="Role in Project" value={roleInProject} onChange={(e) => setRoleInProject(e.target.value)} />
        <FormInput label="Assigned Date" type="date" value={assignedDate} onChange={(e) => setAssignedDate(e.target.value)} />
        <div className="modal-actions">
          <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Assigning...' : 'Assign'}
          </button>
        </div>
      </form>
    </Modal>
  )
}
