import React, { useEffect, useState } from 'react'
import Modal from '../../components/Modal.jsx'
import FormInput from '../../components/FormInput.jsx'
import FormSelect from '../../components/FormSelect.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import { getDepartments } from '../../api/departmentApi'
import { getEmployees } from '../../api/employeeApi'

const EMPTY_FORM = {
  employeeCode: '',
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  jobTitle: '',
  salary: '',
  joiningDate: '',
  employmentStatus: 'ACTIVE',
  departmentId: '',
  managerId: '',
  address: { street: '', city: '', state: '', country: '', postalCode: '' },
}

export default function EmployeeFormModal({ initialData, onClose, onSubmit }) {
  const [form, setForm] = useState(EMPTY_FORM)
  const [departments, setDepartments] = useState([])
  const [employees, setEmployees] = useState([])
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    getDepartments().then((res) => setDepartments(res.data)).catch(() => {})
    getEmployees().then((res) => setEmployees(res.data)).catch(() => {})

    if (initialData) {
      setForm({
        employeeCode: initialData.employeeCode || '',
        firstName: initialData.firstName || '',
        lastName: initialData.lastName || '',
        email: initialData.email || '',
        phoneNumber: initialData.phoneNumber || '',
        jobTitle: initialData.jobTitle || '',
        salary: initialData.salary ?? '',
        joiningDate: initialData.joiningDate || '',
        employmentStatus: initialData.employmentStatus || 'ACTIVE',
        departmentId: initialData.department?.id || '',
        managerId: initialData.manager?.id || '',
        address: {
          street: initialData.address?.street || '',
          city: initialData.address?.city || '',
          state: initialData.address?.state || '',
          country: initialData.address?.country || '',
          postalCode: initialData.address?.postalCode || '',
        },
      })
    }
  }, [initialData])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })
  const handleAddressChange = (e) =>
    setForm({ ...form, address: { ...form.address, [e.target.name]: e.target.value } })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      const payload = {
        ...form,
        salary: form.salary === '' ? null : Number(form.salary),
        departmentId: form.departmentId === '' ? null : Number(form.departmentId),
        managerId: form.managerId === '' ? null : Number(form.managerId),
      }
      await onSubmit(payload)
    } catch (err) {
      const details = err.response?.data?.details
      setError(details ? details.join(', ') : err.response?.data?.message || 'Something went wrong')
      setSubmitting(false)
    }
  }

  return (
    <Modal title={initialData ? 'Edit Employee' : 'Add Employee'} onClose={onClose}>
      <ErrorMessage message={error} />
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <FormInput label="Employee Code" name="employeeCode" value={form.employeeCode} onChange={handleChange} required />
          <FormInput label="Job Title" name="jobTitle" value={form.jobTitle} onChange={handleChange} />
        </div>
        <div className="form-row">
          <FormInput label="First Name" name="firstName" value={form.firstName} onChange={handleChange} required />
          <FormInput label="Last Name" name="lastName" value={form.lastName} onChange={handleChange} required />
        </div>
        <div className="form-row">
          <FormInput label="Email" type="email" name="email" value={form.email} onChange={handleChange} required />
          <FormInput label="Phone Number" name="phoneNumber" value={form.phoneNumber} onChange={handleChange} />
        </div>
        <div className="form-row">
          <FormInput label="Salary" type="number" step="0.01" name="salary" value={form.salary} onChange={handleChange} />
          <FormInput label="Joining Date" type="date" name="joiningDate" value={form.joiningDate} onChange={handleChange} />
        </div>
        <div className="form-row">
          <FormSelect label="Employment Status" name="employmentStatus" value={form.employmentStatus} onChange={handleChange}>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
            <option value="RESIGNED">RESIGNED</option>
            <option value="TERMINATED">TERMINATED</option>
          </FormSelect>
          <FormSelect label="Department" name="departmentId" value={form.departmentId} onChange={handleChange}>
            <option value="">-- None --</option>
            {departments.map((d) => (
              <option key={d.id} value={d.id}>{d.name}</option>
            ))}
          </FormSelect>
        </div>
        <FormSelect label="Manager" name="managerId" value={form.managerId} onChange={handleChange}>
          <option value="">-- None --</option>
          {employees
            .filter((e) => !initialData || e.id !== initialData.id)
            .map((e) => (
              <option key={e.id} value={e.id}>{e.firstName} {e.lastName}</option>
            ))}
        </FormSelect>

        <h3 style={{ fontSize: 14, marginTop: 18, marginBottom: 8 }}>Address</h3>
        <div className="form-row">
          <FormInput label="Street" name="street" value={form.address.street} onChange={handleAddressChange} />
          <FormInput label="City" name="city" value={form.address.city} onChange={handleAddressChange} />
        </div>
        <div className="form-row">
          <FormInput label="State" name="state" value={form.address.state} onChange={handleAddressChange} />
          <FormInput label="Country" name="country" value={form.address.country} onChange={handleAddressChange} />
        </div>
        <FormInput label="Postal Code" name="postalCode" value={form.address.postalCode} onChange={handleAddressChange} />

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
