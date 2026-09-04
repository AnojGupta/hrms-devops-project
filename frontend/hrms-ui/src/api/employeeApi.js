import axiosClient from './axiosClient'

export const getEmployees = () => axiosClient.get('/employees')
export const getEmployee = (id) => axiosClient.get(`/employees/${id}`)
export const searchEmployees = (keyword) => axiosClient.get('/employees/search', { params: { keyword } })
export const createEmployee = (payload) => axiosClient.post('/employees', payload)
export const updateEmployee = (id, payload) => axiosClient.put(`/employees/${id}`, payload)
export const deleteEmployee = (id) => axiosClient.delete(`/employees/${id}`)
