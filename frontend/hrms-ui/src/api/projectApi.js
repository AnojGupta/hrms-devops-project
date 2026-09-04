import axiosClient from './axiosClient'

export const getProjects = () => axiosClient.get('/projects')
export const getProject = (id) => axiosClient.get(`/projects/${id}`)
export const createProject = (payload) => axiosClient.post('/projects', payload)
export const updateProject = (id, payload) => axiosClient.put(`/projects/${id}`, payload)
export const deleteProject = (id) => axiosClient.delete(`/projects/${id}`)
export const assignEmployeeToProject = (projectId, employeeId, payload) =>
  axiosClient.post(`/projects/${projectId}/employees/${employeeId}`, payload)
