import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import {
  getEmployees,
  searchEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
} from '../../api/employeeApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import StatusBadge from '../../components/StatusBadge.jsx'
import EmployeeFormModal from './EmployeeFormModal.jsx'

export default function EmployeeList() {
  const [employees, setEmployees] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [keyword, setKeyword] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingEmployee, setEditingEmployee] = useState(null)

  const loadEmployees = () => {
    setLoading(true)
    getEmployees()
      .then((res) => setEmployees(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load employees'))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadEmployees()
  }, [])

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!keyword.trim()) {
      loadEmployees()
      return
    }
    setLoading(true)
    try {
      const res = await searchEmployees(keyword.trim())
      setEmployees(res.data)
    } catch (err) {
      setError(err.response?.data?.message || 'Search failed')
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (payload) => {
    await createEmployee(payload)
    setShowForm(false)
    loadEmployees()
  }

  const handleUpdate = async (payload) => {
    await updateEmployee(editingEmployee.id, payload)
    setEditingEmployee(null)
    loadEmployees()
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this employee? This cannot be undone.')) return
    try {
      await deleteEmployee(id)
      loadEmployees()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete employee')
    }
  }

  return (
    <div>
      <div className="page-header">
        <h1>Employees</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Add Employee</button>
      </div>

      <form className="toolbar" onSubmit={handleSearch}>
        <input
          className="form-control"
          style={{ maxWidth: 320 }}
          placeholder="Search by name, code, or email..."
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button className="btn btn-secondary" type="submit">Search</button>
      </form>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : employees.length === 0 ? (
        <EmptyState message="No employees found." />
      ) : (
        <table>
          <thead>
            <tr>
              <th>Code</th>
              <th>Name</th>
              <th>Job Title</th>
              <th>Department</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {employees.map((emp) => (
              <tr key={emp.id}>
                <td>{emp.employeeCode}</td>
                <td><Link to={`/employees/${emp.id}`}>{emp.firstName} {emp.lastName}</Link></td>
                <td>{emp.jobTitle || '-'}</td>
                <td>{emp.department?.name || '-'}</td>
                <td><StatusBadge status={emp.employmentStatus} /></td>
                <td>
                  <button className="btn btn-secondary btn-sm" onClick={() => setEditingEmployee(emp)}>Edit</button>{' '}
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(emp.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <EmployeeFormModal onClose={() => setShowForm(false)} onSubmit={handleCreate} />
      )}
      {editingEmployee && (
        <EmployeeFormModal
          initialData={editingEmployee}
          onClose={() => setEditingEmployee(null)}
          onSubmit={handleUpdate}
        />
      )}
    </div>
  )
}
