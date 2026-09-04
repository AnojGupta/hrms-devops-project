import React, { useEffect, useState } from 'react'
import {
  getDepartments,
  createDepartment,
  updateDepartment,
  deleteDepartment,
} from '../../api/departmentApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import DepartmentFormModal from './DepartmentFormModal.jsx'

export default function DepartmentList() {
  const [departments, setDepartments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editing, setEditing] = useState(null)

  const load = () => {
    setLoading(true)
    getDepartments()
      .then((res) => setDepartments(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load departments'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleCreate = async (payload) => {
    await createDepartment(payload)
    setShowForm(false)
    load()
  }

  const handleUpdate = async (payload) => {
    await updateDepartment(editing.id, payload)
    setEditing(null)
    load()
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this department?')) return
    try {
      await deleteDepartment(id)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete department')
    }
  }

  return (
    <div>
      <div className="page-header">
        <h1>Departments</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Add Department</button>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : departments.length === 0 ? (
        <EmptyState message="No departments found." />
      ) : (
        <table>
          <thead>
            <tr><th>Name</th><th>Location</th><th>Manager</th><th>Employees</th><th></th></tr>
          </thead>
          <tbody>
            {departments.map((d) => (
              <tr key={d.id}>
                <td>{d.name}</td>
                <td>{d.location || '-'}</td>
                <td>{d.manager ? `${d.manager.firstName} ${d.manager.lastName}` : '-'}</td>
                <td>{d.employeeCount}</td>
                <td>
                  <button className="btn btn-secondary btn-sm" onClick={() => setEditing(d)}>Edit</button>{' '}
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(d.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && <DepartmentFormModal onClose={() => setShowForm(false)} onSubmit={handleCreate} />}
      {editing && <DepartmentFormModal initialData={editing} onClose={() => setEditing(null)} onSubmit={handleUpdate} />}
    </div>
  )
}
