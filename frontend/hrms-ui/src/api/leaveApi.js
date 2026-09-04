import axiosClient from './axiosClient'

export const getLeaves = () => axiosClient.get('/leaves')
export const getLeave = (id) => axiosClient.get(`/leaves/${id}`)
export const applyLeave = (payload) => axiosClient.post('/leaves', payload)
export const approveLeave = (id, approverEmployeeId) => axiosClient.put(`/leaves/${id}/approve`, { approverEmployeeId })
export const rejectLeave = (id, approverEmployeeId) => axiosClient.put(`/leaves/${id}/reject`, { approverEmployeeId })
