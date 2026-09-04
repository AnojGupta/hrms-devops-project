import React, { useEffect, useState } from 'react'
import { getProjects, createProject, updateProject, deleteProject } from '../../api/projectApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import EmptyState from '../../components/EmptyState.jsx'
import StatusBadge from '../../components/StatusBadge.jsx'
import ProjectFormModal from './ProjectFormModal.jsx'
import AssignEmployeeModal from './AssignEmployeeModal.jsx'

export default function ProjectList() {
  const [projects, setProjects] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editing, setEditing] = useState(null)
  const [assigningTo, setAssigningTo] = useState(null)

  const load = () => {
    setLoading(true)
    getProjects()
      .then((res) => setProjects(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load projects'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleCreate = async (payload) => {
    await createProject(payload)
    setShowForm(false)
    load()
  }

  const handleUpdate = async (payload) => {
    await updateProject(editing.id, payload)
    setEditing(null)
    load()
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this project?')) return
    try {
      await deleteProject(id)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete project')
    }
  }

  return (
    <div>
      <div className="page-header">
        <h1>Projects</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Add Project</button>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <Loader />
      ) : projects.length === 0 ? (
        <EmptyState message="No projects found." />
      ) : (
        <table>
          <thead>
            <tr><th>Code</th><th>Name</th><th>Status</th><th>Start</th><th>End</th><th>Budget</th><th></th></tr>
          </thead>
          <tbody>
            {projects.map((p) => (
              <tr key={p.id}>
                <td>{p.projectCode}</td>
                <td>{p.name}</td>
                <td><StatusBadge status={p.status} /></td>
                <td>{p.startDate || '-'}</td>
                <td>{p.endDate || '-'}</td>
                <td>{p.budget ?? '-'}</td>
                <td>
                  <button className="btn btn-secondary btn-sm" onClick={() => setAssigningTo(p)}>Assign</button>{' '}
                  <button className="btn btn-secondary btn-sm" onClick={() => setEditing(p)}>Edit</button>{' '}
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(p.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && <ProjectFormModal onClose={() => setShowForm(false)} onSubmit={handleCreate} />}
      {editing && <ProjectFormModal initialData={editing} onClose={() => setEditing(null)} onSubmit={handleUpdate} />}
      {assigningTo && (
        <AssignEmployeeModal
          project={assigningTo}
          onClose={() => setAssigningTo(null)}
          onAssigned={() => { setAssigningTo(null); load(); }}
        />
      )}
    </div>
  )
}
