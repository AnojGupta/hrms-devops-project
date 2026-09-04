import axiosClient from './axiosClient'

export const getAllAttendance = () => axiosClient.get('/attendance')
export const getAttendanceByEmployee = (employeeId) => axiosClient.get(`/attendance/employee/${employeeId}`)
export const checkIn = (employeeId) => axiosClient.post('/attendance/check-in', { employeeId })
export const checkOut = (employeeId) => axiosClient.put('/attendance/check-out', { employeeId })
