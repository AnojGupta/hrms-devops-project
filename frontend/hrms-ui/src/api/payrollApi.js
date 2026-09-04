import axiosClient from './axiosClient'

export const getAllPayroll = () => axiosClient.get('/payroll')
export const getPayrollByEmployee = (employeeId) => axiosClient.get(`/payroll/employee/${employeeId}`)
export const createPayroll = (payload) => axiosClient.post('/payroll', payload)
