import axiosClient from './axiosClient'

export const getAllReviews = () => axiosClient.get('/performance-reviews')
export const getReviewsByEmployee = (employeeId) => axiosClient.get(`/performance-reviews/employee/${employeeId}`)
export const createReview = (payload) => axiosClient.post('/performance-reviews', payload)
